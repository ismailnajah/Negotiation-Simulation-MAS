package com.example.android.distributeurdeau.models.Constents;

public class Database {
    // table names
    public static String table_farmers = "farmers";
    public static String table_plots = "plots";
    public static String table_proposed_plots = "proposed_plots";
    public static String table_negotiation_turns = "negotiation_turns";
    public static String table_culture_Data = "type_agriculture";
    public static String table_supervisor = "supervisors";

    // farmers columns
    public static String f_name = "first_name";
    public static String l_name = "last_name";
    public static String farmer_num = "farmer_num";
    public static String is_farmer = "is_farmer";
    public static String password = "password";

    // plots columns
    public static String p_name = "plot_name";
    public static String type = "type";
    public static String area = "area";
    public static String sowing_date = "s_date";
    public static String water_qte = "water_quantity";
    public static String ET0 = "ET0";
    public static String PLUIE = "PLUIE";
    public static String Kc = "Kc";
    public static String Ym = "Ym";
    public static String Ky = "Ky";

    //Culture data
    public static String type_name = "nom";
    public static String sowing_start = "sowing_start";
    public static String sowing_end = "sowing_end";
    public static String price = "prix";
    public static String variable_price = "prix_variable";
    public static String fixed_price = "prix_fix";

    // Supervisors colums
    public static String supervisorId = "id";


    public static String manager = "databaseManager";

    public static String error = "error";
    public static String success = "success";

    public static String plotStatus = "status";

    public static String dotation = "dotation";

    public static String isFarmerTurn = "isFarmerTurn";
}
