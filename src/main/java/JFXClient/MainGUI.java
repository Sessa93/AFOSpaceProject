package JFXClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.input.KeyCode;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainGUI extends Application {

    private static Font font;
    private MenuBox menu;

    // Main menu creation
    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        // Load the fonts
        try {
            InputStream fontStream = Files.newInputStream(Paths.get("fonts/Capture_it.ttf"));
            font = Font.loadFont(fontStream,50);
        }
        catch (IOException e) {
            System.out.println("Could not load fonts!");
            System.exit(0);
        }

        //Load the images
        try {
            InputStream is = Files.newInputStream(Paths.get("images/menuImg.png"));
            ImageView img = new ImageView(new Image(is));

            img.setFitWidth(1000);
            img.setPreserveRatio(true);
            root.setCenter(img);
        }
        catch (IOException e) {
            System.out.println("Could not load images!");
        }

        //Load audio files
        Sound hoverSound = new Sound("sounds/menu_hovering.mp3");

        MenuItem itemQuit = new MenuItem("QUIT", hoverSound);
        itemQuit.setOnMouseClicked(event -> System.exit(0));

        menu = new MenuBox("MENU", font,
                new MenuItem("NEW GAME", hoverSound),
                new MenuItem("OPTIONS", hoverSound),
                new MenuItem("CREDITS", hoverSound),
                new MenuItem("MAIN MENU", hoverSound),
                itemQuit);

        root.setLeft(menu);
        root.setStyle("-fx-background-color: #000000;");
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (menu.isVisible()) {
                    menu.hide();
                }
                else {
                    menu.show();
                }
            }
        });
        primaryStage.setTitle("Escape from Aliens in the outer space");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}