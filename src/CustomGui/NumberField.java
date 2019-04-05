package CustomGui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class NumberField extends textField {

    public NumberField() {
        super();
        this.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,10}([\\.]\\d{0,4})?"))
                    NumberField.this.setText(oldValue);
            }
        });
    }
}
