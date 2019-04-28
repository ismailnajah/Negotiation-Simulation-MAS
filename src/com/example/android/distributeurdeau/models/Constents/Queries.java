package com.example.android.distributeurdeau.models.Constents;

public class Queries {
    public static String p_name;
    public static String farmer_num;

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


    private static String tool(String value) {
        return "='" + value + "'";
    }

    private static String tol(String value) {
        return "'" + value + "'";
    }
}
