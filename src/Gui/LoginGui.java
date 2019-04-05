package Gui;

import Agents.FarmerAgent;
import Agents.LoginAgent;
import CustomGui.PasswordField2;
import CustomGui.textField;
import Gui.HomeGui;
import Gui.MainWindow;
import Gui.RegisterGui;
import com.example.android.distributeurdeau.models.Farmer;
import jade.gui.GuiEvent;
import jade.wrapper.StaleProxyException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginGui extends Stage {
    LoginAgent agent;

    Label loginLabel = new Label("Numero Farmer");
    textField NumeroAgriculteur = new textField();

    Label passwordLabel = new Label("mot de passe");
    PasswordField2 password = new PasswordField2();

    Button connect = new Button("Connecter");
    Button newUser = new Button("Nouveau Farmer");

    public LoginGui(){

        VBox container = new VBox();
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(0, 20, 20, 20));
        layout.setHgap(20);
        layout.setVgap(20);
        HBox header = new HBox();


        header.setPadding(new Insets(10, 10, 0, 0));
        header.setPrefSize(10, 10);
        header.setAlignment(Pos.TOP_RIGHT);

        layout.setConstraints(loginLabel, 2, 1);
        layout.setConstraints(NumeroAgriculteur, 3, 1);
        layout.setConstraints(passwordLabel, 2, 2);
        layout.setConstraints(password, 3, 2, 2, 1);
        layout.setConstraints(connect, 2, 4);
        layout.setConstraints(newUser, 3, 4);

        layout.getChildren().addAll(loginLabel, NumeroAgriculteur, passwordLabel, password, connect, newUser);

        container.getChildren().addAll(layout);

        newUser.setOnAction(e -> {
            agent.setRegister(new RegisterGui());
            agent.getRegister().setAgent(agent);
        });


        //establish connection
        connect.setOnAction(event -> {
            GuiEvent guiEvent = new GuiEvent(event, 1);
            guiEvent.addParameter(NumeroAgriculteur.getText());
            guiEvent.addParameter(password.getText());
            agent.onGuiEvent(guiEvent);
        });

        Scene scene = new Scene(container);
        this.setTitle("Se Connecter");
        this.setMinHeight(250);
        this.setMinWidth(440);
        this.setScene(scene);
    }

    public void setAgent(LoginAgent agent) {
        this.agent = agent;
    }

    public void deploy(Farmer farmer) {
        try {
            HomeGui homeGui = new HomeGui(farmer);
            MainWindow.controller = MainWindow.container.createNewAgent(farmer.getFarmer_num(), FarmerAgent.class.getName(),new Object[]{farmer,homeGui});
            MainWindow.controller.start();
            MainWindow.update(homeGui);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
