package com.example.admin.PTITScan.model;

public class Item {
    private String id;
    private String name;
    private String category;
    private String price;
    private String barcode;


    public Item() {

    }

    public Item(String name, String category, String price, String barcode) {

        this.name = name;
        this.category = category;
        this.price = price;
        this.barcode = barcode;
    }

    public Item(String id, String name, String category, String price, String barcode) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.barcode = barcode;
    }
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}