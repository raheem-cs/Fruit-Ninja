/*
*    PC Fruit Ninja © by Raheem Elsayed, Ahmad Hassan
*    Coded for educational purposes
*
*    Created:  May-2018
*    Released: August-2018
*/

package fruitninja;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameSceneController implements Initializable
{
    // Define general variables
    AudioClip backgroundSound, countDownFinishing, endGameSound, cutBomb;
    boolean gameWorking = true, endSoundEmitted = false, scoreSaved = false;
    Timeline countDown;
    double startX, startY, endX, endY;
    
    // Bomb
    Random bomb;
    Bomb bmb;
    
    // Timeline
    Timeline watermelonsAppear, bananasAppear, orangesAppear, pineappleAppear, 
             kiwisAppear, redapplesAppear, bombAppear;
    
    // Fruits
    Watermelon wm;
    Banana bna;
    Orange org;
    Pineapple pa;
    Kiwi kwi;
    RedApple ra;

    // Some pulbic static variables, it will be used in static methods or from another classes
    public static Text staticTxtScore;
    public static int highestScore;

    // FXML variables
    @FXML
    private ImageView imgPause;
    
    @FXML
    private ImageView imgResume;
    
    @FXML
    private Text txtCountDown;
    
    @FXML
    private Text txtCuttedFruits;
    
    @FXML
    private Text txtScore;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ImageView imgBackground;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Play background sound
        backgroundSound = new AudioClip(this.getClass().getResource("/sounds/game_background_sound.mp3").toExternalForm());
        backgroundSound.play(0.5);
        
        // Repeat the background sound when it ends as the setCycleCount for audio clip not working
        Timeline repeatSound = new Timeline(new KeyFrame(Duration.seconds(41), e -> {
            backgroundSound.play(0.5);
        }));
        
        repeatSound.setCycleCount(Timeline.INDEFINITE);
        repeatSound.play();

        // Start the count down from 90
        startCountDown(90);
        
        // Constant related to path
        Watermelon.centerX = anchorPane.getPrefWidth() / 2;
        Watermelon.centerY = anchorPane.getPrefHeight();
        Banana.centerX = anchorPane.getPrefWidth() / 2;
        Banana.centerY = anchorPane.getPrefHeight();
        Orange.centerX = anchorPane.getPrefWidth() / 2;
        Orange.centerY = anchorPane.getPrefHeight();
        Pineapple.centerX = anchorPane.getPrefWidth() / 2;
        Pineapple.centerY = anchorPane.getPrefHeight();
        Kiwi.centerX = anchorPane.getPrefWidth() / 2;
        Kiwi.centerY = anchorPane.getPrefHeight();
        RedApple.centerX = anchorPane.getPrefWidth() / 2;
        RedApple.centerY = anchorPane.getPrefHeight();
        Bomb.centerX = anchorPane.getPrefWidth() / 2;
        Bomb.centerY = anchorPane.getPrefHeight();
        
        // Start Game
        startGame();
        
        // Save the txtScore refrence
        staticTxtScore = this.txtScore;
        
        // If player open the game scene, then close the stage --> save his score
        FruitNinja.primaryStage.setOnCloseRequest(e -> 
            {
                try
                {
                    if (!scoreSaved)
                    {
                        // Score not saved and he closed the stage, save it in this case
                        saveScore();
                    }
                }
                catch (IOException ex)
                {
                    System.out.println("Scores.txt not found..");
                }
            }
        );
    }

    @FXML
    private void onImgPausePressed(MouseEvent event)
    {
        pauseResumeGame();
    }

    @FXML
    private void omImgResumePressed(MouseEvent event)
    {
        pauseResumeGame();
    }
    
    public void pauseResumeGame()
    {
        if (gameWorking)
        {
            // Pause button clicked, Make pause icon disappear, appear resume one
            gameWorking = false;
            imgPause.setVisible(false);
            imgResume.setVisible(true);
            
            // Pause all fruit appear, and it's current animation
            pauseCountDown();
            watermelonsAppear.pause();
            bananasAppear.pause();
            orangesAppear.pause();
            pineappleAppear.pause();
            kiwisAppear.pause();
            redapplesAppear.pause();
            bombAppear.pause();
        }
        else
        {
            // Resume button clicked, Make resume icon disappear, appear pause one
            gameWorking = true;
            imgPause.setVisible(true);
            imgResume.setVisible(false);
            
            // Resume all fruit appear, and it's current animation
            resumeCountDown();
            watermelonsAppear.play();
            bananasAppear.play();
            orangesAppear.play();
            pineappleAppear.play();
            kiwisAppear.play();
            redapplesAppear.play();
            bombAppear.play();
        }
    }
    
    public void startCountDown(int time)
    {
        // Set initial value for game count down
        txtCountDown.setText(Integer.toString(time));
        
        // Make time line to decrease the time very one second
        countDown = new Timeline(new KeyFrame(Duration.seconds(1), e ->
        {
            // Get the current countdown time
            int countDownTime = getCountDownTime();
            
            // If countDownTime <= 10 Make special effect
            if (countDownTime <= 10)
            {
                txtCountDown.setFill(Color.RED);
                FadeTransition fadeCountDown = new FadeTransition(Duration.millis(300), txtCountDown);
                fadeCountDown.setCycleCount(10);
                fadeCountDown.setFromValue(0.5);
                fadeCountDown.setToValue(1);
                fadeCountDown.play();
                
                // Decrease by 1
                if (countDownTime >= 1)
                {
                    txtCountDown.setText(Integer.toString(countDownTime - 1));
                }
                
                // Start special sound at ending
                if (getCountDownTime() <= 8)
                {
                    if (!endSoundEmitted)
                    {
                        countDownFinishing = new AudioClip(this.getClass().getResource("/sounds/count_down_finishing.mp3").toExternalForm());
                        endGameSound = new AudioClip(this.getClass().getResource("/sounds/end_sound.mp3").toExternalForm());
                        
                        Timeline endSoundsEffect = new Timeline(
                                new KeyFrame(Duration.ZERO, countDownSound -> 
                                {
                                    countDownFinishing.play(0.7);
                                }),
                                new KeyFrame(Duration.seconds(8), endSound -> 
                                {
                                    endGameSound.play(1.0);
                                })
                        );
                 
                        endSoundsEffect.play();
                        endSoundEmitted = true;
                    }
                    
                    if (countDownTime <= 0)
                    {
                        // Here load the end game pane
                        Timeline delay = new Timeline(
                            new KeyFrame(Duration.millis(10), eventDelay2 -> 
                            {
                                try
                                {
                                    AnchorPane root = FXMLLoader.load(this.getClass().getResource("GameEndPane.fxml"));
                                    anchorPane.getChildren().add(root);
                                    root.setLayoutX(280);
                                    root.setLayoutY(220);
                                    
                                    // Stop all music, appear tasks
                                    backgroundSound.stop();
                                    watermelonsAppear.stop();
                                    bananasAppear.stop();
                                    orangesAppear.stop();
                                    pineappleAppear.stop();
                                    kiwisAppear.stop();
                                    redapplesAppear.stop();
                                    bombAppear.stop();
                                }
                                catch (IOException ex)
                                {
                                    System.out.println("GameEndPane.fxml not found..");
                                }
                            })
                        );
                        
                        delay.play();
                        
                        // Game ended, save player score
                        try
                        {
                            // Get high score in the file before saving
                            highestScore = getHighestScore();
                            
                            // Save his score
                            saveScore();
                            
                            // Score saved on end of timer
                            scoreSaved = true;
                        }
                        catch (IOException ex)
                        {
                            System.out.println("Scores.txt not found!");
                        }
                    }
                }
            }
            else
            {
                // Decrease 1, without effects
                txtCountDown.setText(Integer.toString(countDownTime - 1));
            }
        }));
        
        countDown.setCycleCount(time + 1);
        countDown.play();
    }
    
    public void pauseCountDown()
    {
        countDown.pause();
    }
    
    public void resumeCountDown()
    {
        countDown.play();
    }
    
    public int getCountDownTime()
    {
        return Integer.parseInt(txtCountDown.getText());
    }
    
    // The main game functions
    public void startGame()
    {
        imgBackground.setOnDragDetected(dragDetected -> imgBackground.startFullDrag());
        
        watermelonsAppearTask();
        bananasAppearTask();
        orangesAppearTask();
        pineapplesAppearTask();
        kiwisAppearTask();
        redapplesAppearTask();
        bombAppearTask();
    }
    
    public void watermelonsAppearTask()
    {
        watermelonsAppear = new Timeline(new KeyFrame(Duration.millis(2500), e ->
        {
            wm = new Watermelon(anchorPane);
            wm.imgFullWatermelon.setVisible(false);
            wm.imgFullWatermelon.setFitWidth(100);
            wm.imgFullWatermelon.setFitHeight(100);
            
            wm.imgFullWatermelon.setOnMouseDragEntered(dragEntered -> 
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });
            
            wm.imgFullWatermelon.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the watermelon
                wm.cutFruit(anchorPane);

                // Make slash trace
                wm.createSlashTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore();

                // Increase cutted fruits
                increaseCuttedFruit();
            });
            
            wm.appearFruit(anchorPane);
            
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                wm.imgFullWatermelon.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));
        
        watermelonsAppear.setCycleCount(Timeline.INDEFINITE);
        watermelonsAppear.play();
    }
    
    public void bananasAppearTask()
    {
        bananasAppear = new Timeline(new KeyFrame(Duration.millis(2600), e ->
        {
            bna = new Banana(anchorPane);
            bna.imgFullBanana.setVisible(false);
            
            bna.imgFullBanana.setFitWidth(100);
            bna.imgFullBanana.setFitHeight(150);
            
            bna.imgFullBanana.setOnMouseDragEntered(dragEntered -> 
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });
            
            bna.imgFullBanana.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the banana
                bna.cutFruit(anchorPane);

                // Make slash trace
                bna.createSlashTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore();

                // Increase cutted fruits
                increaseCuttedFruit();
            });
            
            bna.appearFruit(anchorPane);
            
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                bna.imgFullBanana.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));
        
        bananasAppear.setCycleCount(Timeline.INDEFINITE);
        bananasAppear.play();
    }
    
    public void orangesAppearTask()
    {
        orangesAppear = new Timeline(new KeyFrame(Duration.millis(2700), e ->
        {
            org = new Orange(anchorPane);
            org.imgFullOrange.setVisible(false);
            
            org.imgFullOrange.setFitWidth(100);
            org.imgFullOrange.setFitHeight(100);
            
            org.imgFullOrange.setOnMouseDragEntered(dragEntered -> 
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });
            
            org.imgFullOrange.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the orange
                org.cutFruit(anchorPane);

                // Make slash trace
                org.createSlashTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore();

                // Increase cutted fruits
                increaseCuttedFruit();
            });
            
            org.appearFruit(anchorPane);
            
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                org.imgFullOrange.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));
        
        orangesAppear.setCycleCount(Timeline.INDEFINITE);
        orangesAppear.play();
    }
    
    public void pineapplesAppearTask()
    {
        pineappleAppear = new Timeline(new KeyFrame(Duration.millis(2800), e ->
        {
            pa = new Pineapple(anchorPane);
            pa.imgFullPineapple.setVisible(false);
            
            pa.imgFullPineapple.setFitWidth(150);
            pa.imgFullPineapple.setFitHeight(200);
            
            pa.imgFullPineapple.setOnMouseDragEntered(dragEntered -> 
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });
            
            pa.imgFullPineapple.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the pineapple
                pa.cutFruit(anchorPane);

                // Make slash trace
                pa.createSlashTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore();

                // Increase cutted fruits
                increaseCuttedFruit();
            });
            
            pa.appearFruit(anchorPane);
            
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                pa.imgFullPineapple.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));
        
        pineappleAppear.setCycleCount(Timeline.INDEFINITE);
        pineappleAppear.play();
    }
    
    public void kiwisAppearTask()
    {
        kiwisAppear = new Timeline(new KeyFrame(Duration.seconds(3), e ->
        {
            kwi = new Kiwi(anchorPane);
            kwi.imgFullKiwi.setVisible(false);
 
            kwi.imgFullKiwi.setOnMouseDragEntered(dragEntered -> 
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });
            
            kwi.imgFullKiwi.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the kiwi
                kwi.cutFruit(anchorPane);

                // Make slash trace
                kwi.createSlashTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore();

                // Increase cutted fruits
                increaseCuttedFruit();
            });
            
            kwi.appearFruit(anchorPane);
            
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                kwi.imgFullKiwi.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));
        
        kiwisAppear.setCycleCount(Timeline.INDEFINITE);
        kiwisAppear.play();
    }
    
    public void redapplesAppearTask()
    {
        redapplesAppear = new Timeline(new KeyFrame(Duration.seconds(4), e ->
        {
            ra = new RedApple(anchorPane);
            ra.imgFullRedApple.setVisible(false);
 
            ra.imgFullRedApple.setOnMouseDragEntered(dragEntered -> 
            {
                // Get start x, y
                startX = dragEntered.getX();
                startY = dragEntered.getY();
            });
            
            ra.imgFullRedApple.setOnMouseDragExited(dragExit ->
            {
                // Get ends of x, y to draw trace
                endX = dragExit.getX();
                endY = dragExit.getY();

                // Cut the red apple
                ra.cutFruit(anchorPane);

                // Make slash trace
                ra.createSlashTrace(startX, startY, endX, endY, anchorPane);

                // Increase score
                increaseScore();

                // Increase cutted fruits
                increaseCuttedFruit();
            });
            
            ra.appearFruit(anchorPane);
            
            Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
            {
                ra.imgFullRedApple.setVisible(true);
            }));

            delay.setCycleCount(1);
            delay.play();
        }));
        
        redapplesAppear.setCycleCount(Timeline.INDEFINITE);
        redapplesAppear.play();
    }
    
    public void bombAppearTask()
    {
        bomb = new Random();
        
        bombAppear = new Timeline(new KeyFrame(Duration.seconds(3), e ->
        {
            // Based on random boolean the bomb will appear or not
            if (bomb.nextBoolean())
            {
                bmb = new Bomb(anchorPane);
                bmb.imgBomb.setVisible(false);

                bmb.imgBomb.setFitWidth(160);
                bmb.imgBomb.setFitHeight(140);

                // In case of bomb, just if he entered bomb will explode
                bmb.imgBomb.setOnMouseDragEntered(dragEntered -> 
                {
                    // Decrease his score
                    decreaseScore();

                    // Explosion sound
                    AudioClip explosionSound = new AudioClip(this.getClass().getResource("/sounds/cut_bomb.mp3").toExternalForm());
                    explosionSound.play(1);
                    
                    // Make it invisible
                    bmb.imgBomb.setVisible(false);
                    
                    // Hide all other fruits at this moment
                    wm.imgFullWatermelon.setVisible(false);
                    bna.imgFullBanana.setVisible(false);
                    org.imgFullOrange.setVisible(false);
                    pa.imgFullPineapple.setVisible(false);
                    kwi.imgFullKiwi.setVisible(false);
                    ra.imgFullRedApple.setVisible(false);

                    //Special effect in case of explosion
                    FadeTransition ft = new FadeTransition(Duration.millis(300), anchorPane);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.setCycleCount(1);
                    ft.setAutoReverse(true);
                    ft.play();
                });

                bmb.appearBomb(anchorPane);

                Timeline delay = new Timeline(new KeyFrame(Duration.millis(5), delayVisible ->
                {
                    bmb.imgBomb.setVisible(true);
                }));

                delay.setCycleCount(1);
                delay.play();
            }
        }));
        
        bombAppear.setCycleCount(Timeline.INDEFINITE);
        bombAppear.play();
    }
    
    public void increaseScore()
    {
        // TODO: Add sound effect when score increased
        int hisCurrentScore = Integer.parseInt(txtScore.getText());
        txtScore.setText(Integer.toString(hisCurrentScore + 10));
    }
    
    public void decreaseScore()
    {
        // TODO: Add sound effect when score decreased
        int hisCurrentScore = Integer.parseInt(txtScore.getText());
        
        // No negative score
        if (hisCurrentScore >= 80)
        {
            txtScore.setText(Integer.toString(hisCurrentScore - 80));
        }
        else if (hisCurrentScore < 80 && hisCurrentScore >= 0)
        {
            txtScore.setText(Integer.toString(0));
        }
    }
    
    public void increaseCuttedFruit()
    {
        int hisCuttedFruit = Integer.parseInt(txtCuttedFruits.getText());
        txtCuttedFruits.setText(Integer.toString(hisCuttedFruit + 1));
    }

    public static void saveScore() throws IOException
    {
        // Open file to save score to
        File scoreFile = new File("C:\\Scores.txt");
        
        // File wrtiter is uset to append text
        FileWriter scoreFileWriter = new FileWriter(scoreFile, true);
        
        // If player score = 0 then don't save, exit this function
        if (Integer.parseInt(staticTxtScore.getText()) == 0)
            return;

        // " " Space will be added befroe the score number
        scoreFileWriter.write(" " + staticTxtScore.getText());
        scoreFileWriter.close();
    }
    
    /* Function to just return first high score in Scores.txt */
    public static int getHighestScore()
    {
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
            
            // Return highest score stored in Scores.txt
            if (scores.size() == 0)
            {
                // If the Scores.txt was empty then the scores arraylist will be empty then return 0
                return 0;
            }
            else
            {
                return scores.get(0);
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Scores.txt not found..");
        }
        
        // This return in case of this function failed
        return -1;
    }
}