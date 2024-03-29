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
public class ModifyProductScreenController implements Initializable {
    private Inventory inventory;
    private Product selectedProduct;
    
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
    @FXML private TextField productMaxField;
    @FXML private TextField productMinField;
    @FXML private TextField partSearchField;
    
    // Buttons
    @FXML private Button partSearchButton;
    @FXML private Button addPartButton;
    @FXML private Button saveProductButton;
    @FXML private Button cancelButton;
    @FXML private Button deletePartBtn;
    
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
    private void handleSearchBtnClick(ActionEvent event) {
        String searchText = partSearchField.getText();
        ObservableList<Part> result;
        try {
            // result of part id lookup
            result = inventory.lookupPart(Integer.parseInt(searchText));
            
            // result of part name lookup
            ObservableList<Part> nameResults = inventory.lookupPart(searchText);
            
            // if part name result already in results list, add it
            for(Part part : nameResults) {
                if(!result.contains(part)) {
                    result.add(part);
                }
            }
            
        } catch(NumberFormatException e) {
            result = inventory.lookupPart(searchText);
        } 
        
        // populate "All Parts" table with search result
        allPartsTable.setItems(result);
    }

    @FXML
    private void handleAddBtnClick(ActionEvent event) {
        try {
            // makes sure a part was selected
            if(allPartsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            // get selected part and add it to the "Associated Parts" table
            Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
            selectedProduct.addAssociatedPart(selectedPart);
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No part selected");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteBtnClick(ActionEvent event) {
        try {
            // make sure a part was selected
            if(associatedPartsTable.getSelectionModel().isEmpty()) {
                throw new NullPointerException("You must select a part to do that!");
            }
            
            // get selected part and remove it from the "Associated Parts" table
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            selectedProduct.deleteAssociatedPart(selectedPart);
            
            associatedPartsTable.setItems(selectedProduct.getAllAssociatedParts());
            
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No part selected");
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
            id = Integer.parseInt(productIdField.getText());
            stock = Integer.parseInt(productStockField.getText());
            min = Integer.parseInt(productMinField.getText());
            max = Integer.parseInt(productMaxField.getText());
            name = productNameField.getText();
            price = Double.parseDouble(productPriceField.getText());
            
            // make sure max is greater than min
            if(max < min) {
                throw new IncorrectValueException("Max cannot be less than Min");
            }
            
            // make sure stock is between min and max
            if(stock < min || stock > max) {
                throw new IncorrectValueException("Stock must be between Min and Max");
            }
            
            // make sure product has at least one associated part
            if(associatedPartsTable.getItems().isEmpty()) {
                throw new IncorrectValueException("Product must have at least one part");
            }
            
            // calculat total cost of associated parts
            double partsSum = 0;
            for(Part part : associatedPartsTable.getItems()) {
                partsSum += part.getPrice();
            }
            
            // make sure product price is more than cost of associated parts
            if(price < partsSum) {
                throw new IncorrectValueException("Product price must be more than cost of all parts");
            }
            
            // create product instance and add associated parts to it
            Product updatedProduct = new Product(id, name, price, stock, min, max);
            for(Part part : associatedPartsTable.getItems()) {
                updatedProduct.addAssociatedPart(part);
            }
            
            // update invntory with modified product
            inventory.updateProduct(inventory.indexOfProduct(this.selectedProduct), updatedProduct);
            
            // alert user that product was updated
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Updated");
            alert.setContentText("Product \"" + name + "\" was successfully saved");
            alert.showAndWait();
            
            // load inventory screen
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/InventoryScreen.fxml"));
            loader.load();
            
            InventoryScreenController invController = loader.getController();
            invController.setData(inventory);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            
            
        } catch(NullPointerException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must enter valid values in all fields.");
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
        
        // result of confirm dialog
        Optional<ButtonType> result = confirmDialog.showAndWait();
        
        // if user clicked OK, load inventory screen
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
     * load data from previous screen
     * @param inventory inventory instance
     * @param product selected part from previous screen
     */
    public void setData(Inventory inventory, Product product) {
        this.inventory = inventory;
        this.selectedProduct = product;
        
        // Populate All Parts Table
        allPartsTable.setItems(inventory.getAllParts());
        allPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Populate Associated Parts Table
        associatedPartsTable.setItems(selectedProduct.getAllAssociatedParts());
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // populate text fields
        productIdField.setText(Integer.toString(selectedProduct.getId()));
        productNameField.setText(selectedProduct.getName());
        productStockField.setText(Integer.toString(selectedProduct.getStock()));
        productMinField.setText(Integer.toString(selectedProduct.getMin()));
        productMaxField.setText(Integer.toString(selectedProduct.getMax()));
        productPriceField.setText(Double.toString(selectedProduct.getPrice()));
    }
    
}
