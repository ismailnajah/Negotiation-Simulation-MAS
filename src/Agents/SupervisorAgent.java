package Agents;

import com.example.android.distributeurdeau.models.Constents.Database;
import com.example.android.distributeurdeau.models.Constents.Onthologies;
import com.example.android.distributeurdeau.models.Constents.Templates;
import com.example.android.distributeurdeau.models.CultureData;
import com.example.android.distributeurdeau.models.Estimation;
import com.example.android.distributeurdeau.models.Plot;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Vector;

public class SupervisorAgent extends Agent {
    Vector<CultureData> cultureData;


    @Override
    protected void setup() {
        getCultureData();
        addBehaviour(new CultureDataBehaviour());
        addBehaviour(new AnalyseBehaviour());


    }

    private void analyse(Plot plot) {
        Estimation estimation = new Estimation(cultureData);
        float besoin = (float) Math.floor(plot.getWater_qte());
        float dotation = (float) Math.floor(plot.getDotation());
        float estimated = (float) Math.floor((estimation.estimateBesoin(plot) / 0.007) * plot.getArea());

        if (besoin == estimated) {
            if (besoin > dotation) {
                plot = tweek(plot);
            } else {
                accept(plot.getP_name(), plot.getFarmer().getFarmer_num());
                return;
            }
        } else if (besoin > estimated) {
            if (estimated > dotation) {
                plot = tweek(plot);
            } else {
                plot.setWater_qte(estimated);
            }

        } else {
            if (besoin > dotation) {
                plot = tweek(plot);
            } else {
                if (estimated > dotation) {
                    plot = tweek(plot);
                } else {
                    plot.setWater_qte(estimated);
                }
            }
        }

        proposePlot(plot);
    }

    private Plot tweek(Plot plot) {
        Plot proposedPlot;
        float dotation = (float) Math.floor(plot.getDotation());
        float newArea = (float) Math.sqrt(dotation / (plot.Kc * plot.ET0 - plot.PLUIE) * 0.007f);
        proposedPlot = new Plot(plot);
        proposedPlot.setArea(newArea);
        proposedPlot.setWater_qte(plot.getDotation());
        // TODO : Date de semi
        return proposedPlot;
    }

    private void proposePlot(Plot plot) {
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.addReceiver(new AID(Database.manager, AID.ISLOCALNAME));
        message.setOntology(Onthologies.propose_plot);
        try {
            message.setContentObject(plot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(message);
    }

    public void accept(String plotName, String farmerNum) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID(Database.manager, AID.ISLOCALNAME));
        message.setOntology(Onthologies.ACCEPT_PLAN);
        message.addUserDefinedParameter(Database.p_name, plotName);
        message.addUserDefinedParameter(Database.farmer_num, farmerNum);

        notifyFarmer(plotName, farmerNum);
        send(message);
    }

    private void notifyFarmer(String p_name, String farmer_num) {
        ACLMessage notify = new ACLMessage(ACLMessage.CONFIRM);
        notify.addReceiver(new AID(Onthologies.FARMER_PREFIX + farmer_num
                , AID.ISLOCALNAME));
        notify.setOntology(Onthologies.ACCEPT_PLAN);
        notify.addUserDefinedParameter(Database.p_name, p_name);
        send(notify);

    }

    private void getCultureData() {
        // Get culture data
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setOntology(Onthologies.culture_data);
        message.addReceiver(new AID(Database.manager, AID.ISLOCALNAME));
        send(message);
    }

    private class CultureDataBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage message = receive(Templates.CULTURE_DATA);
            if (message != null) {
                if (message.getPerformative() == ACLMessage.CONFIRM) {
                    try {
                        cultureData = (Vector<CultureData>) message.getContentObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                block();
            }
        }
    }

    private class AnalyseBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive(Templates.ANALYSE);
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    try {
                        Plot plot = (Plot) msg.getContentObject();
                        analyse(plot);
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                block();
            }
        }

    }
}
