package com.example.admin.PTITScan.services;

import com.example.admin.PTITScan.enums.ItemType;
import com.example.admin.PTITScan.model.Item;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ManageItemService {
    protected DatabaseReference mDatabase;

    public void addItemToDB(String name, String price, String barcode, ItemType type, String description, String imageUrl) {
        Item item = new Item(name, type, price, barcode, description, imageUrl);
        mDatabase.child("items").child(barcode).setValue(item);
    }
    public void deleteItemByBarcode(String barcode) {
        mDatabase.child("items").child(barcode).removeValue();
    }

    public void deleteItemById(String id) {
        mDatabase.child("items").child(id).removeValue();
    }

    public void updateItemById(String id, Item newItem) {
        mDatabase.child("items").child(id).setValue(newItem);
    }

    public void updateItemByBarcode(String barcode, Item newItem) {
        mDatabase.child("items").child(barcode).setValue(newItem);
    }
}
