/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import exceptions.IncorrectValueException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author Corey
 */
public class AddProductScreenController implements Initializable {
    
    Inventory inventory;
    ObservableList<Part> associatedParts;

    // Labels
    @FXML private Label productIDLabel;
    @FXML private Label productNameLabel;
    @FXML private Label productStockLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productMaxLabel;
    
    // Text Fields
    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField productStockField;
    @FXML private TextField productPriceField;
    @FXML private TextField productMinField;
    @FXML private TextField productMaxField;
    @FXML private TextField partSearchField;
    
    // Buttons
    @FXML private Button partSearchButton;
    @FXML private Button addPartButton;
    @FXML private Button saveProductButton;
    @FXML private Button cancelButton;
    
    // All Parts Table and Columns
    @FXML private TableView<Part> allPartsTable;
    @FXML private TableColumn<Part, Integer> allPartsIdCol;
    @FXML private TableColumn<Part, String> allPartsNameCol;
    @FXML private TableColumn<Part, Integer> allPartsStockCol;
    @FXML private TableColumn<Part, Double> allPartsPriceCol;
    
    // Associated Parts Table and Columns
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, Integer> associatedPartIdCol;
    @FXML private TableColumn<Part, String> associatedPartNameCol;
    @FXML private TableColumn<Part, Integer> associatedPartStockCol;
    @FXML private TableColumn<Part, Double> associatedPartPriceCol;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleSearchBtnClick(ActionEvent event) throws IOException {
        String searchText = partSearchField.getText();
        ObservableList<Part> results;
        
        try {
            results = inventory.lookupPart(Integer.parseInt(searchText));
            ObservableList<Part> nameResults = inventory.lookupPart(searchText);
            
            for(Part part : nameResults) {
                if(!results.contains(part)) {
                    results.add(part);
                }
            }
        } catch(NumberFormatException e) {
            results = inventory.lookupPart(searchText);
        }
        
        allPartsTable.setItems(results);
    }

    @FXML
    private void handleAddBtnClick(ActionEvent event) throws IOException {
        try {
            if(allPartsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
            associatedParts.add(selectedPart);
            
        } catch(NullPointerException e) {
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("No part selected");
            infoDialog.setContentText(e.getMessage());
            infoDialog.showAndWait();
        }
    }

    @FXML
    private void handleDeleteBtnClick(ActionEvent event) throws IOException {
        try {
            if(associatedPartsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            
            associatedParts.remove(selectedPart);
            
            allPartsTable.setItems(inventory.getAllParts());
            
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSaveBtnClick(ActionEvent event) throws IOException {
        int id, stock, min, max;
        String name;
        double price;
        
        try {
            id = nextProductId();
            name = productNameField.getText();
            price = Double.parseDouble(productPriceField.getText());
            stock = Integer.parseInt(productStockField.getText());
            min = Integer.parseInt(productMinField.getText());
            max = Integer.parseInt(productMaxField.getText());
            
            if(max < min) {
                throw new IncorrectValueException("Max cannot be less than Min");
            }
            
            if(stock < min || stock > max) {
                throw new IncorrectValueException("Stock must be between Min and Max");
            }
            
            if(associatedParts.isEmpty()) {
                throw new IncorrectValueException("Product must have at least one associated part");
            }
            
            Product addedProduct = new Product(id, name, price, stock, min, max);
            
            associatedPartsTable.getItems().forEach((part) -> {
                addedProduct.addAssociatedPart(part);
            });
            
            inventory.addProduct(addedProduct);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Saved");
            alert.setContentText("Product \"" + name + "\" was successfully saved.");
            alert.showAndWait();
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/InventoryScreen.fxml"));
            loader.load();
            
            InventoryScreenController InventoryController = loader.getController();
            InventoryController.setData(inventory);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            
        } catch(NumberFormatException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must enter valid values in all fields");
            alert.showAndWait();
        } catch(IncorrectValueException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
        }
    }

    @FXML
    private void handleCancelBtnClick(ActionEvent event) throws IOException {
        Alert  confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation");
        confirmDialog.setContentText(
            "Are you sure you want to Cancel? All progress will be lost");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/InventoryScreen.fxml"));
            loader.load();
            
            InventoryScreenController inventoryController = loader.getController();
            inventoryController.setData(inventory);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    public void setData(Inventory inventory) {
        this.inventory = inventory;
        
        this.associatedParts = FXCollections.observableArrayList();
        
        // Set up "All Parts" table
        allPartsTable.setItems(inventory.getAllParts());
        allPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Set up "Associated Parts" table
        associatedPartsTable.setItems(this.associatedParts);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    private int nextProductId() {
        int max = 1;
        
        for(Product product : inventory.getAllProducts()) {
            if(product.getId() > max) {
                max = product.getId();
            }
        }
        
        return max + 1;
    }
    
}
