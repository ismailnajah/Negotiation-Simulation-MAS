package Agents;

import Gui.LoginGui;
import Gui.RegisterGui;
import com.example.android.distributeurdeau.models.Constents.Database;
import com.example.android.distributeurdeau.models.Constents.Onthologies;
import com.example.android.distributeurdeau.models.Farmer;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.application.Platform;

import java.io.IOException;

public class LoginAgent extends GuiAgent {
    LoginGui loginGui;
    RegisterGui register;
    Farmer farmer = null;


    @Override
    protected void setup() {
        //hna kant MainWindow
        loginGui = (LoginGui)getArguments()[0];
        loginGui.setAgent(this);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage response = receive();
                if(response != null){
                    switch (response.getPerformative()){
                        case ACLMessage.CONFIRM:
                            if(response.getOntology().equals(Onthologies.registration)){
                                register.close();
                            }else{
                                try {
                                    farmer = (Farmer)response.getContentObject();
                                    LoginAgent.this.doDelete();
                                } catch (UnreadableException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;

                        case  ACLMessage.FAILURE:
                            System.out.println("error");
                            break;
                        default:
                    }
                }else{
                    block();
                }
            }
        });
    }


    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        if(guiEvent.getType() == 1){
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addUserDefinedParameter(Database.farmer_num, guiEvent.getParameter(0).toString());
            request.addUserDefinedParameter(Database.password, guiEvent.getParameter(1).toString());
            request.addUserDefinedParameter(Database.is_farmer,String.valueOf(true));
            request.addReceiver(new AID(Database.manager,AID.ISLOCALNAME));
            request.setOntology(Onthologies.authentication);
            send(request);
        }else{
            Farmer farmer = (Farmer)guiEvent.getParameter(0);
            addUser(farmer);
        }
    }

    public void addUser(Farmer farmer){
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new AID(Database.manager,AID.ISLOCALNAME));
        try {
            request.setOntology(Onthologies.registration);
            request.setContentObject(farmer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(request);
    }

    @Override
    protected void takeDown() {
        if(farmer!=null)
            Platform.runLater(()-> loginGui.deploy(farmer));
        super.takeDown();
    }


    public RegisterGui getRegister() {
        return register;
    }

    public void setRegister(RegisterGui register) {
        this.register = register;
    }
}
