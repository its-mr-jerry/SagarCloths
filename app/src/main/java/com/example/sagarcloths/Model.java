package com.example.sagarcloths;


public class Model {
    private String Name, Purl;
    private long Price;

    public Model(){}

    public Model(String Name, String Purl, int Price) {
        this.Name = Name;
        this.Purl = Purl;
        this.Price = Price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPurl() {
        return Purl;
    }

    public void setPurl(String purl) {
        Purl = purl;
    }

    public long getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }



}