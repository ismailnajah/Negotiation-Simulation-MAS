package Gui;

import com.example.android.distributeurdeau.models.Plot;
import jade.gui.GuiEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RemovePlotGui{

    public RemovePlotGui(String plot_name,HomeGui home){
        Stage window = new Stage();
        VBox layout = new VBox();
        HBox buttons = new HBox();
        Scene scene = new Scene(layout);
        Button yesB = new Button("Oui");
        Button noB = new Button("Non");

        Label message = new Label("Vouz voulez supprimer la parcelle " + plot_name+" ?");



        buttons.setSpacing(50);
        buttons.getChildren().addAll(yesB,noB);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15,0,0,0));

        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(message,buttons);

        yesB.setOnAction(event-> {
            Plot plot = home.getPlot(plot_name);
            GuiEvent guiEvent = new GuiEvent(this,3);
            guiEvent.addParameter(plot);
            home.farmerAgent.onGuiEvent(guiEvent);
            home.removePlot(plot_name);
            window.close();
        });

        noB.setOnAction(event->{
            window.close();
        });

        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.setTitle("Suppreimer une parcelle");
        window.setMinWidth(340);
        window.setResizable(false);
        window.show();
    }


}
