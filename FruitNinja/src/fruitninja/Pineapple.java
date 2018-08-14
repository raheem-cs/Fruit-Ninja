package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Pineapple extends Fruit
{
    // Data Fields - The pineapple image and the two halves
    public ImageView imgFullPineapple;
    public ImageView imgHalfPineapple1;
    public ImageView imgHalfPineapple2;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;

    // Constructors
    public Pineapple(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullPineapple = new ImageView("images/Pineapple.png");
        
        // Set any X, Y, and the pineapple dimensions
        this.imgFullPineapple.setX(100);
        this.imgFullPineapple.setY(100);
        this.imgFullPineapple.setFitWidth(200);
        this.imgFullPineapple.setFitHeight(150);
        
        // Place the pineapple on the pane
        anchorPane.getChildren().add(this.imgFullPineapple);
    }
    
    public Pineapple(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the pineapple data from parametars above
        this.imgFullPineapple = new ImageView(filePath);
        
        this.imgFullPineapple.setX(x);
        this.imgFullPineapple.setX(y);
        this.imgFullPineapple.setFitWidth(200);
        this.imgFullPineapple.setFitHeight(150);
        
        anchorPane.getChildren().add(this.imgFullPineapple);
    }
    
    // Methods
    @Override
    public void cutFruit(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        imgHalfPineapple1 = new ImageView("images/PineappleHalf1.png");
        imgHalfPineapple2 = new ImageView("images/PineappleHalf2.png");
        
        // Fix them at a hard-coded position - They will start moving/rotating from it
        imgHalfPineapple1.setRotate(24.8);
        imgHalfPineapple2.setRotate(31);

        imgHalfPineapple1.setLayoutX(this.imgFullPineapple.getLayoutX() - 13);
        imgHalfPineapple1.setLayoutY(this.imgFullPineapple.getLayoutY() + 96);
        
        imgHalfPineapple2.setLayoutX(this.imgFullPineapple.getLayoutX() + 20);
        imgHalfPineapple2.setLayoutY(this.imgFullPineapple.getLayoutY() - 21);
        
        imgHalfPineapple1.setFitWidth(100);
        imgHalfPineapple1.setFitHeight(100);
        
        imgHalfPineapple2.setFitWidth(100);
        imgHalfPineapple2.setFitHeight(150);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(imgHalfPineapple1);
        anchorPane.getChildren().add(imgHalfPineapple2);
        
        // Remove the pineapple and make the two halves of pineapple visible
        this.imgFullPineapple.setVisible(false);
        imgHalfPineapple1.setVisible(true);
        imgHalfPineapple2.setVisible(true);
        
        // Play the cut sound
        this.playCutSound();
        
        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> 
        {
            setAnimation(this.imgHalfPineapple1, this.imgHalfPineapple2);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    
    @Override
    public void setAnimation(ImageView imgHalfPineapple1, ImageView imgHalfPineapple2)
    {
        // Rotate and move in X, Y
        imgHalfPineapple1.setRotate(imgHalfPineapple1.getRotate() + 1);
        imgHalfPineapple2.setRotate(imgHalfPineapple2.getRotate() + 1);
        
        imgHalfPineapple1.setX(imgHalfPineapple1.getX() + 1);
        imgHalfPineapple1.setY(imgHalfPineapple1.getY() + 1);
        
        imgHalfPineapple2.setX(imgHalfPineapple2.getX() - 1);
        imgHalfPineapple2.setY(imgHalfPineapple2.getY() + 1);
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
        
        // Tie the trace with our full pineapple layout x, y
        trace.setLayoutX(this.imgFullPineapple.getLayoutX() - 17);
        trace.setLayoutY(this.imgFullPineapple.getLayoutY() + 21);
        
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
        this.imgFullPineapple.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Pineapple.centerX - this.radiusX && this.X < Pineapple.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Pineapple.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Pineapple.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Pineapple
            this.imgFullPineapple.setLayoutX(this.X);
            this.imgFullPineapple.setLayoutY(this.Y);
            this.imgFullPineapple.setRotate(this.imgFullPineapple.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}