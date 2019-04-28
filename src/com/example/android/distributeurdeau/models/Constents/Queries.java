package com.example.android.distributeurdeau.models.Constents;

import com.example.android.distributeurdeau.models.Farmer;
import com.example.android.distributeurdeau.models.Plot;

public class Queries {
    public static String p_name;
    public static String farmer_num;

    public static String AddFarmer(Farmer farmer) {
        String l_name = farmer.getL_name();
        String f_name = farmer.getF_name();
        String farmer_num = farmer.getFarmer_num();
        String password = farmer.getPassword();
        return "insert into " + Database.table_farmers + " values ('" + farmer_num + "','" + f_name + "','" + l_name + "','" + password + ")";
    }

    public static String DeleteProposedPlot() {
        return "Delete FROM " + Database.table_proposed_plots + " WHERE " + Database.p_name + tool(p_name) + " AND "
                + Database.farmer_num + tool(farmer_num);
    }

    public static String DeletePlot() {
        return "DELETE FROM " + Database.table_plots + " WHERE " + Database.p_name + tool(p_name) + " AND "
                + Database.farmer_num + tool(farmer_num);
    }

    public static String UpdatePlotStatus(int status) {
        return "UPDATE " + Database.table_plots + " SET " + Database.plotStatus + " = " + status + " WHERE " +
                Database.farmer_num + tool(farmer_num) + " AND " + Database.p_name + tool(p_name);
    }

    public static String SendPlot(float water_qte) {
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


    public static String AddPlot(Plot plot, String tableName) {
        final String p_name = tol(plot.getP_name());
        final String farmer_num = tol(plot.getFarmer().getFarmer_num());
        final String type = tol(plot.getType());
        final float area = plot.getArea();
        final String date = tol(plot.getS_date().toString());
        final float water_qte = plot.getWater_qte();
        final String c = ",";

        return "INSERT into " + tableName + " VALUES (" + p_name + c + farmer_num + c + type + c +
                area + c + date + c + water_qte + c + plot.getET0() +
                c + plot.getPLUIE() + c + plot.getKc() + c + plot.getYm() + c + plot.getKy() + c +
                "default,default)";
    }

    private static String tool(String value) {
        return "='" + value + "'";
    }

    private static String tol(String value) {
        return "'" + value + "'";
    }
}
