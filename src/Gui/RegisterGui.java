package Gui;

import Agents.LoginAgent;
import CustomGui.PasswordField2;
import CustomGui.textField;
import com.example.android.distributeurdeau.models.Farmer;
import jade.gui.GuiEvent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterGui {
    LoginAgent agent;
    Farmer farmer;

    Label nameLabel = new Label("Nom");
    Label prenameLabel = new Label("Prenom");
    Label numAgriLabel = new Label("Numero Agriculteur");
    Label passwordLabel = new Label("mot de passe");
    Label confirmPasswordLabel = new Label("confirmer le mot de passe");

    textField lname = new textField();
    textField fname = new textField();
    textField farmer_num = new textField();

    PasswordField2 password = new PasswordField2();
    PasswordField2 confirmpassword = new PasswordField2();
    CheckBox risk = new CheckBox("je prent le risque");

    Button create = new Button("S'enregistrer") ;
    Stage window = new Stage();


    public RegisterGui(){
        GridPane layout = new GridPane();
        layout.setVgap(20);
        layout.setHgap(20);


        layout.setConstraints(nameLabel,1,1);
        layout.setConstraints(prenameLabel,1,2);
        layout.setConstraints(numAgriLabel,1,3);
        layout.setConstraints(passwordLabel,1,4);
        layout.setConstraints(confirmPasswordLabel,1,5);
        layout.setConstraints(risk,2,6);
        layout.setConstraints(create,2,7,2,1);

        layout.setConstraints(lname,2,1);
        layout.setConstraints(fname,2,2);
        layout.setConstraints(farmer_num,2,3);
        layout.setConstraints(password,2,4,2,1);
        layout.setConstraints(confirmpassword,2,5,2,1);



        layout.getChildren().addAll(nameLabel, lname,prenameLabel, fname,numAgriLabel,
                farmer_num,passwordLabel,password,
                confirmPasswordLabel,confirmpassword,risk,create);


        create.setOnAction(e->{
            if(checkFields()){
               farmer = new Farmer(farmer_num.getText().toLowerCase(), fname.getText(), lname.getText(),password.getText(),risk.isSelected());
                GuiEvent guiEvent = new GuiEvent(this,2);
                guiEvent.addParameter(farmer);
                agent.onGuiEvent(guiEvent);
            }
        });


        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.setTitle("Nouveau Agriculteur");
        window.setMinWidth(480);
        window.setMinHeight(370);
        window.show();
    }

    public boolean checkFields(){
        //test all text fields and show red boarder if there is an error!
        return !lname.isEmpty() && !fname.isEmpty() && !farmer_num.isEmpty()
                && !password.isEmpty() && !confirmpassword.isEmpty()
                && !confirmpassword.Equalse(password);
    }



    public LoginAgent getAgent() {
        return agent;
    }

    public void setAgent(LoginAgent agent) {
        this.agent = agent;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public void close() {
        Platform.runLater(()->{
            window.close();
        });
    }

    public void showError(){
        System.out.println("Error");
    }


}
