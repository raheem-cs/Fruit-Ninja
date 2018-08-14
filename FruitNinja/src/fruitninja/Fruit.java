package fruitninja;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public abstract class Fruit
{
  // Create abstract methods  
    public abstract void cutFruit(AnchorPane anchorPane);
    public abstract void setAnimation(ImageView fruitHalf1, ImageView fruitHalf2);
    public abstract void playCutSound();
    public abstract void createSlashTrace(double startX, double startY, double endX, double endY, AnchorPane pane);
    public abstract void appearFruit(AnchorPane pane);
}