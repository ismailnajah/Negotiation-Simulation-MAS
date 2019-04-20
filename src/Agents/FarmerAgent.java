package Agents;

import Gui.HomeGui;
import com.example.android.distributeurdeau.models.Database;
import com.example.android.distributeurdeau.models.Farmer;
import com.example.android.distributeurdeau.models.Onthologies;
import com.example.android.distributeurdeau.models.Plot;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.application.Platform;

import java.io.IOException;


public class FarmerAgent extends GuiAgent {
    private Farmer farmer;
    private HomeGui homeGui;

    @Override
    protected void setup() {
        farmer = (Farmer) getArguments()[0];
        homeGui = (HomeGui) getArguments()[1];
        homeGui.setFarmerAgent(this);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){

                }else{
                    block();
                }
            }
        });

    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        //type = 1 Save plot changes
        switch(guiEvent.getType()){
            case 1:
                SavePlot((Plot)guiEvent.getParameter(0));
                break;

            case 2:
                //add new Plot;
                addPlot((Plot)guiEvent.getParameter(0));
                break;
            case 3:
                FarmerAgent.this.doDelete();
                break;
            default:
                //sent to gestionnaire;
        }
    }

    private void addPlot(Plot plot) {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setOntology(Onthologies.plot_addition);
        request.addReceiver(new AID(Database.manager, AID.ISLOCALNAME));
        try {
            request.setContentObject(plot);
            send(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SavePlot(Plot plot) {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setOntology(Onthologies.plot_modification);
        request.addReceiver(new AID(Database.manager, AID.ISLOCALNAME));
        try {
            request.setContentObject(plot);
            send(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        Platform.runLater(()->homeGui.Logout());
        super.takeDown();
    }

}
