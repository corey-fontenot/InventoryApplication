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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author Corey
 */
public class AddPartScreenController implements Initializable {
    
    private Inventory inventory;

    // Radio Buttons
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private ToggleGroup productionToggle;
    
    // Buttons
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    
    // Labels
    @FXML private Label machineLabel;
    @FXML private Label companyLabel;
    
    // Text Fields
    @FXML private TextField partIdField;
    @FXML private TextField partNameField;
    @FXML private TextField partStockField;
    @FXML private TextField partPriceField;
    @FXML private TextField partMaxField;
    @FXML private TextField partMinField;
    @FXML private TextField partProductionField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setData(Inventory inventory) {
        this.inventory = inventory;
    }
    
    private int nextPartId() {
        int max = 0;
        
        for(Part part : inventory.getAllParts()) {
            if(part.getId() > max) {
                max = part.getId();
            }
        }
        
        return max + 1;
    }

    @FXML
    private void handleCancelButtonClick(ActionEvent event) throws IOException{
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
    
    @FXML
    private void handleSaveBtnClick(ActionEvent event) throws IOException {
        int id, stock, min, max, machineId;
        double price;
        String name;
        String companyName;
        
        try {
            id = nextPartId();
            stock = Integer.valueOf(partStockField.getText());
            min = Integer.valueOf(partMinField.getText());
            max = Integer.valueOf(partMaxField.getText());
            price = Double.valueOf(partPriceField.getText());
            name = partNameField.getText();
            
            if(max < min) {
                throw new NumberFormatException("Max cannot be less than Min");
            }
            if(stock < min | stock > max) {
                throw new NumberFormatException("Stock must be between Min and Max");
            }
            
            if(inHouseRadio.isSelected()) {
                machineId = Integer.valueOf(partProductionField.getText());
                
                inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
            }else {
                companyName = partProductionField.getText();
                inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            }
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/InventoryScreen.fxml"));
            loader.load();
            
            InventoryScreenController inventoryController = loader.getController();
            inventoryController.setData(inventory);
            
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Part Saved");
            infoDialog.setContentText(
                name + " was successfully saved");
            infoDialog.showAndWait();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.setResizable(false);
            stage.show();
        } catch(NumberFormatException | NullPointerException e) {
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Error");
            errorDialog.setContentText(
                "You must enter valid values in all fields");
            errorDialog.showAndWait();
        }
    }
    
    @FXML
    private void handleRadioButton(ActionEvent event) {
        if(inHouseRadio.isSelected()) {
            companyLabel.setVisible(false);
            machineLabel.setVisible(true);
            partProductionField.setPromptText("Machine ID");
        } else {
            machineLabel.setVisible(false);
            companyLabel.setVisible(true);
            partProductionField.setPromptText("Company Name");
        }
    }
    
}
