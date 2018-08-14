package fruitninja;

import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Bomb
{
    // Data Fields
    public ImageView imgBomb;
    
    // Some data fields related to object path
    public static double centerX, centerY;
    double radiusX, radiusY, X, Y;
    
    // Static data field to be used in pause purpose
    public static Timeline bmbAnimation;
    
    // Constructors
    public Bomb(AnchorPane anchorPane)
    {
        // Default values
        this.imgBomb = new ImageView("images/Bomb.gif");
        
        // Set any X, Y, and the bomb dimensions
        this.imgBomb.setX(100);
        this.imgBomb.setY(100);
        this.imgBomb.setFitWidth(150);
        this.imgBomb.setFitHeight(100);
        
        // Place the bomb on the pane
        anchorPane.getChildren().add(this.imgBomb);
    }
    
    public Bomb(String filePath, double x, double y, AnchorPane anchorPane)
    {
        // Set the banana data from parametars above
        this.imgBomb = new ImageView(filePath);
        
        this.imgBomb.setX(x);
        this.imgBomb.setX(y);
        this.imgBomb.setFitWidth(150);
        this.imgBomb.setFitHeight(100);
        
        anchorPane.getChildren().add(this.imgBomb);
    }
    
    public void appearBomb(AnchorPane pane)
    {
        // Random number have range this good in game physics
        this.radiusX = ((pane.getPrefWidth() / 2) - 100) * ThreadLocalRandom.current().nextDouble(0.3, 1.0);
        this.radiusY = (pane.getPrefHeight() - 136) * ThreadLocalRandom.current().nextDouble(0.4, 1.0);
        
        this.X = pane.getPrefWidth()/2 - this.radiusX; // Starting point for fruit
        this.imgBomb.setLayoutX(this.X);
        
        // Play sound for bomb appear
        AudioClip bombAppearSound = new AudioClip(this.getClass().getResource("/sounds/bomb_appear2.mp3").toExternalForm());
        bombAppearSound.play(1);
        
        // Timeline to set animation for current fruit
        Timeline makeAnimation = new Timeline(new KeyFrame(Duration.millis(3), makeAnimationAppear ->
        {
            if (this.X >= Bomb.centerX - this.radiusX && this.X < Bomb.centerX + this.radiusX)
            {
                this.X++;
                this.Y = Bomb.centerY - (this.radiusY * Math.sqrt(1 - ((1.0/Math.pow(this.radiusX, 2)) * Math.pow(this.X - Bomb.centerX, 2))));
            }
            else
            {
                this.X++;
                this.Y++;
            }
            
            // Bomb
            this.imgBomb.setLayoutX(this.X);
            this.imgBomb.setLayoutY(this.Y);
            this.imgBomb.setRotate(this.imgBomb.getRotate() + 1);
        }));
        
        makeAnimation.setCycleCount(1000);
        makeAnimation.play();
        bmbAnimation = makeAnimation;
    }
}