package it.polimi.ingsw.cg_19;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.Graphs;

import common.Coordinate;
import common.Sector;
import common.SectorType;

/**
 * Represents a generic map in the game Use the JGraphT library to represent the
 * game map structure in terms of an undirected graph
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 */
public class GameMap {

	// The map's associated graph
	private UndirectedGraph<Sector, DefaultEdge> graph;
	// A searchable version of the map's associated graph
	private NeighborIndex<Sector, DefaultEdge> searchableGraph;
	// The human starting sector
	private final Sector humanSector;
	// The alien starting sector
	private final Sector alienSector;
	// The horizontal span of the map
	private final int horizontalLength;
	// The vertical span of the map
	private final int verticalLength;
	// The starting horizontal coordinate
	private final int startingHorizontalCoord;
	// The starting vertical coordinate
	private final int startingVerticalCoord;
	// The map's name
	private final String name;
	// The rescue sectors of the map
	private List<Sector> escapes;

	/**
	 * Constructs a generic map in the game from an undirected graph, from
	 * informations about its table like representation and from its name. A
	 * searchable version of the undirected graph given is created and
	 * references to the map's alien sectors and human sectors are saved.
	 * 
	 * @param graph
	 *            the graph associated with the map
	 * @param startingHorizontalCoord
	 *            considering a table like representation of the map, the map's
	 *            starting horizontal coordinate
	 * @param startingVerticolCoord
	 *            considering a table like representation of the map, the map's
	 *            starting vertical coordinate of the map
	 * @param horizontalLength
	 *            considering a table like representation of the map, the map's
	 *            number of columns
	 * @param verticalLength
	 *            considering a table like representation of the map, the map's
	 *            number of rows
	 * @param name
	 *            the map's name
	 */
	public GameMap(UndirectedGraph<Sector, DefaultEdge> graph,
			int startingHorizontalCoord, int startingVerticalCoord,
			int horizontalLength, int verticalLength, String name) {
		this.name = name;
		this.graph = graph;
		this.searchableGraph = new NeighborIndex<Sector, DefaultEdge>(
				this.graph);
		this.startingHorizontalCoord = startingHorizontalCoord;
		this.startingVerticalCoord = startingVerticalCoord;
		this.horizontalLength = horizontalLength;
		this.verticalLength = verticalLength;
		this.humanSector = this.getSectorByType(SectorType.HUMAN);
		this.alienSector = this.getSectorByType(SectorType.ALIEN);
		this.escapes = this.findEscapes();
	}

	/**
	 * Gets the map's name
	 * 
	 * @return the map's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Considering a table like representation of the map, gets the map's
	 * starting horizontal coordinate
	 * 
	 * @return the map's starting horizontal coordinate
	 */
	public int getStartingHorizontalCoord() {
		return startingHorizontalCoord;
	}

	/**
	 * Considering a table like representation of the map, gets the map's
	 * starting vertical coordinate
	 * 
	 * @return the map's starting vertical coordinate
	 */
	public int getStartingVerticalCoord() {
		return startingVerticalCoord;
	}

	/**
	 * Considering a table like representation of the map, gets the map's number
	 * of rows
	 * 
	 * @return the map's number of rows
	 */
	public int getVerticalLength() {
		return verticalLength;
	}

	/**
	 * Considering a table like representation of the map, gets the map's number
	 * of columns
	 * 
	 * @return the map's number of columns
	 */
	public int getHorizontalLength() {
		return horizontalLength;
	}

	/**
	 * @return A reference to the graph data structure, used for testing
	 *         purposes
	 */
	public UndirectedGraph<Sector, DefaultEdge> getGraph() {
		return this.graph;
	}

	/**
	 * Gets the map's searchable graph
	 * 
	 * @return the map's searchable graph
	 */
	public NeighborIndex<Sector, DefaultEdge> getSearchableGraph() {
		return searchableGraph;
	}

	/**
	 * Gets the map's human sector
	 * 
	 * @return Gets the map's human sector
	 */
	public Sector getHumanSector() {
		return humanSector;
	}

	/**
	 * Gets the map's alien sector
	 * 
	 * @return Gets the map's alien sector
	 */
	public Sector getAlienSector() {
		return alienSector;
	}

