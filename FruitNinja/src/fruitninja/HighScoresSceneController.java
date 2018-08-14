package fruitninja;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HighScoresSceneController implements Initializable
{
    // General variable
    AudioClip backgroundSound;
    
    // FXML Variables
    @FXML
    private ImageView imgBackMainmenu;
    
    @FXML
    private Text txtTop10Scores;
    
    @FXML
    private Rectangle rectangleTop10Scores;
    
    @FXML
    private Text txtScores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Run first a background sound - Same as gaming music
        backgroundSound = new AudioClip(this.getClass().getResource("/sounds/game_background_sound.mp3").toExternalForm());
        backgroundSound.setVolume(0.2);
        backgroundSound.setCycleCount(Timeline.INDEFINITE);
        backgroundSound.play();
        
        // Open the scores.txt
        try
        {
            // Open score file
            File scoresFile = new File("C:\\Scores.txt");
            Scanner scanScoreFile = new Scanner(scoresFile);
            
            // Create dynamic array, we don't know how much scores in our file
            ArrayList<Integer> scores = new ArrayList();
            
            // Loop and store all the scores in our array
            while(scanScoreFile.hasNext())
            {
                scores.add(scanScoreFile.nextInt());
            }
            
            // Now we ranking our scores array - Not efficient way, if file have many scores it will make lag
            for (int i = 0; i < scores.size(); i++)
            {
                for (int j = i + 1; j < scores.size(); j++)
                {
                    if (scores.get(i) < scores.get(j))
                    {
                        // Swap the two elements
                        int temp = scores.get(i);
                        scores.set(i, scores.get(j));
                        scores.set(j, temp);
                    }
                }
            }
            
            // define our string that will hold text that will be set to txtScores
            String formatedScores = ""; // Inialize it to nothing
           
            // The scores file have scores less than or equal to 10?
            if (scores.size() <= 10)
            {
                for (int i = 0; i < scores.size(); i++)
                {
                    formatedScores += Integer.toString(scores.get(i)) + "\n";
                }
            }
            else
            {
                // In case there is more than 10 scores, get only the 10 higher ones
                // We already ranked the arraylist then first 10 is the highest
                for (int i = 0; i < 10; i++)
                {
                    formatedScores += Integer.toString(scores.get(i)) + "\n";
                }
            }
            
            // Finally set our text to the txtScores
            txtScores.setText(formatedScores);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Scores.txt file not found..");
        }
        
        // Fade in/out
        FadeTransition top10Scorec = new FadeTransition(Duration.seconds(1), txtTop10Scores);
        
        top10Scorec.setFromValue(1.0);
        top10Scorec.setToValue(0.7);
        top10Scorec.setCycleCount(Timeline.INDEFINITE);
        top10Scorec.setAutoReverse(true);
        top10Scorec.play();
        
        // Make random colors every 10 seconds for High 10 Scores text
        Timeline randomColors = new Timeline(new KeyFrame(Duration.seconds(10), e ->
                {
                    txtTop10Scores.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                }));
        
        randomColors.setCycleCount(Timeline.INDEFINITE);
        randomColors.play();
    }

    @FXML
    private void onImgBackToMainMenuPressed(MouseEvent event)
    {
        // Make delay before backing to the start game fxml
        Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(20), eventDelay2 -> 
                {
                    try
                    {
                        Parent root = FXMLLoader.load(this.getClass().getResource("StartingScene.fxml"));
                        Scene startingScene = new Scene(root);
                        FruitNinja.primaryStage.setScene(startingScene);
                        backgroundSound.stop();
                    } 
                    catch (IOException ex)
                    {
                        System.out.println("HighScoresScene.fxml not found..");
                    }
                })
        );
        
        delay.setCycleCount(1);
        delay.play();
    }
}