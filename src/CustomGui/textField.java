package CustomGui;


import javafx.scene.control.TextField;

public class textField extends TextField {

    public textField(){
        super();
        this.setOnKeyTyped(e->textField.this.setStyle(null));
    }

    //control if the text field is empty and show red boarder
    public boolean isEmpty(){
        if(this.getText().isEmpty()){
            this.setStyle("-fx-border-color:red;-fx-border-radius:3px;-fx-border-size: 1px;");
            return true;
        }
        return false;
    }
}
