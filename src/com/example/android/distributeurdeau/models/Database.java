package com.example.android.distributeurdeau.models;

public class Database {
    // table names
    public static String table_farmers = "farmers";
    public static String table_plots = "plots";
    public static String table_plotData = "plots_data";
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

    // Supervisors colums
    public static String supervisorId = "id";


    public static String manager = "databaseManager";

    public static String error = "error";
    public static String success = "success";

}
