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
public class Inventory {
    // fields
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;
    
    // constructor
    public Inventory() {
        this.allParts = FXCollections.observableArrayList();
        this.allProducts = FXCollections.observableArrayList();
    }
    
    /**
     *  Add part to allParts list
     *  @param part : part to add
     */
    public void addPart(Part part) {
        allParts.add(part);
    }
    
    /**
     *  Add product to allProducts list
     *  @param product : product to add
     */
    public void addProduct(Product product) {
        allProducts.add(product);
    }
    
    /**
     *  Lookup part in allParts list by ID
     *  @param partId: id to search for
     *  @return list of matched items. Empty list if none found
     */
    public ObservableList<Part> lookupPart(int partId) {
        ObservableList<Part> tempList = FXCollections.observableArrayList();
        
        allParts.stream().filter((part) -> (String.valueOf(part.getId()).contains(String.valueOf(partId)))).forEachOrdered((part) -> {
            tempList.add(part);
        });
        
        return tempList;
    }
    
    /**
     *  Lookup part in allParts list by ID
     *  @param partName: name to search for
     *  @return list of matched items. Empty list if none found
     */
    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> tempList = FXCollections.observableArrayList();
        
        allParts.stream().filter((part) -> (part.getName().contains(partName))).forEachOrdered((part) -> {
            tempList.add(part);
        });
        
        return tempList;
    }
    
    /**
     *  Lookup product in allProducts list by ID
     *  @param productId: id to search for
     *  @return list of matched items. Empty list if none found
     */
    public ObservableList<Product> lookupProduct(int productId) {
        ObservableList<Product> tempList = FXCollections.observableArrayList();
        
        allProducts.stream().filter((product) -> (String.valueOf(product.getId()).contains(String.valueOf(productId)))).forEachOrdered((product) -> {
            tempList.add(product);
        });
        
        return tempList;
        
    }
    
    /**
     *  Lookup product in allProducts list by name
     *  @param productName: name to search for
     *  @return list of matched items. Empty list if none found
     */
    public ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> tempList = FXCollections.observableArrayList();
        
        allProducts.stream().filter((product) -> (product.getName().contains(productName))).forEachOrdered((product) -> {
            tempList.add(product);
        });
        
        return tempList;
    }
    
    /**
     *  Return index of specified part
     * @param part: Part whose index is to be returned
     * @return index of specified part
     */
    public int indexOfPart(Part part) {
        if(allParts.contains(part)) {
            return allParts.indexOf(part);
        }
        return -1;
    }
    
    /**
     *  Update part at specified index in allParts list
     *  @param index: index of part to replace
     *  @param part: new part replacing old part in list
     */
    public void updatePart(int index, Part part) {
        if(index < allParts.size()) {
            allParts.set(index, part);
        } 
    }
    
    /**
     *  Returns index of specified product
     *  @param product product whose index will be returned
     *  @return index of specified product
     */
    public int indexOfProduct(Product product) {
        if(allProducts.contains(product)) {
            return allProducts.indexOf(product);
        }
        return -1;
    }
    
    /**
     *  Update product at specified index in allProducts list
     *  @param index: index of product to replace
     *  @param product: new product replacing old product in list
     */
    public void updateProduct(int index, Product product) {
        if(index < allProducts.size()) {
            allProducts.set(index, product);
        } 
    }
    
    /**
     *  Delete specified part from allParts list
     *  @param part: part to be removed
     */
    public void deletePart(Part part) {
        if(allParts.contains(part)) {
            allParts.remove(part);
        }
    }
    
    /**
     *  Delete specified product from allProducts list
     *  @param product: product to be removed
     */
    public void deleteProduct(Product product) {
        if(allProducts.contains(product)) {
            allProducts.remove(product);
        }
    }
    
    /**
     *  Getter for allParts
     *  @return list of all parts in inventory
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    /**
     *  Getter for allProducts
     *  @return list of all products in inventory
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
