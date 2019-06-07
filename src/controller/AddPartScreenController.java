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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Inventory;

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
    
    // Labels
    @FXML private Label machineLabel;
    @FXML private Label companyLabel;

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
    private void handleRadioButton(ActionEvent event) {
        if(inHouseRadio.isSelected()) {
            companyLabel.setVisible(false);
            machineLabel.setVisible(true);
        } else {
            machineLabel.setVisible(false);
            companyLabel.setVisible(true);
        }
    }
    
}
