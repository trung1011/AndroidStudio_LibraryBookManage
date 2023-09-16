package com.a51703210_demo.models;

import java.io.Serializable;

public class Profession implements Serializable {
    private int id;
    private String name;

    public Profession(){

    }

    public Profession(int id, String name){
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

    @Override
    public String toString() {
        return name;
    }
}
