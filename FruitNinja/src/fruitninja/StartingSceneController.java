package fruitninja;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class StartingSceneController implements Initializable
{
    // Some general variables
    AudioClip backgroundSound, startSound;
    double startX, startY, endX, endY;
    boolean soundWorking = true;
    
    // Variables related to FXML file
    @FXML
    private Text txtStartTop;
    
    @FXML
    private Text txtStartBottom;
    
    @FXML
    private ImageView imgSound;
    
    @FXML
    private ImageView imgBlockSound;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ImageView imgBackground;
    
    @FXML
    private ImageView imgLogo;
    
    @FXML
    private Circle circle;
    
    @FXML
    private Rectangle restangle;
    
    @FXML
    private Text txtHighScore;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Create starting watermelon object
        Watermelon startingWM = new Watermelon(anchorPane);
        startingWM.imgFullWatermelon.setLayoutX(445);
        startingWM.imgFullWatermelon.setLayoutY(274);
        startingWM.imgFullWatermelon.setFitWidth(150);
        startingWM.imgFullWatermelon.setFitHeight(150);
        
        // Starting watermelon Rotation
        Timeline watermelonRotation = new Timeline(
                new KeyFrame(Duration.millis(10), e -> startingWM.imgFullWatermelon.setRotate(startingWM.imgFullWatermelon.getRotate() + 1))
        );
        
        watermelonRotation.setCycleCount(Timeline.INDEFINITE);
        watermelonRotation.play();
        
        // Start text fade in/out
        FadeTransition topTextFade = new FadeTransition(Duration.millis(500), txtStartTop);
        FadeTransition bottomTextFade = new FadeTransition(Duration.millis(500), txtStartBottom);
        
        topTextFade.setFromValue(1.0);
        bottomTextFade.setFromValue(1.0);
        
        topTextFade.setToValue(0.5);
        bottomTextFade.setToValue(0.5);
        
        topTextFade.setCycleCount(Timeline.INDEFINITE);
        bottomTextFade.setCycleCount(Timeline.INDEFINITE);
        
        topTextFade.setAutoReverse(true);
        bottomTextFade.setAutoReverse(true);
        
        topTextFade.play();
        bottomTextFade.play();
        
        // Play starting background sound
        backgroundSound = new AudioClip(this.getClass().getResource("/sounds/starting_background_music.mp3").toExternalForm());
        backgroundSound.setCycleCount(Timeline.INDEFINITE);
        backgroundSound.play();

        /* Register out events */
        
        // Start full drag on any of these
        imgBackground.setOnDragDetected(e -> imgBackground.startFullDrag());
        imgLogo.setOnDragDetected(e -> imgLogo.startFullDrag());
        circle.setOnDragDetected(e -> circle.startFullDrag());
        restangle.setOnDragDetected(e -> restangle.startFullDrag());
        txtHighScore.setOnDragDetected(e -> txtHighScore.startFullDrag());
        txtStartTop.setOnDragDetected(e -> txtStartTop.startFullDrag());
        txtStartBottom.setOnDragDetected(e -> txtStartBottom.startFullDrag());
        
        // Record the enter point
        startingWM.imgFullWatermelon.setOnMouseDragEntered(e -> 
            {
                startX = e.getX();
                startY = e.getY();
            }
        );
        
        // On mouse exit our watermelon cut it, start the game
        startingWM.imgFullWatermelon.setOnMouseDragExited(e -> 
            {
                // Get the end x, y this will be used in drawing the slash trace
                endX = e.getX();
                endY = e.getY();

                // Cut the watermelon
                startingWM.cutFruit(anchorPane);
                
                // Make slash trace
                startingWM.createSlashTrace(startX, startY, endX, endY, anchorPane);
                
                // Run the starting sound and load the new fxml file for game
                Timeline delay = new Timeline(
                        new KeyFrame(Duration.millis(500), eventDelay1 ->
                        {
                            // This is just delay before we open our new game scene
                            startSound = new AudioClip(this.getClass().getResource("/sounds/starting_sound.mp3").toExternalForm());
                            backgroundSound.stop();
                            startSound.play();
                            startSound.setCycleCount(1);
                        }
                    ),
                        new KeyFrame(Duration.seconds(1), eventDelay2 -> 
                        {
                            Parent root;
                            try
                            {
                               root = FXMLLoader.load(this.getClass().getResource("GameScene.fxml"));
                               Scene gameScene = new Scene(root);
                               FruitNinja.primaryStage.setScene(gameScene);
                            } 
                            catch (IOException ex) 
                            {
                                System.out.println("GameScene.fxml not found..");
                            }
                        }
                    )
                );
                
                delay.setCycleCount(1);
                delay.play();
            }
        );
    }
    
    @FXML
    private void onImgSoundPressed(MouseEvent event)
    {
        // Pause/Start background sound
        pausePlaySound(backgroundSound);
    }
    
    @FXML
    private void onImgBlockSoundPressed(MouseEvent event)
    {
        // Pause/Start background sound
        pausePlaySound(backgroundSound);
    }
    
    private void pausePlaySound(AudioClip backgroundSound)
    {
        if (soundWorking)
        {
            // Sound working? Then stop it and make block icon visible
            backgroundSound.stop();
            soundWorking = false;
            imgBlockSound.setVisible(true);
        }
        else
        {
            // Sound not working? Then play it and remove block icon
            backgroundSound.play();
            backgroundSound.setCycleCount(Timeline.INDEFINITE);
            imgBlockSound.setVisible(false);
            soundWorking = true;
        }
    }

    @FXML
    private void onHighScoreRectanglePressed(MouseEvent event)
    {
        openHighScoreScene();
    }

    @FXML
    private void onHighScoresTxtPressed(MouseEvent event)
    {
        openHighScoreScene();
    }
    
    public void openHighScoreScene()
    {
        // Make delay before opening the high score new fxml file
        Timeline delay = new Timeline(
                new KeyFrame(Duration.millis(20), eventDelay -> 
                {
                    try
                    {
                        Parent root = FXMLLoader.load(this.getClass().getResource("HighScoresScene.fxml"));
                        Scene highScoreScene = new Scene(root);
                        FruitNinja.primaryStage.setScene(highScoreScene);
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