/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryapplication;

import controller.InventoryScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 *
 * @author Corey
 */
public class InventoryApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inventory = loadSampleData();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/InventoryScreen.fxml"));
        loader.load();
        
        InventoryScreenController InvController = loader.getController();
        InvController.setData(inventory);
        
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.setTitle("Inventory Management System");
        stage.show();
    }
    
    private Inventory loadSampleData() {
        Inventory temp = new Inventory();
        
        temp.addPart(new InHouse(1, "Part 1", 4.99, 25, 10, 50, 12));
        temp.addPart(new InHouse(2, "Part 2", 6.99, 17, 15, 45, 19));
        temp.addPart(new Outsourced(3, "Part 3", 5.49, 27, 15, 60, "Company 1"));
        temp.addPart(new InHouse(4, "Part 4", 3.98, 57, 50, 100, 27));
        temp.addPart(new Outsourced(5, "Part 5", 4.99, 15, 10, 45, "Company 2"));
        temp.addPart(new Outsourced(6, "Part 6", 3.99, 17, 5, 20, "Company 1"));
        temp.addPart(new Outsourced(11, "Part 11", 7.49, 15, 10, 25, "Company 2"));
        
        Product prod1 = new Product(1, "Product 1", 14.99, 8, 5, 15);
        prod1.addAssociatedPart(temp.getAllParts().get(3));
        
        Product prod2 = new Product(2, "Product 2", 19.99, 23, 10, 25);
        prod2.addAssociatedPart(temp.getAllParts().get(6));
        
        Product prod3 = new Product(3, "Product 3", 27.99, 7, 5, 20);
        prod3.addAssociatedPart(temp.getAllParts().get(6));
        
        temp.addProduct(prod1);
        temp.addProduct(prod2);
        temp.addProduct(prod3);
        
        return temp;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
