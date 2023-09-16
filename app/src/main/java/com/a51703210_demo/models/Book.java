package com.a51703210_demo.models;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String name;
    private String author;
    private int number;
    private String location;
    private Category category;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Book() {
    }

    public Book(String name, String author, int number, String location, Category category) {
        this.name = name;
        this.author = author;
        this.number = number;
        this.location = location;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", number=" + number +
                ", location='" + location + '\'' +
                ", category=" + category +
                '}';
    }
}
