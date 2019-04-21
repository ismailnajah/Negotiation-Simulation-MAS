package CustomGui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class RButton extends Button {

    public RButton(String path,int width,int hight,int padding) {
        super();
        Image image = new Image(getClass().getResourceAsStream(path), width, hight, true, true);
        this.setGraphic(new ImageView(image));
        this.setPadding(new Insets(padding));
        this.setShape(new Circle(image.getWidth()));
    }

}
