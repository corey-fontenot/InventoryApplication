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
import javafx.stage.Stage;
import model.Inventory;
import model.Part;

/**
 * FXML Controller class
 *
 * @author Corey
 */
public class AddProductScreenController implements Initializable {
    
    Inventory inventory;

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
    }

    @FXML
    private void handleAddBtnClick(ActionEvent event) throws IOException {
    }

    @FXML
    private void handleDeleteBtnClick(ActionEvent event) throws IOException {
    }

    @FXML
    private void handleSaveBtnClick(ActionEvent event) throws IOException {
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
    }
    
}
