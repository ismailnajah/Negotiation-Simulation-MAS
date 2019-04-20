package Gui;

import Agents.FarmerAgent;
import Agents.LoginAgent;
import CustomGui.RButton;
import CustomGui.NumberField;
import CustomGui.textField;
import com.example.android.distributeurdeau.models.Farmer;
import com.example.android.distributeurdeau.models.Plot;
import jade.gui.GuiEvent;
import jade.wrapper.StaleProxyException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class HomeGui extends Stage {
    Farmer farmer;
    FarmerAgent farmerAgent;
    ChoiceBox<String> plotsCB = new ChoiceBox<>();
    RButton addPlotB = new RButton("add.png",20,20,2);

    Label plotsLabel = new Label("Choisi une parcelle");
    Label typeLabel = new Label("Type de culture");
    Label areaLabel = new Label("Superficie en m²");
    Label dateLabele = new Label("Date de semis");
    Label qte_water_label = new Label("Quantité d'eau demandé");


    DatePicker s_datePK = new DatePicker();
    textField typeTF = new textField();
    NumberField areaNF = new NumberField();

    Label needs_label = new Label("Besoin on eau");
    Label profit_label = new Label("Calcul du profit");
    Label ren_label = new Label("Rendement");
    Button calculB = new Button("Calculer");

    TextField water_needs = new TextField();
    TextField profit = new TextField();
    TextField rendement = new TextField();

    NumberField qte_water = new NumberField();

    Button sendB = new Button("Envoyer");
    Button saveB = new Button("Enregistrer");

    RButton logout = new RButton("logout.png",30,30,5);
    Label farmerFname = new Label();
    Label farmerLname = new Label();

    public HomeGui(Farmer farmer){
        super();
        this.farmer = farmer;
        farmerFname.setText("Nom : "+farmer.getL_name());
        farmerLname.setText("Prenom : "+farmer.getF_name());

        VBox container = new VBox();

        HBox header = new HBox();
        HBox footer = new HBox();
        HBox waterB = new HBox();

        GridPane layout = new GridPane();
        layout.setHgap(20);
        layout.setVgap(20);
        layout.setPadding(new Insets(0,0,20,0));

        GridPane calculLayout = new GridPane();
        calculLayout.setHgap(20);
        calculLayout.setVgap(20);
        calculLayout.setPadding(new Insets(20));

        //choisi une parcelle
        header.setPadding(new Insets(20));
        header.setSpacing(20);
        header.getChildren().addAll(plotsLabel,plotsCB,addPlotB);
        plotsLabel.setPadding(new Insets(0,20,0,0));

        plotsCB.setTooltip(new Tooltip("Secelctionner une parcelle"));
        populatePlotCB();


        //get plot data from farmer;
        plotsCB.setOnAction(event->populateFields());

        //typeTF de culture
        layout.setConstraints(typeLabel,1,0);
        layout.setConstraints(typeTF,2,0);

        //superfice du parcelle
        layout.setConstraints(areaLabel,1,1);
        layout.setConstraints(areaNF,2,1);

        //date de semi
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        layout.setConstraints(dateLabele,1,2);
        layout.setConstraints(s_datePK,2,2);
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

        layout.getChildren().addAll(typeLabel, typeTF, areaLabel, areaNF, s_datePK,dateLabele);


        //calcul fields:
        calculLayout.setConstraints(needs_label,0,0);
        calculLayout.setConstraints(water_needs,1,0);
        calculLayout.setConstraints(profit_label,0,1);
        calculLayout.setConstraints(profit,1,1);
        calculLayout.setConstraints(ren_label,0,2);
        calculLayout.setConstraints(rendement,1,2);
        calculLayout.setConstraints(calculB,0,3,2,1, HPos.CENTER, VPos.TOP);
        calculLayout.getChildren().addAll(needs_label, water_needs, profit_label,profit, ren_label,rendement,calculB);
        calculLayout.setStyle("-fx-background-color:#90FEAA;");

        //set result fields uneditable
        water_needs.setEditable(false);
        profit.setEditable(false);
        rendement.setEditable(false);


        waterB.getChildren().addAll(qte_water_label,qte_water);
        waterB.setAlignment(Pos.CENTER);
        waterB.setSpacing(20);
        waterB.setPadding(new Insets(20));


        footer.getChildren().addAll(sendB,saveB);
        footer.setSpacing(30);
        footer.setAlignment(Pos.CENTER);

        calculB.setOnAction(event->{
            CalculProfit();
            CalculRendement();
            CalculWater();
        });

        saveB.setOnAction(event -> SavePlotChanges(event));

        //add Plot
        addPlotB.setOnAction(event->{
            new AddPlotGui(HomeGui.this,farmerAgent);

        });

        //real header
        HBox logoutBox = new HBox();
        logoutBox.setAlignment(Pos.CENTER);
        logoutBox.getChildren().addAll(farmerLname,farmerFname,logout);
        logoutBox.setPadding(Insets.EMPTY);
        logoutBox.setSpacing(100);

        logout.setOnAction(event->{
            GuiEvent guiEvent = new GuiEvent(this,3);
            farmerAgent.onGuiEvent(guiEvent);
        });

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(logoutBox,header,layout,calculLayout,waterB,footer);
        container.setPadding(new Insets(30));

        Scene scene = new Scene(container);

        this.setScene(scene);
        this.setTitle("Accueil");
        this.setMinWidth(430);
        this.setMinHeight(620);
    }

    private void SavePlotChanges(ActionEvent event) {
        GuiEvent guiEvent = new GuiEvent(event,1);
        if(isChanged() && !emptyField()){
            Plot plot = createPlot();
            guiEvent.addParameter(plot);
            farmerAgent.onGuiEvent(guiEvent);
            Vector<Plot> plots = farmer.getPlots();
            for(int i=0;i<plots.size() ; i++) {
                if(plots.get(i).getP_name().equals(plot.getP_name())){
                    farmer.getPlots().set(i,plot);
                    break;
                }
            }
        }
    }


    public void CalculWater(){
        this.water_needs.setText("2000");
    }
    public void CalculProfit(){
        this.profit.setText("4000");
    }
    public void CalculRendement(){
        this.rendement.setText("2000");
    }

   public void populatePlotCB(){
        Vector<Plot> plots = farmer.getPlots();
        String[] plotsNames;
        int size = plots.size();
        if(size>0){
            plotsNames = new String[size];
            for(int i=0;i<size;i++)
                plotsNames[i] = plots.get(i).getP_name();
        }else{
            plotsNames = new String[]{"Vide"};
        }
        plotsCB.getItems().clear();
        plotsCB.getItems().addAll(plotsNames);
        plotsCB.setValue(plotsNames[0]);
        populateFields();
    }

    private void populateFields() {
        String plotName = plotsCB.getValue();
        Plot plot = getPlot(plotName);
        if(plot!=null){
            typeTF.setText(plot.getType());
            areaNF.setText(""+plot.getArea());
            s_datePK.setValue(LocalDate.parse(plot.getS_date().toString()));
            qte_water.setText(plot.getWater_qte()+"");
        }
    }

    public Plot getPlot(String plotename){
        Vector<Plot> plots = farmer.getPlots();
        for(Plot plot : plots){
            if (plot.getP_name().equals(plotename))
                return plot;
        }
        return null;
    }

    public boolean isChanged(){
        Plot oldPlot = getPlot(plotsCB.getValue());
        if(oldPlot == null) {
            return true;
        }else{
            Plot newPlot = createPlot();
            if( newPlot.equals(oldPlot)) {
                return false;
            }else{
                return true;
            }
        }
    }

    private boolean emptyField() {
        return typeTF.isEmpty() || areaNF.isEmpty() || qte_water.isEmpty() || s_datePK.getValue() == null;
    }

    private Plot createPlot() {
        //Plot(Farmer farmer, String p_name, String type,Date s_date, float area, float water_qte)
        String p_name = plotsCB.getValue();
        String type = typeTF.getText();
        float area = Float.parseFloat(areaNF.getText());
        float water_qte = Float.parseFloat(qte_water.getText());

        if(s_datePK.getValue() != null){
            Date s_date = Date.valueOf(s_datePK.getValue());
            return new Plot(farmer,p_name,type,s_date,area,water_qte);
        }else{
            return null;
        }
    }

    public void setFarmerAgent(FarmerAgent farmerAgent) {
        this.farmerAgent = farmerAgent;
    }

    public void Logout(){
        try {
            LoginGui loginGui = new LoginGui();
            MainWindow.controller = MainWindow.container.createNewAgent(MainWindow.loginAgentName, LoginAgent.class.getName(),new Object[]{loginGui});
            MainWindow.controller.start();
            MainWindow.update(loginGui);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
