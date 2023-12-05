package com.example.admin.PTITScan.model;

import java.util.Date;

public class StorageItem {
    private String id;
    private String itemReference;
    private String storageReference;
    private int quantity;

    public StorageItem() {

    }

    public StorageItem(String itemReference, String storageReference, int quantity) {
        this.itemReference = itemReference;
        this.storageReference = storageReference;
        this.quantity = quantity;
    }

    public StorageItem(String id, String itemReference, String storageReference, int quantity) {
        this.id = id;
        this.itemReference = itemReference;
        this.storageReference = storageReference;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemReference() {
        return itemReference;
    }

    public void setItemReference(String itemReference) {
        this.itemReference = itemReference;
    }

    public String getStorageReference() {
        return storageReference;
    }

    public void setStorageReference(String storageReference) {
        this.storageReference = storageReference;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
