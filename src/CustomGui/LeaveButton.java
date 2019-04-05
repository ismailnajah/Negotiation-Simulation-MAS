package CustomGui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class LeaveButton extends Button {

    public LeaveButton() {
        super();
        Image image = new Image(getClass().getResourceAsStream("logout.png"), 30, 30, true, true);
        this.setGraphic(new ImageView(image));
        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(5));
        this.setShape(new Circle(image.getHeight()));
    }

}
