package MainPackage;

import Agents.DataBaseManager;
import Agents.SupervisorAgent;
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
            AgentController agentControllerDBA = agentContainer.createNewAgent(
                    Database.manager,
                    DataBaseManager.class.getName(),
                    null
            );
            AgentController agentControllerSupervisor = agentContainer.createNewAgent(
                    Database.Supervisor,
                    SupervisorAgent.class.getName(),
                    null
            );
            agentControllerDBA.start();
            agentControllerSupervisor.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
