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
 * @author Corey Fontenot
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
            // id results
            results = inventory.lookupPart(Integer.parseInt(searchText));
            
            // name results
            ObservableList<Part> nameResults = inventory.lookupPart(searchText);
            
            /*
                If a result from the name lookup is not already in the results
                list from the id lookup then add it to the results list
            */
            for(Part part : nameResults) {
                if(!results.contains(part)) {
                    results.add(part);
                }
            }
        } catch(NumberFormatException e) {
            /*
                if search text does not contain only integers then search
                only based on name field
            */
            results = inventory.lookupPart(searchText);
        }
        
        // populate "All Parts" table with search results
        allPartsTable.setItems(results);
    }

    @FXML
    private void handleAddBtnClick(ActionEvent event) throws IOException {
        try {
            // check if "All Parts" table contains any items
            if(allPartsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            // add selected part to the "All Parts" table
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
            // check if a part was selected
            if(associatedPartsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            // remove selected part from the list
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            associatedParts.remove(selectedPart);
            
            // reset "All Parts" table in case table is populated with search results
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
            
            // make sure max is greater than min
            if(max < min) {
                throw new IncorrectValueException("Max cannot be less than Min");
            }
            
            // make sure stock is between min and max
            if(stock < min || stock > max) {
                throw new IncorrectValueException("Stock must be between Min and Max");
            }
            
            // make sure "Associated Parts" table has at least one part
            if(associatedPartsTable.getItems().isEmpty()) {
                throw new IncorrectValueException("A product must have at least one part");
            }
            
            // get total cost of items in "Associated Parts" table
            double totalCost = 0;
            for(Part part : associatedPartsTable.getItems()) {
                totalCost += part.getPrice();
            }
            
            // make sure cost of all parts is less than price of product
            if(price < totalCost) {
                throw new IncorrectValueException("Product price must be more than the cost of all associated parts");
            }
            
            // add all associated parts to the product instance
            Product addedProduct = new Product(id, name, price, stock, min, max);
            associatedPartsTable.getItems().forEach((part) -> {
                addedProduct.addAssociatedPart(part);
            });
            
            // add product instance to inventory
            inventory.addProduct(addedProduct);
            
            // alert user that product was saved
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Saved");
            alert.setContentText("Product \"" + name + "\" was successfully saved.");
            alert.showAndWait();
            
            // load inventory screen
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
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelBtnClick(ActionEvent event) throws IOException {
        // ask user to confirm that they want to cancel
        Alert  confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation");
        confirmDialog.setContentText(
            "Are you sure you want to Cancel? All progress will be lost");
        
        // get result of confirm dialog
        Optional<ButtonType> result = confirmDialog.showAndWait();
        
        // if user clicks OK, load inventory screen
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
    
    /**
     * get inventory instance and populate tables
     * @param inventory 
     */
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
    
    /**
     * calculate next auto generated product id
     * @return next product id
     */
    private int nextProductId() {
        int max = 1;
        
        // calculate highest product id in inventory
        for(Product product : inventory.getAllProducts()) {
            if(product.getId() > max) {
                max = product.getId();
            }
        }
        
        return max + 1;
    }
    
}
