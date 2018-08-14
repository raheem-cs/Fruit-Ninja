package fruitninja;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameEndPaneController implements Initializable
{
    @FXML
    private Text txtScore;
    
    @FXML
    private Text txtBestScore;
    
    @FXML
    private ImageView imgReplay;
    
    @FXML
    private ImageView imgBack;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Set score in the end pane
        txtScore.setText(GameSceneController.staticTxtScore.getText());
        
        // Get high score and compare it with his score
        int hisScore = Integer.parseInt(GameSceneController.staticTxtScore.getText());
        int highestScore = GameSceneController.highestScore;
        
        if (hisScore > highestScore)
        {
            txtBestScore.setText("Congratulation, You achieved best score: " + hisScore);
            txtBestScore.setLayoutX(33);
            txtBestScore.setLayoutY(79);
        }
        else
        {
            txtBestScore.setText("Best Score: " + highestScore);
            txtBestScore.setLayoutX(170);
            txtBestScore.setLayoutY(80);
        }
    }

    @FXML
    private void onImgReplayPressed(MouseEvent event)
    {
        // Add small delay, then open game scene again
        Timeline delayReplay = new Timeline(new KeyFrame(Duration.millis(20), e ->
            {
                try
                {
                    Parent root = FXMLLoader.load(this.getClass().getResource("GameScene.fxml"));
                    Scene gameScene = new Scene(root);
                    FruitNinja.primaryStage.setScene(gameScene);
                }
                catch (IOException ex)
                {
                    System.out.println("GameScene.fxml not found..");
                }
            })
        );
        
        delayReplay.play();
    }

    @FXML
    private void onImgBackPressed(MouseEvent event)
    {
        // Add small delay, then back to starting scene
        Timeline delayStartingScene = new Timeline(new KeyFrame(Duration.millis(20), e ->
            {
                try
                {
                    Parent root = FXMLLoader.load(this.getClass().getResource("StartingScene.fxml"));
                    Scene gameScene = new Scene(root);
                    FruitNinja.primaryStage.setScene(gameScene);
                }
                catch (IOException ex)
                {
                    System.out.println("StartingScene.fxml not found..");
                }
            })
        );
        
        delayStartingScene.play();
    }
}