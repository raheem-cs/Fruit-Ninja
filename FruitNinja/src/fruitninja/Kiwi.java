package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Kiwi extends Fruit
{
    // Data Fields - The kiwi image and the two halves
    public ImageView imgFullKiwi;
    public ImageView imgHalfKiwi1;
    public ImageView imgHalfKiwi2;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    
    // Constructors
    public Kiwi(AnchorPane anchorPane)
    {
        // Default values
        this.imgFullKiwi = new ImageView("images/Kiwi.png");
        
        // Set any X, Y, and the orange dimensions
        this.imgFullKiwi.setX(100);
        this.imgFullKiwi.setY(100);
        this.imgFullKiwi.setFitWidth(82);
        this.imgFullKiwi.setFitHeight(89);
        
        // Place the orange on the pane
        anchorPane.getChildren().add(this.imgFullKiwi);
    }
    
    public Kiwi(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the orange data from parametars above
        this.imgFullKiwi = new ImageView(filePath);
        
        this.imgFullKiwi.setX(x);
        this.imgFullKiwi.setX(y);
        this.imgFullKiwi.setFitWidth(82);
        this.imgFullKiwi.setFitHeight(89);
        
        anchorPane.getChildren().add(this.imgFullKiwi);
    }
    
    // Methods
    @Override
    public void cutFruit(AnchorPane anchorPane)
    {
        // Create the two halves - Set image for every half
        this.imgHalfKiwi1 = new ImageView("images/kiwi_half1.png");
        this.imgHalfKiwi2 = new ImageView("images/kiwi_half2.png");
        
        // Fix them at a hard-coded position - They will start moving/rotating from it
        this.imgHalfKiwi1.setRotate(22);
        this.imgHalfKiwi2.setRotate(25);

        this.imgHalfKiwi1.setLayoutX(this.imgFullKiwi.getLayoutX() - 2);
        this.imgHalfKiwi1.setLayoutY(this.imgFullKiwi.getLayoutY() + 38);
        
        this.imgHalfKiwi2.setLayoutX(this.imgFullKiwi.getLayoutX() - 5);
        this.imgHalfKiwi2.setLayoutY(this.imgFullKiwi.getLayoutY() - 18);
        
        this.imgHalfKiwi1.setFitWidth(92);
        this.imgHalfKiwi1.setFitHeight(76);
        
        this.imgHalfKiwi2.setFitWidth(92);
        this.imgHalfKiwi2.setFitHeight(76);
        
        // Add the two halves to the pane
        anchorPane.getChildren().add(this.imgHalfKiwi1);
        anchorPane.getChildren().add(this.imgHalfKiwi2);
        
        // Remove the kiwi and make the two halves of kiwi visible
        this.imgFullKiwi.setVisible(false);
        this.imgHalfKiwi1.setVisible(true);
        this.imgHalfKiwi2.setVisible(true);
        
        // Play the cut sound
        this.playCutSound();
        
        // Start animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> 
        {
            setAnimation(this.imgHalfKiwi1, this.imgHalfKiwi2);
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    
    @Override
    public void setAnimation(ImageView imgHalfOrange1, ImageView imgHalfOrange2)
    {
        // Rotate and move in X, Y
        this.imgHalfKiwi1.setRotate(imgHalfKiwi1.getRotate() + 1);
        this.imgHalfKiwi2.setRotate(imgHalfKiwi2.getRotate() + 1);
        
        this.imgHalfKiwi1.setX(imgHalfKiwi1.getX() + 1);
        this.imgHalfKiwi1.setY(imgHalfKiwi1.getY() + 1);
        
        this.imgHalfKiwi2.setX(imgHalfKiwi2.getX() - 1);
        this.imgHalfKiwi2.setY(imgHalfKiwi2.getY() + 1);
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
        
        // Tie the trace with our full kiwi layout x, y
        trace.setLayoutX(this.imgFullKiwi.getLayoutX() - 28);
        trace.setLayoutY(this.imgFullKiwi.getLayoutY() - 25);
        
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
        this.imgFullKiwi.setLayoutX(this.X);
        
        // Play sound for appear fruit
        AudioClip fruitAppearEffect = new AudioClip(this.getClass().getResource("/sounds/fruit_appear2.mp3").toExternalForm());
        fruitAppearEffect.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Kiwi.centerX - this.radiusX && this.X < Kiwi.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Kiwi.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Kiwi.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Kiwi
            this.imgFullKiwi.setLayoutX(this.X);
            this.imgFullKiwi.setLayoutY(this.Y);
            this.imgFullKiwi.setRotate(this.imgFullKiwi.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
    }
}