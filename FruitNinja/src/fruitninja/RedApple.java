package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class RedApple extends Fruit
{
    // Data Fields - The RedApple image and the two halves
    public ImageView imgFullRedApple;
    public ImageView imgHalfRedApple1;
    public ImageView imgHalfRedApple2;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    
    // Constructors
    public RedApple(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullRedApple = new ImageView("images/Red_Apple.png");
        
        // Set any X, Y, and the orange dimensions
        this.imgFullRedApple.setX(100);
        this.imgFullRedApple.setY(100);
        this.imgFullRedApple.setFitWidth(83);
        this.imgFullRedApple.setFitHeight(83);
        
        // Place the orange on the pane
        anchorPane.getChildren().add(this.imgFullRedApple);
    }
    
    public RedApple(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the orange data from parametars above
        this.imgFullRedApple = new ImageView(filePath);
        
        this.imgFullRedApple.setX(x);
        this.imgFullRedApple.setX(y);
        this.imgFullRedApple.setFitWidth(83);
        this.imgFullRedApple.setFitHeight(83);
        
        anchorPane.getChildren().add(this.imgFullRedApple);
    }
    
    // Methods
    @Override
    public void cutFruit(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        this.imgHalfRedApple1 = new ImageView("images/apple_half1.png");
        this.imgHalfRedApple2 = new ImageView("images/apple_half2.png");
        
        // Fix them at a hard-coded position - They will start moving/rotating from it
        this.imgHalfRedApple1.setLayoutX(this.imgFullRedApple.getLayoutX() - 5);
        this.imgHalfRedApple1.setLayoutY(this.imgFullRedApple.getLayoutY() + 20);
        
        this.imgHalfRedApple2.setLayoutX(this.imgFullRedApple.getLayoutX() - 7);
        this.imgHalfRedApple2.setLayoutY(this.imgFullRedApple.getLayoutY() - 19);
        
        this.imgHalfRedApple1.setFitWidth(96);
        this.imgHalfRedApple1.setFitHeight(83);
        
        this.imgHalfRedApple2.setFitWidth(96);
        this.imgHalfRedApple2.setFitHeight(83);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(this.imgHalfRedApple1);
        anchorPane.getChildren().add(this.imgHalfRedApple2);
        
        // Remove the red apple and make the two halves of red apple visible
        this.imgFullRedApple.setVisible(false);
        this.imgHalfRedApple1.setVisible(true);
        this.imgHalfRedApple2.setVisible(true);
        
        // Play the cut sound
        this.playCutSound();
        
        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> 
        {
            setAnimation(this.imgHalfRedApple1, this.imgHalfRedApple2);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    
    @Override
    public void setAnimation(ImageView imgHalfOrange1, ImageView imgHalfOrange2)
    {
        // Rotate and move in X, Y
        this.imgHalfRedApple1.setRotate(imgHalfRedApple1.getRotate() + 1);
        this.imgHalfRedApple2.setRotate(imgHalfRedApple2.getRotate() + 1);
        
        this.imgHalfRedApple1.setX(imgHalfRedApple1.getX() + 1);
        this.imgHalfRedApple1.setY(imgHalfRedApple1.getY() + 1);
        
        this.imgHalfRedApple2.setX(imgHalfRedApple2.getX() - 1);
        this.imgHalfRedApple2.setY(imgHalfRedApple2.getY() + 1);
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
        
        // Tie the trace with our full red-apple layout x, y
        trace.setLayoutX(this.imgFullRedApple.getLayoutX() - 27);
        trace.setLayoutY(this.imgFullRedApple.getLayoutY() - 27);
        
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
        this.imgFullRedApple.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= RedApple.centerX - this.radiusX && this.X < RedApple.centerX + this.radiusX)
            {
                this.X++;
                this.Y = RedApple.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - RedApple.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Kiwi
            this.imgFullRedApple.setLayoutX(this.X);
            this.imgFullRedApple.setLayoutY(this.Y);
            this.imgFullRedApple.setRotate(this.imgFullRedApple.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}