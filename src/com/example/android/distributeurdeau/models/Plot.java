package com.example.android.distributeurdeau.models;



import jade.util.leap.Serializable;

import java.sql.Date;

public class Plot implements Serializable {
    private static final long serialVersionUID = 3L;

    private Farmer farmer;
    private String p_name;
    private float area;
    private float water_qte;
    private Date s_date;
    private String type;
    private int status;

    public Plot(Farmer farmer, String p_name, String type,Date s_date, float area, float water_qte) {
        this.p_name = p_name;
        this.farmer = farmer;
        this.type = type;
        this.area = area;
        this.s_date = s_date;
        this.water_qte = water_qte;
        this.status = 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getWater_qte() {
        return water_qte;
    }

    public void setWater_qte(float water_qte) {
        this.water_qte = water_qte;
    }

    public Date getS_date() {
        return s_date;
    }

    public void setS_date(Date s_date) {
        this.s_date = s_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Plot{" +
                "farmer=" + farmer.getFarmer_num() +
                ", p_name='" + p_name + '\'' +
                ", area=" + area +
                ", water_qte=" + water_qte +
                ", s_date=" + s_date +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plot)) return false;
        Plot plot = (Plot) o;
        return Float.compare(plot.area, area) == 0 &&
                Float.compare(plot.water_qte, water_qte) == 0 &&
                s_date.equals(plot.s_date) &&
                type.equals(plot.type);
    }

}
