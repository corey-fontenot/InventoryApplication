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
 * @author Corey Fontenot
 */
public class ModifyPartScreenController implements Initializable {
    
    private Inventory inventory;
    private Part part;

    // Radio Buttons and Toggle Groups
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private ToggleGroup productionToggle;
    
    // Labels
    @FXML private Label machineLabel;
    @FXML private Label companyLabel;
    
    // Text Fields
    @FXML private TextField partIdField;
    @FXML private TextField partNameField;
    @FXML private TextField partStockField;
    @FXML private TextField partPriceField;
    @FXML private TextField partMaxField;
    @FXML private TextField partProductionField;
    @FXML private TextField partMinField;
    
    // Buttons
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setData(Inventory inventory, Part part) {
        this.inventory = inventory;
        this.part = part;
        
        partIdField.setText(Integer.toString(part.getId()));
        partNameField.setText(part.getName());
        partStockField.setText(Integer.toString(part.getStock()));
        partPriceField.setText(Double.toString(part.getPrice()));
        partMaxField.setText(Integer.toString(part.getMax()));
        partMinField.setText(Integer.toString(part.getMin()));
        
        if(part instanceof InHouse) {
            partProductionField.setText(Integer.toString(((InHouse) part).getMachineId()));
            inHouseRadio.setSelected(true);
        } else {
            partProductionField.setText(((Outsourced) part).getCompanyName());
            outsourcedRadio.setSelected(true);
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

    @FXML
    private void handleSaveBtnClick(ActionEvent event) throws IOException {
        int id, stock, min, max, machineId;
        double price;
        String name, companyName;
        
        try {
            id = Integer.parseInt(partIdField.getText());
            name = partNameField.getText();
            stock = Integer.parseInt(partStockField.getText());
            price = Double.parseDouble(partPriceField.getText());
            min = Integer.parseInt(partMinField.getText());
            max = Integer.parseInt(partMaxField.getText());
            
            if(max < min) {
                throw new IncorrectValueException("Max cannot be less than Min");
            }
            
            if(stock < min || stock > max) {
                throw new IncorrectValueException("Stock must be between Min and Max");
            }
            
            if(inHouseRadio.isSelected()) {
                machineId = Integer.parseInt(partProductionField.getText());
                InHouse updatedPart = new InHouse(id, name, price, stock, min, max, machineId);
                if(inventory.indexOfPart(this.part) >= 0) {
                    inventory.updatePart(inventory.indexOfPart(this.part), updatedPart);
                }
            } else {
                companyName = partProductionField.getText();
                Outsourced updatedPart = new Outsourced(id, name, price, stock, min, max, companyName);
                if(inventory.indexOfPart(this.part) >= 0) {
                    inventory.updatePart(inventory.indexOfPart(this.part), updatedPart);
                }
            }
            
            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("Part Saved");
            infoDialog.setContentText(
                "\"" + name + "\" was successfully saved");
            infoDialog.showAndWait();
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/InventoryScreen.fxml"));
            loader.load();
            
            InventoryScreenController inventoryController = loader.getController();
            inventoryController.setData(inventory);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            
        } catch(NumberFormatException | NullPointerException e) {
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Error");
            errorDialog.setContentText(
                "You must enter valid values in all fields");
            errorDialog.showAndWait();
        } catch(IncorrectValueException e) {
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("Error");
            errorDialog.setContentText(e.getMessage());
            errorDialog.showAndWait();
        }
    }

    @FXML
    private void handleCancelButtonClick(ActionEvent event) throws IOException {
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
}
