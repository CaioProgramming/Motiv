package com.creat.motiv.Beans;

public class Category {
    String name;
    String imageuri;

    public Category(String name, String imageuri) {
        this.name = name;
        this.imageuri = imageuri;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