	/**
	 * Gets a map's sector from a given coordinate
	 * 
	 * @param coordinate
	 *            the coordinate of the sector to be returned
	 * @return the map's sector whose coordinate is the one specified
	 */
	public Sector getSectorByCoords(Coordinate coordinate) {
		/*
		 * Get list of sectors in the graph and scans this list in order to find
		 * the correct sector if the sector doesn't exist returns null
		 */
		Set<Sector> sectors = this.graph.vertexSet();
		for (Sector s : sectors) {
			if (s.getCoordinate().equals(coordinate)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Gets the first map's sector that matches a given sector type
	 * 
	 * @param sectorType
	 *            the type of sector, whose first occurrence in the map we want
	 *            to return
	 * @return the first map's sector that matches the given sector type
	 */
	public Sector getSectorByType(SectorType sectorType) {
		/*
		 * Get list of sectors in the graph and scans this list in order to find
		 * the correct sector if the sector doesn't exist returns null
		 */
		Set<Sector> sectors = this.graph.vertexSet();
		for (Sector s : sectors) {
			if (s.getSectorType() == sectorType) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Checks if two map's sector are adjacent according to a given maximum
	 * distance
	 * 
	 * @param sourceSector
	 *            the first sector
	 * @param targetSector
	 *            the second sector
	 * @param playerType
	 *            the type of player, true if human, false if alien
	 * 
	 * @param adrenalined
	 *            If True then the function return True iff the distance between
	 *            sourceSector and targetSector equals to maxLen, if False than
	 *            the distance between sourceSector and targetSecto could be
	 *            lower or equals to maxLen
	 * @return true if source and target are adjacent according to player and
	 *         speed
	 */
	public boolean checkSectorAdiacency(Sector sourceSector,
			Sector targetSector, int speed,
			PlayerType playerType, boolean adrenalined) {


		/*
		 * A recursive function. At each level of recursion source contains the
		 * sector from which building the path toward target. If source isn't
		 * crossable by player(p) or if the current path length, depth, is equal
		 * to speed without reaching the target, the current branch of the
		 * recursion ends with false otherwise the recursion proceeds until
		 * source == target and the correct path length(depth) has been reached.
		 */

		// If Human
		if (playerType == PlayerType.HUMAN) {
			if(targetSector.getSectorLegality() == SectorLegality.NONE) return false;
			else {
				List<Sector> levelOneNeighbors = Graphs.neighborListOf(this.graph, sourceSector);
				List<Sector> levelTwoNeighbors = new ArrayList<Sector>();

				if(adrenalined) {
					for (Sector s1 : levelOneNeighbors) {
						if (s1.getSectorLegality() == SectorLegality.ALL || s1.getSectorLegality() == SectorLegality.HUMAN) {
							for (Sector s2 : Graphs.neighborListOf(this.graph, s1)) {
								levelTwoNeighbors.add(s2);
							}
						}
					}
					if(levelTwoNeighbors.contains(targetSector)) return true;
					return false;
				}
				if(levelOneNeighbors.contains(targetSector)) return true;
				return false;
			}
		}
		// If Alien
		else {
			if(targetSector.getSectorLegality() == SectorLegality.NONE || targetSector.getSectorLegality() == SectorLegality.HUMAN) return false;
			else {
				List<Sector> levelOneNeighbors = Graphs.neighborListOf(this.graph, sourceSector);
				List<Sector> levelTwoNeighbors = new ArrayList<Sector>();
				List<Sector> levelThreeNeighbors = new ArrayList<Sector>();

				for (Sector s1 : levelOneNeighbors) {
					if (s1.getSectorLegality() == SectorLegality.ALL) {
						for (Sector s2 : Graphs.neighborListOf(this.graph, s1)) {
							levelTwoNeighbors.add(s2);
						}
					}
				}
				if(speed > 2) {
					for (Sector s2 : levelTwoNeighbors) {
						if (s2.getSectorLegality() == SectorLegality.ALL) {
							for (Sector s3 : Graphs.neighborListOf(this.graph, s2)) {
								levelThreeNeighbors.add(s3);
							}
						}
					}
				}
				if(levelOneNeighbors.contains(targetSector) || levelTwoNeighbors.contains(targetSector) || levelThreeNeighbors.contains(targetSector)) return true;
				return false;
			}
		}
	}

	/**
	 * Find the rescue sector in the map
	 * 
	 * @return A list of the rescue sector of the map
	 */
	private List<Sector> findEscapes() {
		List<Sector> toReturn = new ArrayList<Sector>();
		Set<Sector> sectors = this.graph.vertexSet();
		for (Sector s : sectors) {
			if (s.getSectorType() == SectorType.OPEN_RESCUE
					|| s.getSectorType() == SectorType.CLOSED_RESCUE) {
				toReturn.add(s);

			}
		}
		return toReturn;
	}

	/**
	 * @return True if there is still an escape for the human players
	 */
	public boolean existEscapes() {
		for (Sector s : escapes) {
			if (s.getSectorType() == SectorType.OPEN_RESCUE)
				return true;
		}
		return false;
	}
}
