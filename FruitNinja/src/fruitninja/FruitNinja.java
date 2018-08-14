/*
*    PC Fruit Ninja © by Raheem Elsayed, Ahmad Hassan
*    Coded for educational purposes
*
*    Created:  May-2018
*    Released: August-2018
*/

package fruitninja;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FruitNinja extends Application
{
    // Create a public data field so we can access stage and set new scene for it
    public static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception
    {
        // Load the starting scene fxml file at the start of the game
        Parent root = FXMLLoader.load(getClass().getResource("StartingScene.fxml"));
        
        // The starting scene
        Scene scene = new Scene(root);
        
        // Set the scene to the stage
        stage.setScene(scene);
        
        // Set title/icon for our stage
        stage.setTitle("Fruit Ninja");
        Image icon = new Image("images/stage_icon.png");
        stage.getIcons().add(icon);
        
        // Show our stage
        stage.show();
        
        // Store our stage refrence here
        primaryStage = stage;
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}