package com.example.admin.PTITScan.model;

import com.example.admin.PTITScan.enums.ItemType;

public class Item {
    private String name;
    private ItemType type;
    private String price;
    private String barcode;

    private String desscription;

    private String imageUrl;


    public Item() {

    }

    public Item(String name, ItemType type, String price, String barcode, String desscription, String imageUrl) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.barcode = barcode;
        this.desscription = desscription;
        this.imageUrl = imageUrl
    }









}