package Agents;

import com.example.android.distributeurdeau.models.Database;
import com.example.android.distributeurdeau.models.Farmer;
import com.example.android.distributeurdeau.models.Onthologies;
import com.example.android.distributeurdeau.models.Plot;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.sql.*;
import java.sql.Date;
import java.util.Vector;

public class DataBaseManager extends Agent {
    Connection connect = null;
    Statement statement = null;
    ResultSet resultSet = null;
    @Override
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try {
                    connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestioneau?user=root&password=Password123");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    ACLMessage msg = receive();
                    if (msg != null && connect != null) {
                        ACLMessage res = msg.createReply();
                        Farmer farmer = null;
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            switch (msg.getOntology()) {
                                case (Onthologies.registration):
                                    res.setOntology(Onthologies.registration);
                                    farmer = (Farmer) msg.getContentObject();
                                    String callback = addFarmer(farmer);
                                    if (callback.equals(Database.error)) {
                                        res.setPerformative(ACLMessage.FAILURE);
                                    } else {
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    }
                                    break;

                                case (Onthologies.authentication):
                                    res.setOntology(Onthologies.authentication);
                                    String login = msg.getUserDefinedParameter(Database.farmer_num);
                                    String pass = msg.getUserDefinedParameter(Database.password);
                                    farmer = getFarmer(login, pass);
                                    if (farmer != null) {
                                        res.setPerformative(ACLMessage.CONFIRM);
                                        res.setContentObject(farmer);
                                    } else {
                                        res.setPerformative(ACLMessage.REFUSE);
                                    }
                                    break;

                                case (Onthologies.plot_modification):
                                    res.setOntology(Onthologies.plot_modification);
                                    Plot plot = (Plot)msg.getContentObject();
                                    boolean status = EditePlot(plot);
                                    if(status)
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    else
                                        res.setPerformative(ACLMessage.FAILURE);
                            }
                        }
                        send(res);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public Farmer getFarmer(String farmer_num, String password){
        final String query = "select * from "+Database.table_farmers+" where "+ Database.farmer_num +"='"+farmer_num +"'";
        Farmer farmer = null;
        try{
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                if (password.equals(resultSet.getString(Database.password))) {
                    String l_name = resultSet.getString(Database.l_name);
                    String f_name = resultSet.getString(Database.f_name);
                    farmer = new Farmer(farmer_num, f_name,l_name, password);
                    farmer.setPlots(getFarmerPlots(farmer));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmer;
    }

    private Vector<Plot> getFarmerPlots(Farmer farmer) {
        final String query = "select * from "+Database.table_plots+" where "+Database.farmer_num + "='"+farmer.getFarmer_num()+"'";
        Vector<Plot> plots = new Vector<>();
        try{
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String p_name = resultSet.getString(Database.p_name);
                float area = resultSet.getFloat(Database.area);
                float water_qte = resultSet.getFloat(Database.water_qte);
                Date s_date = resultSet.getDate(Database.sowing_date);
                String type = resultSet.getString(Database.type);
                plots.addElement(new Plot(farmer,p_name,type,s_date,area,water_qte));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plots;

    }

    public String addFarmer(Farmer farmer){
        String callback;
        String l_name = farmer.getL_name();
        String f_name = farmer.getF_name();
        String farmer_num = farmer.getFarmer_num();
        String password = farmer.getPassword();

        String query = "insert into "+ Database.table_farmers+" values ('"+farmer_num+"','"+f_name+"','"+l_name+"','"+password+")";

        try {
            connect.prepareStatement(query).executeUpdate();
            callback = Database.success;
        } catch (SQLException e) {
            callback = Database.error;
            e.printStackTrace();
        }

        return callback;
    }

    public boolean EditePlot(Plot plot){
        boolean status;
        final String date = tool(plot.getS_date().toString());
        final String newPlotValues = Database.type + tool(plot.getType())
                                    +" , "+Database.area+"="+plot.getArea()
                                    +" , "+Database.water_qte+"="+plot.getWater_qte()
                                    +" , "+Database.sowing_date+date;
        final String query = "update "+Database.table_plots+" set " +newPlotValues+
                            " where "+ Database.p_name+tool(plot.getP_name())
                            +" and " + Database.farmer_num+tool(plot.getFarmer().getFarmer_num());

        try {
            connect.prepareStatement(query).executeUpdate();
            status = true;
        } catch (SQLException e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

    private String tool(String value){
        return "='"+value+"'";
    }
}
