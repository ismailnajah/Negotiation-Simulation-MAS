package com.example.android.distributeurdeau.models.Constents;

import com.example.android.distributeurdeau.models.Farmer;
import com.example.android.distributeurdeau.models.Plot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Queries {

    public static String AddFarmer(Farmer farmer) {
        String l_name = farmer.getL_name();
        String f_name = farmer.getF_name();
        String farmer_num = farmer.getFarmer_num();
        String password = farmer.getPassword();
        return "insert into " + Database.table_farmers + " values ('" + farmer_num + "','" + f_name + "','" + l_name + "','" + password + ")";
    }

    public static String DeleteProposedPlot(String p_name, String farmer_num) {
        return "Delete FROM " + Database.table_proposed_plots + " WHERE " + Database.p_name + tool(p_name) + " AND "
                + Database.farmer_num + tool(farmer_num);
    }

    public static String DeletePlot(String table, String p_name, String farmer_num) {
        return "DELETE FROM " + table + " WHERE " + Database.p_name + tool(p_name) + " AND "
                + Database.farmer_num + tool(farmer_num);
    }

    public static String UpdatePlotStatus(String p_name, String farmer_num, int status) {
        return "UPDATE " + Database.table_plots + " SET " + Database.plotStatus + " = " + status + " WHERE " +
                Database.farmer_num + tool(farmer_num) + " AND " + Database.p_name + tool(p_name);
    }

    public static String SendPlot(String p_name, String farmer_num, float water_qte) {
        return "UPDATE " + Database.table_plots + " SET " + Database.plotStatus + " = 1," +
                Database.water_qte + " = " + water_qte + " WHERE " +
                Database.farmer_num + tool(farmer_num) + " AND " + Database.p_name + tool(p_name);
    }

    public static String EditPlot(Plot plot) {
        final String date = tool(plot.getS_date().toString());
        final String newPlotValues = Database.type + tool(plot.getType())
                + " , " + Database.area + "=" + plot.getArea()
                + " , " + Database.water_qte + "=" + plot.getWater_qte()
                + " , " + Database.sowing_date + date;
        return "update " + Database.table_plots + " set " + newPlotValues +
                " where " + Database.p_name + tool(plot.getP_name())
                + " and " + Database.farmer_num + tool(plot.getFarmer().getFarmer_num());

    }

    public static PreparedStatement AddPlot(Plot plot, String tableName, Connection connection) throws SQLException {

        String query = "INSERT INTO " + tableName + " (" + Database.p_name + ", " + Database.farmer_num + "," + Database.type +
                "," + Database.area + "," + Database.sowing_date + "," + Database.water_qte + "," + Database.ET0 + "," + Database.PLUIE +
                "," + Database.Kc + "," + Database.Ym + "," + Database.Ky + ", `dotation`, `status`, `modified`) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, plot.getP_name());
        preparedStatement.setString(2, plot.getFarmer().getFarmer_num());
        preparedStatement.setString(3, plot.getType());
        preparedStatement.setFloat(4, plot.getArea());
        preparedStatement.setDate(5, plot.getS_date());
        preparedStatement.setFloat(6, plot.getWater_qte());
        preparedStatement.setFloat(7, plot.ET0);
        preparedStatement.setFloat(8, plot.PLUIE);
        preparedStatement.setFloat(9, plot.Kc);
        preparedStatement.setFloat(10, plot.Ym);
        preparedStatement.setFloat(11, plot.Ky);
        preparedStatement.setFloat(12, plot.dotation);
        preparedStatement.setInt(13, plot.getStatus());
        preparedStatement.setTimestamp(14, getCurrentTimeStamp());

        return preparedStatement;
    }

    public static String getPlot(String p_name, String farmer_num) {
        return "SELECT * FROM " + Database.table_plots + " WHERE " + Database.p_name + tool(p_name) + " AND " +
                Database.farmer_num + tool(farmer_num);
    }

    public static String getPlot(String p_name, String farmer_num) {
        return "SELECT * FROM " + Database.table_plots + " WHERE " + Database.p_name + tool(p_name) + " AND " +
                Database.farmer_num + tool(farmer_num);
    }

    private static String tool(String value) {
        return "='" + value + "'";
    }

    public static String getFarmer(String farmer_num) {
        return "SELECT * FROM " + Database.table_farmers + " WHERE " + Database.farmer_num + tool(farmer_num);
    }

    private static Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new Timestamp(today.getTime());

    }

    public static String getFarmer(String farmer_num) {
        return "SELECT * FROM " + Database.table_farmers + " WHERE " + Database.farmer_num + tool(farmer_num);
    }
}
