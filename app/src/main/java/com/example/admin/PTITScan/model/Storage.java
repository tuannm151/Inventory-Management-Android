package com.example.admin.PTITScan.model;

public class Storage {
    String id;
    String name;
    String location;
    public Storage(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Storage(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Storage() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
