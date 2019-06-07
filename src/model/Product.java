/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Corey Fontenot
 */
public class Product {
    // fields
    private ObservableList<Part> associatedParts;
    private int id, stock, min, max;
    private String name;
    private double price;
    
    // constructor
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }
    
    // setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setstock(int stock) {
        this.stock = stock;
    }
    
    public void setMin(int min) {
        this.min = min;
    }
    
    public void setMax(int max) {
        this.max = max;
    }
    
    // getters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public int getMin() {
        return min;
    }
    
    public int getMax() {
        return max;
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
    
    // add part to associated parts
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    // remove part from associated parts
    public void deleteAssociatedPart(Part part) {
        if(associatedParts.contains(part)) {
            associatedParts.remove(part);
        }
    }
}
