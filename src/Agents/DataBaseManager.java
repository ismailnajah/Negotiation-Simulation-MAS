package Agents;

import com.example.android.distributeurdeau.models.*;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class DataBaseManager extends Agent {
    Connection connect = null;
    Statement statement = null;
    ResultSet resultSet = null;
    ACLMessage msg;
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
                    msg = receive();
                    if (msg != null && connect != null) {
                        ACLMessage res = msg.createReply();
                        Farmer farmer = null;
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            switch (msg.getOntology()) {
                                case (Onthologies.registration):
                                    // create new user account
                                    res.setOntology(Onthologies.registration);
                                    farmer = (Farmer) msg.getContentObject();
                                    if (addFarmer(farmer)) {
                                        res.setPerformative(ACLMessage.FAILURE);
                                    } else {
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    }
                                    break;

                                case (Onthologies.authentication):
                                    String login = msg.getUserDefinedParameter(Database.farmer_num);
                                    String pass = msg.getUserDefinedParameter(Database.password);
                                    boolean isFarmer = Boolean.valueOf(msg.getUserDefinedParameter(Database.is_farmer));
                                    res = getAccount(login,pass,isFarmer);
                                    break;

                                case (Onthologies.plot_modification):
                                    res.setOntology(Onthologies.plot_modification);
                                    Plot plot = (Plot)msg.getContentObject();
                                    if(EditePlot(plot))
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    else
                                        res.setPerformative(ACLMessage.FAILURE);
                                    break;

                                case (Onthologies.plot_addition):
                                    res.setOntology(Onthologies.plot_addition);
                                    plot = (Plot)msg.getContentObject();
                                    if(addPlot(plot))
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    else
                                        res.setPerformative(ACLMessage.FAILURE);
                                    break;

                                case (Onthologies.plot_removing):
                                    System.out.println("debug");
                                    res.setOntology(Onthologies.plot_removing);
                                    String pName = msg.getUserDefinedParameter(Database.p_name);
                                    String farmerNum = msg.getUserDefinedParameter(Database.farmer_num);
                                    if(removePlot(pName,farmerNum))
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    else
                                        res.setPerformative(ACLMessage.FAILURE);
                                    break;

                                case (Onthologies.plot_send):
                                    res.setOntology(Onthologies.plot_send);
                                    String p_name = msg.getUserDefinedParameter(Database.p_name);
                                    String farmer_num = msg.getUserDefinedParameter(Database.farmer_num);
                                    if(sendPlot(p_name,farmer_num))
                                        res.setPerformative(ACLMessage.CONFIRM);
                                    else
                                        res.setPerformative(ACLMessage.FAILURE);
                                    break;
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
                    farmer.setPlots(getFarmerPlots(farmer,""));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmer;
    }


    private Vector<Plot> getFarmerPlots(Farmer farmer,String sentOnly) {
        final String query = "select * from "+Database.table_plots+
                " where "+Database.farmer_num + tool(farmer.getFarmer_num())+" "+
                sentOnly+
                " ORDER BY modified DESC";
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
                Plot plot = new Plot(farmer,p_name,type,s_date,area,water_qte);
                plot.setStatus(resultSet.getInt(Database.plotStatus));
                plots.addElement(plot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plots;

    }


    public boolean addFarmer(Farmer farmer){
        String l_name = farmer.getL_name();
        String f_name = farmer.getF_name();
        String farmer_num = farmer.getFarmer_num();
        String password = farmer.getPassword();

        String query = "insert into "+ Database.table_farmers+ " values ('"+farmer_num+"','"+f_name+"','"+l_name+"','"+password+")";

        return executeUpdate(query);
    }


    public boolean EditePlot(Plot plot){
        final String date = tool(plot.getS_date().toString());
        final String newPlotValues = Database.type + tool(plot.getType())
                                    +" , "+Database.area+"="+plot.getArea()
                                    +" , "+Database.water_qte+"="+plot.getWater_qte()
                                    +" , "+Database.sowing_date+date;
        final String query = "update "+Database.table_plots+" set " +newPlotValues+
                            " where "+ Database.p_name+tool(plot.getP_name())
                            +" and " + Database.farmer_num+tool(plot.getFarmer().getFarmer_num());

        return executeUpdate(query);
    }

    private boolean addPlot(Plot plot) {
        final String p_name = tol(plot.getP_name());
        final String farmer_num = tol(plot.getFarmer().getFarmer_num());
        final String type = tol(plot.getType());
        final float area = plot.getArea();
        final String date = tol(plot.getS_date().toString());
        final float water_qte = plot.getWater_qte();
        final float pstatus = plot.getStatus();
        final String c = ",";

        String query = "INSERT into "+Database.table_plots+" VALUES ("+p_name + c + farmer_num + c + type + c +
                                            area + c + date + c + water_qte + c +pstatus+",now())";
        return executeUpdate(query);
    }

    private boolean removePlot(String p_name,String farmer_num) {
        String query = "DELETE FROM "+Database.table_plots+" WHERE "+ Database.p_name + tool(p_name) + " and "
                + Database.farmer_num + tool(farmer_num);
        System.out.println(query);
        return executeUpdate(query);
    }

    private boolean sendPlot(String p_name, String farmer_num) {
        String query = "UPDATE "+Database.table_plots+" SET "+ Database.plotStatus + " = 1 WHERE " +
                Database.farmer_num + tool(farmer_num) + " AND " + Database.p_name + tool(p_name)+";";
        return executeUpdate(query);
    }

    public ACLMessage getAccount(String login,String pass,boolean isFarmer){
        ACLMessage respons = msg.createReply();
        respons.setOntology(Onthologies.authentication);
        try {
            if(isFarmer){
                Farmer farmer = getFarmer(login,pass);
                if(farmer!=null){
                    respons.setContentObject(farmer);
                    respons.setPerformative(ACLMessage.CONFIRM);
                }else {
                    respons.setPerformative(ACLMessage.REFUSE);
                }
            }else{
                Supervisor supervisor = getSupervisor(login,pass);
                if(supervisor!=null){
                    respons.setContentObject(supervisor);
                    respons.setPerformative(ACLMessage.CONFIRM);
                }else{
                    respons.setPerformative(ACLMessage.REFUSE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        respons.addUserDefinedParameter(Database.is_farmer,String.valueOf(isFarmer));
        return respons;
    }

    private Supervisor getSupervisor(String login, String password) {
        final String query = "select * from "+Database.table_supervisor+" where "+ Database.supervisorId +"='"+login +"'";
        Supervisor supervisor = null;
        try{
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                if (password.equals(resultSet.getString(Database.password))) {
                    String l_name = resultSet.getString(Database.l_name);
                    String f_name = resultSet.getString(Database.f_name);
                    supervisor = new Supervisor(f_name,l_name, login,password);
                    supervisor.setFarmers(getSupervisorFarmers(supervisor));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supervisor;
    }

    private Vector<Farmer> getSupervisorFarmers(Supervisor supervisor) {
        final String query = "select * from "+Database.table_farmers;
        Vector<Farmer> farmes = new Vector<>();
        try{
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String fname = resultSet.getString(Database.f_name);
                String lname = resultSet.getString(Database.l_name);
                String farmer_num = resultSet.getString(Database.farmer_num);
                String pass = resultSet.getString(Database.password);
                farmes.addElement(new Farmer(farmer_num,fname,lname,pass));
            }
            String sentOnly ="AND "+Database.plotStatus +" IN (1,2) ";
            for(Farmer f : farmes){
                f.setPlots(getFarmerPlots(f,sentOnly));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return farmes;
    }

    private boolean executeUpdate(String query){
        boolean status;
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
    private String tol(String value){ return "'"+value+"'";}

}
