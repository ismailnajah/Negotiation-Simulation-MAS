package Gui;

import Agents.LoginAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainWindow extends Application {
   public static AgentContainer container;
   public static AgentController controller;
   public static String loginAgentName = "loginAgent";

   static Stage window = new LoginGui();


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        startContainer();
        window.show();


    }

    public void startContainer() {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(false);
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        container = runtime.createAgentContainer(profile);
        try {
            controller = container.createNewAgent(loginAgentName,LoginAgent.class.getName(),new Object[]{window});
            controller.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void stop(){
        try {
            container.kill();
            super.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void update(Stage stage){
        Platform.runLater(()->{
            window.close();
            window = stage;
            window.show();
        });
    }

}
