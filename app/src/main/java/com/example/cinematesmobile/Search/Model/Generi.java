package com.example.cinematesmobile.Search.Model;

public class Generi {
    private int id;
    private String name;

    public Generi() {
    }

    public Generi(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
