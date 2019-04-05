package CustomGui;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PasswordField2 extends HBox {
    PasswordField maskedPassword = new PasswordField();
    textField unmaskedPassword = new textField();
    CheckBox checkBox = new CheckBox("Afficher");

    public PasswordField2(){
        super();
        unmaskedPassword.setManaged(false);
        unmaskedPassword.setVisible(false);

        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        unmaskedPassword.managedProperty().bind(checkBox.selectedProperty());
        unmaskedPassword.visibleProperty().bind(checkBox.selectedProperty());

        maskedPassword.managedProperty().bind(checkBox.selectedProperty().not());
        maskedPassword.visibleProperty().bind(checkBox.selectedProperty().not());

        // Bind the textField and passwordField text values bidirectionally.
        unmaskedPassword.textProperty().bindBidirectional(maskedPassword.textProperty());

        this.getChildren().addAll(maskedPassword,unmaskedPassword,checkBox);
        checkBox.setPadding(new Insets(0,0,0,4));

        unmaskedPassword.setOnKeyTyped(e->{
            unmaskedPassword.setStyle(null);
            maskedPassword.setStyle(null);
        });
        maskedPassword.setOnKeyTyped(e->{
            unmaskedPassword.setStyle(null);
            maskedPassword.setStyle(null);
        });
    }

    public String getText(){
        return maskedPassword.getText();
    }

    public boolean isEmpty(){
        if(unmaskedPassword.getText().isEmpty()){
            this.redBorder();
            return false;
        }
        return true;
    }

    public boolean Equalse(PasswordField2 password){
        if(!unmaskedPassword.getText().equals(password.getText())){
            this.redBorder();
            return false;
        }
        return true;
    }

    public void redBorder(){
        unmaskedPassword.setStyle("-fx-border-color:red;-fx-border-radius:3px;-fx-border-size: 1px;");
        maskedPassword.setStyle("-fx-border-color:red;-fx-border-radius:3px;-fx-border-size: 1px;");
    }
}
