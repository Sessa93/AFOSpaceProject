package JFXClient;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Andrea Sessa
 * Represents the box containing the menu
 */

public class MenuBox extends StackPane {

    public MenuBox(String title, Font font, MenuItem... items) {
        Rectangle bg = new Rectangle(300, 600);
        bg.setOpacity(0.2);

        DropShadow shadow = new DropShadow(7, 5, 0, Color.BLACK);
        shadow.setSpread(0.8);

        bg.setEffect(shadow);

        Text text = new Text(title + "   ");
        text.setFont(font);
        text.setFill(Color.WHITE);

        Line hSep = new Line();
        hSep.setEndX(250);
        hSep.setStroke(Color.DARKGREEN);
        hSep.setOpacity(0.4);

        Line vSep = new Line();
        vSep.setStartX(300);
        vSep.setEndX(300);
        vSep.setEndY(600);
        vSep.setStroke(Color.DARKGREEN);
        vSep.setOpacity(0.4);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.setPadding(new Insets(60, 0, 0, 0));
        vbox.getChildren().addAll(text, hSep);
        vbox.getChildren().addAll(items);

        setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(bg, vSep, vbox);
    }

    public void show() {
        setVisible(true);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), this);
        tt.setToX(0);
        tt.play();
    }

    public void hide() {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), this);
        tt.setToX(-300);
        tt.setOnFinished(event -> setVisible(false));
        tt.play();
    }
}