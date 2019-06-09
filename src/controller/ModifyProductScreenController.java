/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    }

    @FXML
    private void handleAddBtnClick(ActionEvent event) {
    }

    @FXML
    private void handleDeleteBtnClick(ActionEvent event) {
    }

    @FXML
    private void handleSaveBtnClick(ActionEvent event) {
    }

    @FXML
    private void handleCancelBtnClick(ActionEvent event) {
    }
    
    public void setData(Inventory inventory, Product product) {
        this.inventory = inventory;
        this.selectedProduct = product;
    }
    
}
