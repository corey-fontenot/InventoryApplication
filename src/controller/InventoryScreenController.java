/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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
        // load "Add Part" screen
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
            // make sure a part was selected
            if(partsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            // load modify part screen
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartScreen.fxml"));
            loader.load();

            ModifyPartScreenController modifyPartController = loader.getController();
            modifyPartController.setData(inventory, (Part) partsTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch(NullPointerException e) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Error");
            infoDialog.setContentText(e.getMessage());
            infoDialog.showAndWait();
        }
    }
    
    @FXML
    private void handleDeletePartButtonClick(ActionEvent event) {
        try {
            // make sure a part was selected
            if(partsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }

            // get selected part
            Part temp = (Part) partsTable.getSelectionModel().getSelectedItem();

            // ask user to confirm that they want to delete the part
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setContentText(
                "Are you sure you want to permanently delete \"" + temp.getName() + 
                 "\" from Inventory? This action cannot be undone");

            // result of confirm dialog
            Optional<ButtonType> result = confirmDialog.showAndWait();
            
            // if user clicked OK remove part from inventory
            if(result.isPresent() && result.get() == ButtonType.OK) {
                inventory.deletePart(temp);
            }
            
            // reset part table in case it was populated by a search operation
            partSearchField.setText("");
            partsTable.setItems(inventory.getAllParts());
            
        } catch(NullPointerException e) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Error");
            infoDialog.setContentText(e.getMessage());
            infoDialog.showAndWait();
        }
    }
    
    @FXML
    private void handleSearchPartButtonClick(ActionEvent event) {
        String searchText = partSearchField.getText();
        ObservableList<Part> results;
        
        try {
            // id lookup results
            results = inventory.lookupPart(Integer.parseInt(searchText));
            
            // name lookup results
            ObservableList<Part> nameResults = inventory.lookupPart(searchText);
            
           // if part from name search is not already in the result list, add it
            for(Part part : nameResults) {
                if(!results.contains(part)) {
                    results.add(part);
                }
            }
            
        } catch(NumberFormatException e) {
            results = inventory.lookupPart(searchText);
        }
        
        //Populate Parts table with the results
        partsTable.setItems(results);
    }
    
    @FXML
    private void handleAddProductButtonClick(ActionEvent event) throws IOException{
        // load "Add Product" screen
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
    private void handleModifyProductButtonClick(ActionEvent event) throws IOException {
        try {
            // make sure a product was selected
            if(productsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a product to do that!");
            } 
            
            // get selected product and send it and inventory instance to "Modify Part" screen
            Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductScreen.fxml"));
            loader.load();
            
            ModifyProductScreenController modProdController = loader.getController();
            modProdController.setData(inventory, selectedProduct);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            
            
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Product Selected");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleDeleteProductButtonClick(ActionEvent event) {
        try {
            // make sure a product was selected
            if(productsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a product to do that!");
            }
            
            Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
            
            // ask user to confirm delete operation
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setContentText(
                "Are you sure you want to permanently delete \"" + selectedProduct.getName() + 
                "\" from Inventory? This action cannot be undone");
            
            // confirm dialog result
            Optional<ButtonType> result = confirmDialog.showAndWait();
            
            // if user clicks OK delete the selected part
            if(result.isPresent() && result.get() == ButtonType.OK) {
                inventory.deleteProduct(selectedProduct);
            }
            
            // reset products table in case search operation was performed
            productsTable.setItems(inventory.getAllProducts());
            productSearchField.setText("");
            
        } catch(NullPointerException e) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Error");
            infoDialog.setContentText(e.getMessage());
            infoDialog.showAndWait();
        }
    }
    
    @FXML
    private void handleSearchProductButtonClick(ActionEvent event) {
        String searchText = productSearchField.getText();
        ObservableList<Product> results;
        
        try {
            // product id results
            results = inventory.lookupProduct(Integer.parseInt(searchText));
            
            // product name results
            ObservableList<Product> nameResults = inventory.lookupProduct(searchText);
            
            // if name lookup results are not already in results list, add them
            for(Product product : nameResults) {
                if(!results.contains(product)) {
                    results.add(product);
                }
            }
        } catch(NumberFormatException e) {
            results = inventory.lookupProduct(searchText);
        }
        
        //Populate Product table with the results
        productsTable.setItems(results);
    }
    
    /**
     * get data from previous screen
     * @param inventory inventory instance
     */
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
