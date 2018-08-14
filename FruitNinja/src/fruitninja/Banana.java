package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Banana extends Fruit
{
    // Data Fields - The banana image and the two halves
    public ImageView imgFullBanana;
    public ImageView imgHalfBanana1;
    public ImageView imgHalfBanana2;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    
    // Static data field to be used in pause purpose
    public static Timeline bnaAnimation;
    
    // Constructors
    public Banana(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullBanana = new ImageView("images/Banana.png");
        
        // Set any X, Y, and the banana dimensions
        this.imgFullBanana.setX(100);
        this.imgFullBanana.setY(100);
        this.imgFullBanana.setFitWidth(150);
        this.imgFullBanana.setFitHeight(100);

        // Place the banana on the pane
        anchorPane.getChildren().add(this.imgFullBanana);
    }
    
    public Banana(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the banana data from parametars above
        this.imgFullBanana = new ImageView(filePath);
        
        this.imgFullBanana.setX(x);
        this.imgFullBanana.setX(y);
        this.imgFullBanana.setFitWidth(150);
        this.imgFullBanana.setFitHeight(100);
        
        anchorPane.getChildren().add(this.imgFullBanana);
    }
    
    // Methods
    @Override
    public void cutFruit(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        imgHalfBanana1 = new ImageView("images/halfbanana.png");
        imgHalfBanana2 = new ImageView("images/halfbanana.png");
        
        // Fix them at a hard-coded position - They will start moving/rotating from it
        imgHalfBanana1.setRotate(-167.5);
        imgHalfBanana2.setRotate(33.7);

        imgHalfBanana1.setLayoutX(this.imgFullBanana.getLayoutX() + 20);
        imgHalfBanana1.setLayoutY(this.imgFullBanana.getLayoutY() - 16);
        
        imgHalfBanana2.setLayoutX(this.imgFullBanana.getLayoutX() - 14);
        imgHalfBanana2.setLayoutY(this.imgFullBanana.getLayoutY() + 27);
        
        imgHalfBanana1.setFitWidth(100);
        imgHalfBanana1.setFitHeight(150);
        
        imgHalfBanana2.setFitWidth(100);
        imgHalfBanana2.setFitHeight(150);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(imgHalfBanana1);
        anchorPane.getChildren().add(imgHalfBanana2);
        
        // Remove the banana and make the two halves of banana visible
        this.imgFullBanana.setVisible(false);
        imgHalfBanana1.setVisible(true);
        imgHalfBanana2.setVisible(true);
        
        // Play the cut sound
        this.playCutSound();
        
        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> 
        {
            setAnimation(this.imgHalfBanana1, this.imgHalfBanana2);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    
    @Override
    public void setAnimation(ImageView imgHalfBanana1, ImageView imgHalfBanana2)
    {
        // Rotate and move in X, Y
        imgHalfBanana1.setRotate(imgHalfBanana1.getRotate() + 1);
        imgHalfBanana2.setRotate(imgHalfBanana2.getRotate() + 1);
        
        imgHalfBanana1.setX(imgHalfBanana1.getX() + 1);
        imgHalfBanana1.setY(imgHalfBanana1.getY() + 1);
        
        imgHalfBanana2.setX(imgHalfBanana2.getX() - 1);
        imgHalfBanana2.setY(imgHalfBanana2.getY() + 1);
    }
    
    @Override
    public void playCutSound()
    {
        // Create array of aduio clip 4 sounds
        AudioClip cutSounds[] = new AudioClip[4];
        
        // Sounds urls
        String sounds[] = 
        {
            "/sounds/cut_fruit1.mp3",
            "/sounds/cut_fruit2.mp3",
            "/sounds/cut_fruit3.mp3",
            "/sounds/cut_fruit4.mp3"
        };
        
        // Create objects
        for (int i = 0; i < cutSounds.length; i++)
        {
            cutSounds[i] = new AudioClip(this.getClass().getResource(sounds[i]).toExternalForm());
        }
        
        // Create random number from 0 to 3
        int randomSound = (int) (4 * Math.random());
        
        // Play random sound
        cutSounds[randomSound].setCycleCount(1);
        cutSounds[randomSound].play();
    }
    
    @Override
    public void createSlashTrace(double startX, double startY, double endX, double endY, AnchorPane pane)
    {
        // Get slash slope
        double slope = (endY - startY) / (endX - startX);
        
        // Get angle of incline with +ve X axis
        double angle = Math.toDegrees(Math.atan(slope));
        
        // Define the slash image and create the object for it
        ImageView trace = new ImageView("images/slash_trace.gif");
        
        // Set rotate angle we calculated, 45 hard-coded value
        trace.setRotate(45 - angle);
        
        // Firstly make the slash invisible
        trace.setVisible(false);
        
        // Tie the trace with our full banana layout x, y
        trace.setLayoutX(this.imgFullBanana.getLayoutX() - 37);
        trace.setLayoutY(this.imgFullBanana.getLayoutY());
        
        // Add the trace to the pane
        pane.getChildren().add(trace);
        
        // Make animation to show the trace just for 300 millisecond
        Timeline showSlash = new Timeline(
                new KeyFrame(Duration.ZERO, e ->
                {
                    trace.setVisible(true);
                }
            ),
                new KeyFrame(Duration.millis(300), e-> 
                {
                    trace.setVisible(false);
                }
            )
        );
        
        showSlash.setCycleCount(1);
        showSlash.play();
    }
    
    /* Function to move fruit */
    @Override
    public void appearFruit(AnchorPane pane)
    {
        // Random number have range this good in game physics
        this.radiusX = ((pane.getPrefWidth() / 2) - 100) * ThreadLocalRandom.current().nextDouble(0.3, 1.0);
        this.radiusY = (pane.getPrefHeight() - 136) * ThreadLocalRandom.current().nextDouble(0.4, 1.0);
        
        this.X = pane.getPrefWidth()/2 - this.radiusX; // Starting point for fruit
        this.imgFullBanana.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Banana.centerX - this.radiusX && this.X < Banana.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Banana.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Banana.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Banana
            this.imgFullBanana.setLayoutX(this.X);
            this.imgFullBanana.setLayoutY(this.Y);
            this.imgFullBanana.setRotate(this.imgFullBanana.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
        bnaAnimation = makeAnimation;
    }
}