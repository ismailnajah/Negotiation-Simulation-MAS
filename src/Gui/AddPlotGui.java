package Gui;


import Agents.FarmerAgent;
import CustomGui.NumberField;
import CustomGui.textField;
import com.example.android.distributeurdeau.models.Plot;
import jade.gui.GuiEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddPlotGui {
    HomeGui home;

    Label plotNameLabel = new Label("Nom du parcelle");
    Label typeLabel = new Label("Type de culture");
    Label areaLabel = new Label("Superficie en m²");
    Label dateLabele = new Label("Date de semis");
    Label qte_water_label = new Label("Quantité d'eau demandé");

    textField plotnameTF = new textField();
    DatePicker s_datePK = new DatePicker();
    textField typeTF = new textField();
    NumberField areaNF = new NumberField();

    NumberField qte_water = new NumberField();

    Button saveB = new Button("Enregistrer");
    Stage window = new Stage();
    public AddPlotGui(HomeGui home){

        this.home = home;

        GridPane layout = new GridPane();
        layout.setHgap(20);
        layout.setVgap(20);
        layout.setPadding(new Insets(20));

        //Plot name
        layout.setConstraints(plotNameLabel,1,0);
        layout.setConstraints(plotnameTF,2,0);
        //typeTF de culture
        layout.setConstraints(typeLabel,1,1);
        layout.setConstraints(typeTF,2,1);

        //superfice du parcelle
        layout.setConstraints(areaLabel,1,2);
        layout.setConstraints(areaNF,2,2);
        //date de semi
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        layout.setConstraints(dateLabele,1,3);
        layout.setConstraints(s_datePK,2,3);
        s_datePK.setEditable(false);
        s_datePK.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if(object != null)
                    return formatter.format(object);
                return null;
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.trim().isEmpty())
                    return LocalDate.parse(string,formatter);
                return null;
            }
        });


        //water quantity
        layout.setConstraints(qte_water_label,1,4);
        layout.setConstraints(qte_water,2,4);

        layout.setConstraints(saveB,2,7,2,1);

        layout.getChildren().addAll(plotNameLabel,plotnameTF,typeLabel,typeTF, areaLabel,
                        areaNF, s_datePK,dateLabele,qte_water_label,qte_water,saveB);

        saveB.setOnAction(event->{
            if(CheckFields()){
                //Plot(Farmer farmer, String p_name, String type,Date s_date, float area, float water_qte
                Date s_date = Date.valueOf(s_datePK.getValue());
                Plot plot = new Plot(home.farmer,plotnameTF.getText(),typeTF.getText(),s_date,Float.parseFloat(areaNF.getText())
                        ,Float.parseFloat(qte_water.getText()));
                GuiEvent guiEvent = new GuiEvent(this,2);
                guiEvent.addParameter(plot);
                home.farmerAgent.onGuiEvent(guiEvent);
                home.addPlot(plot);
                window.close();
            }
        });

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.setTitle("Nouveau Parcelle");
        window.setMinWidth(480);
        window.setMinHeight(370);
        window.show();
    }

    boolean CheckFields(){
        return !plotnameTF.isEmpty() && !areaNF.isEmpty() && !typeTF.isEmpty() && !qte_water.isEmpty()
                && s_datePK.getValue() != null;
    }
}

