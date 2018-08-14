package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Orange extends Fruit
{
    // Data Fields - The orange image and the two halves
    public ImageView imgFullOrange;
    public ImageView imgHalfOrange1;
    public ImageView imgHalfOrange2;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    
    // Constructors
    public Orange(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullOrange = new ImageView("images/Orange.png");
        
        // Set any X, Y, and the orange dimensions
        this.imgFullOrange.setX(100);
        this.imgFullOrange.setY(100);
        this.imgFullOrange.setFitWidth(150);
        this.imgFullOrange.setFitHeight(100);
        
        // Place the orange on the pane
        anchorPane.getChildren().add(this.imgFullOrange);
    }
    
    public Orange(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the orange data from parametars above
        this.imgFullOrange = new ImageView(filePath);
        
        this.imgFullOrange.setX(x);
        this.imgFullOrange.setX(y);
        this.imgFullOrange.setFitWidth(150);
        this.imgFullOrange.setFitHeight(100);
        
        anchorPane.getChildren().add(this.imgFullOrange);
    }
    
    // Methods
    @Override
    public void cutFruit(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        imgHalfOrange1 = new ImageView("images/orangehalf.png");
        imgHalfOrange2 = new ImageView("images/orangehalf.png");
        
        // Fix them at a hard-coded position - They will start moving/rotating from it
        imgHalfOrange1.setRotate(-82.9);
        imgHalfOrange2.setRotate(97.1);

        imgHalfOrange1.setLayoutX(this.imgFullOrange.getLayoutX() - 38);
        imgHalfOrange1.setLayoutY(this.imgFullOrange.getLayoutY() + 8);
        
        imgHalfOrange2.setLayoutX(this.imgFullOrange.getLayoutX() + 21);
        imgHalfOrange2.setLayoutY(this.imgFullOrange.getLayoutY() + 11);
        
        imgHalfOrange1.setFitWidth(120);
        imgHalfOrange1.setFitHeight(100);
        
        imgHalfOrange2.setFitWidth(120);
        imgHalfOrange2.setFitHeight(100);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(imgHalfOrange1);
        anchorPane.getChildren().add(imgHalfOrange2);
        
        // Remove the orange and make the two halves of orange visible
        this.imgFullOrange.setVisible(false);
        imgHalfOrange1.setVisible(true);
        imgHalfOrange2.setVisible(true);
        
        // Play the cut sound
        this.playCutSound();
        
        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> 
        {
            setAnimation(this.imgHalfOrange1, this.imgHalfOrange2);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    
    @Override
    public void setAnimation(ImageView imgHalfOrange1, ImageView imgHalfOrange2)
    {
        // Rotate and move in X, Y
        imgHalfOrange1.setRotate(imgHalfOrange1.getRotate() + 1);
        imgHalfOrange2.setRotate(imgHalfOrange2.getRotate() + 1);
        
        imgHalfOrange1.setX(imgHalfOrange1.getX() + 1);
        imgHalfOrange1.setY(imgHalfOrange1.getY() + 1);
        
        imgHalfOrange2.setX(imgHalfOrange2.getX() - 1);
        imgHalfOrange2.setY(imgHalfOrange2.getY() + 1);
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
        
        // Tie the trace with our full orange layout x, y
        trace.setLayoutX(this.imgFullOrange.getLayoutX() - 33);
        trace.setLayoutY(this.imgFullOrange.getLayoutY() - 16);
        
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
        this.imgFullOrange.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Orange.centerX - this.radiusX && this.X < Orange.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Orange.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Orange.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Orange
            this.imgFullOrange.setLayoutX(this.X);
            this.imgFullOrange.setLayoutY(this.Y);
            this.imgFullOrange.setRotate(this.imgFullOrange.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}