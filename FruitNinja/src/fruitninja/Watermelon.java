package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Watermelon extends Fruit
{
    // Data Fields - The watermelon image and the two halves
    public ImageView imgFullWatermelon;
    public ImageView imgHalfWatermelon1;
    public ImageView imgHalfWatermelon2;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y; 
    
    // Constructors
    public Watermelon(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullWatermelon = new ImageView("images/Watermelon.png");
        
        // Set any X, Y, and the watermelon dimensions
        //this.imgFullWatermelon.setX(0);
        //this.imgFullWatermelon.setY(0);
        this.imgFullWatermelon.setFitWidth(150);
        this.imgFullWatermelon.setFitHeight(100);
        
        // Place the watermelon on the pane
        anchorPane.getChildren().add(this.imgFullWatermelon);
    }

    public Watermelon(String filePath, double x, double y, double fitWidth, double fitHeight, AnchorPane anchorPane)
    {
        // Set the watermelon data from parametars above
        this.imgFullWatermelon = new ImageView(filePath);
        
        this.imgFullWatermelon.setLayoutX(x);
        this.imgFullWatermelon.setLayoutY(y);
        this.imgFullWatermelon.setFitWidth(fitWidth);
        this.imgFullWatermelon.setFitHeight(fitHeight);
        
        anchorPane.getChildren().add(this.imgFullWatermelon);
    }
    
    // Methods
    @Override
    public void cutFruit(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        imgHalfWatermelon1 = new ImageView("images/halfwatermelon.png");
        imgHalfWatermelon2 = new ImageView("images/halfwatermelon.png");
        
        // Fix them at a hard-coded position - They will start moving/rotating from it
        imgHalfWatermelon1.setRotate(-81.3);
        imgHalfWatermelon2.setRotate(90);

        imgHalfWatermelon1.setLayoutX(this.imgFullWatermelon.getLayoutX() + 44);
        imgHalfWatermelon1.setLayoutY(this.imgFullWatermelon.getLayoutY() + 9);
        
        imgHalfWatermelon2.setLayoutX(this.imgFullWatermelon.getLayoutX() - 26);
        imgHalfWatermelon2.setLayoutY(this.imgFullWatermelon.getLayoutY() + 32);
        
        imgHalfWatermelon1.setFitWidth(90);
        imgHalfWatermelon1.setFitHeight(83);
        
        imgHalfWatermelon2.setFitWidth(90);
        imgHalfWatermelon2.setFitHeight(83);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(imgHalfWatermelon1);
        anchorPane.getChildren().add(imgHalfWatermelon2);
        
        // Remove the watermelon and make the two halves of watermelon visible
        this.imgFullWatermelon.setVisible(false);
        imgHalfWatermelon1.setVisible(true);
        imgHalfWatermelon2.setVisible(true);
        
        // Play the cut sound
        this.playCutSound();
        
        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> 
        {
            setAnimation(this.imgHalfWatermelon1, this.imgHalfWatermelon2);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    
    @Override
    public void setAnimation(ImageView imgHalfWatermelon1, ImageView imgHalfWatermelon2)
    {
        // Rotate and move in X, Y
        imgHalfWatermelon1.setRotate(imgHalfWatermelon1.getRotate() + 1);
        imgHalfWatermelon2.setRotate(imgHalfWatermelon2.getRotate() + 1);
        
        imgHalfWatermelon1.setX(imgHalfWatermelon1.getX() + 1);
        imgHalfWatermelon1.setY(imgHalfWatermelon1.getY() + 1);
        
        imgHalfWatermelon2.setX(imgHalfWatermelon2.getX() - 1);
        imgHalfWatermelon2.setY(imgHalfWatermelon2.getY() + 1);
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
        cutSounds[randomSound].setCycleCount(1); // Not needed as by default it's repeated 1 time only
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
        
        // Tie the trace with our full watermelon layout x, y
        trace.setLayoutX(this.imgFullWatermelon.getLayoutX());
        trace.setLayoutY(this.imgFullWatermelon.getLayoutY());
        
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
        this.imgFullWatermelon.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Watermelon.centerX - this.radiusX && this.X < Watermelon.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Watermelon.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Watermelon.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Watermelon
            this.imgFullWatermelon.setLayoutX(this.X);
            this.imgFullWatermelon.setLayoutY(this.Y);
            this.imgFullWatermelon.setRotate(this.imgFullWatermelon.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}