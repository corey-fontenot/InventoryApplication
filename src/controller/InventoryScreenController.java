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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private void handleAddPartButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddPartScreen.fxml"));
        loader.load();
        
        AddPartScreenController addPartController = loader.getController();
        addPartController.setData(inventory);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    private void handleModifyPartButtonClick(ActionEvent event) throws IOException{
        try {
            if(partsTable.getSelectionModel().isEmpty()) {
                throw new IllegalArgumentException("You must select a part to do that!");
            }
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartScreen.fxml"));
            loader.load();

            ModifyPartScreenController modifyPartController = loader.getController();
            modifyPartController.setData(inventory, (Part) partsTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch(IllegalArgumentException e) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Error");
            infoDialog.setContentText(e.getMessage());
            infoDialog.showAndWait();
        }
    }
    
    @FXML
    private void handleDeletePartButtonClick(ActionEvent event) {
        Part temp = (Part) partsTable.getSelectionModel().getSelectedItem();
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setContentText(
            "Are you sure you want to permanently delete \"" + temp.getName() + 
             "\" from Inventory? This action cannot be undone");
        confirmDialog.showAndWait();
        
        inventory.deletePart(temp);
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
    private void handleAddProductButtonClick(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddProductScreen.fxml"));
        loader.load();
        
        AddProductScreenController addProdController = loader.getController();
        addProdController.setData(inventory);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    private void handleModifyProductButtonClick(ActionEvent event) {
        System.out.println("Modify Product Button Clicked");
    }
    
    @FXML
    private void handleDeleteProductButtonClick(ActionEvent event) {
        
    }
    
    @FXML
    private void handleSearchProductButtonClick(ActionEvent event) {
        String searchText = productSearchField.getText();
        ObservableList<Product> results;
        
        try {
            results = inventory.lookupProduct(Integer.parseInt(searchText));
        } catch(NumberFormatException e) {
            results = inventory.lookupProduct(searchText);
        }
        
        //Populate Product table with the results
        productsTable.setItems(results);
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
