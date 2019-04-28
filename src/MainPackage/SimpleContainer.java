package MainPackage;

import Agents.DataBaseManager;
import com.example.android.distributeurdeau.models.Constents.Database;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class SimpleContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            ProfileImpl profile = new ProfileImpl(false);
            profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            AgentContainer agentContainer = runtime.createAgentContainer(profile);
            AgentController agentController = agentContainer.createNewAgent(
                    Database.manager,
                    DataBaseManager.class.getName(),
                    null
            );
            agentController.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
