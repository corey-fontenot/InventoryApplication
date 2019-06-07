/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 *
 * @author Corey
 */
public class InventoryScreenController implements Initializable {
    
    private Inventory inventory;
    
    // Buttons
    @FXML private Button exitButton;
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;
    @FXML private Button searchPartButton;
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;
    @FXML private Button searchProductButton;
    
    // Text fields
    @FXML private TextField partSearchField;
    @FXML private TextField productSearchField;
    
    // Parts TableView and Columns
    @FXML private TableView partsTable;
    @FXML private TableColumn<Part, Integer> partIdCol;
    @FXML private TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partStockCol;
    @FXML private TableColumn<Part, Double> partPriceCol;
    
    // Products TableView and Columns
    @FXML private TableView productsTable;
    @FXML private TableColumn<Product, Integer> productIdCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Integer> productStockCol;
    @FXML private TableColumn<Product, Double> productPriceCol;
    
    // Button handlers
    @FXML
    private void handleExitButtonClick(ActionEvent event) throws IOException {
        Platform.exit();
    }
    
    @FXML
    private void handleAddPartButtonClick(ActionEvent event) {
        System.out.println("Add Part Button Clicked");
    }
    
    @FXML
    private void handleModifyPartButtonClick(ActionEvent event) {
        System.out.println("Modify Part Button Clicked");
    }
    
    @FXML
    private void handleDeletePartButtonClick(ActionEvent event) {
        System.out.println("Delete Part Button Clicked");
    }
    
    @FXML
    private void handleSearchPartButtonClick(ActionEvent event) {
        String searchText = partSearchField.getText();
        ObservableList<Part> results;
        
        try {
            results = inventory.lookupPart(Integer.parseInt(searchText));
        } catch(NumberFormatException e) {
            results = inventory.lookupPart(searchText);
        }
        
        //Populate Parts table with the results
        partsTable.setItems(results);
    }
    
    @FXML
    private void handleAddProductButtonClick(ActionEvent event) {
        System.out.println("Add Product Button Clicked");
    }
    
    @FXML
    private void handleModifyProductButtonClick(ActionEvent event) {
        System.out.println("Modify Product Button Clicked");
    }
    
    @FXML
    private void handleDeleteProductButtonClick(ActionEvent event) {
        System.out.println("Delete Product Button Clicked");
    }
    
    @FXML
    private void handleSearchProductButtonClick(ActionEvent event) {
        System.out.println("Search Product Button Clicked");
    }
    
    public void setData(Inventory inventory) {
        this.inventory = inventory;
        
        // Populate Parts Table
        partsTable.setItems(inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Populate Products Table
        productsTable.setItems(inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
