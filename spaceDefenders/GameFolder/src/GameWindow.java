import java.io.File;
import java.io.IOException;
import java.net.http.WebSocket.Listener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.*;
import model.GameObject.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.css.PseudoClass;
import javafx.util.Duration;
import javafx.scene.media.*;
import javafx.scene.paint.Paint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * File: GameWindow.java Desc: View Class, the display screen of the Game;
 * handles animation and input key handling
 *
 * @author Jeremy Tan
 * @reference https://github.com/tutsplus/Introduction-to-JavaFX-for-Game-Development/blob/master/Example5.java
 *            for input key handling pt 1
 * @reference https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 *            for input key handling pt 2
 */

public class GameWindow {

    @FXML
    BorderPane borderpaneGameWindow;
    @FXML
    VBox vboxController;
    @FXML
    HBox hboxMenu; // menu with save and exit buttons
    @FXML
    VBox PlayerDisplayInfo; // displays info for the player on the top right side of the screen
    @FXML
    VBox vboxCenter;
    @FXML
    Button btnSaveGame; // save game button
    @FXML
    Button btnExit; // exit game button
    @FXML
    Pane paneGameStage;

    @FXML
    Label lblPauseText; // indicates that the game is paused
    @FXML
    Label lblPlayerName; // shows the player name
    @FXML
    Label lblPlayerHealth; // shows the player's current health
    @FXML
    Label lblAliensKilled; // shows how many aliens the player has killed
    @FXML
    Label lblArtifactsCollected; // shows how many artifacts the player has collected
    @FXML
    Label lblLastKilledEnemy; // shows the last enemy that the player killed

    Difficulty difficultyAdjuster; // difficulty object that containts multipliers
    KeyFrame keyframeAnimation; // key frame for game movement
    KeyFrame keyframeSpawningEnemies; // key frame for spawning Actors
    Timeline gameClockMovement; // timeline for game movement
    Timeline gameClockSpawning; // timeline for spawning Actors

    Scene currentScene; // currently used scene
    World world = GameController.instance().getWorld(); // current world object in game intsance
    Player playership; // player object in the world class
    DisplayActor playerimg;

    List<ImageView> actorDisplayList = new ArrayList<ImageView>(); // list of all actor image views
    List<ImageView> removeActors = new ArrayList<ImageView>(); // list of actors to be removed from the screen

    List<String> inputKeys = new ArrayList<String>(); // list of key inputs to be executed

    boolean pauseGame; // tells if game is paused
    boolean easterEgg = false; // tells if easter egg has been found

    ImageView lastKilledEnemyPicture; // image of the last killed enemy

    final int PANE_X_MIN = -230; // min x value of game pane
    final int PANE_X_MAX = 1120; // max x value of game pane
    final int PANE_Y_MIN = -20; // min y value of game pane
    final int PANE_Y_MAX = 760; // max y value of game pane

    // general game music
    final AudioClip AUDIO_INGAMEMUSIC = new AudioClip(getClass().getResource("/media/Final_Test.mp3").toString());
    // button clicked sound effect
    final AudioClip AUDIO_BUTTONCLICKED = new AudioClip(
            getClass().getResource("/media/confirmation_001.mp3").toString());
    // sound when player moves
    final AudioClip AUDIO_PLAYERMOVEMENT = new AudioClip(
            getClass().getResource("/media/spaceEngine_000.mp3").toString());
    // sound when player is hit
    final AudioClip AUDIO_PLAYERHIT = new AudioClip(
            getClass().getResource("/media/explosionCrunch_004.mp3").toString());
    // sound when player fires projectile
    final AudioClip AUDIO_PLAYERFIRE = new AudioClip(getClass().getResource("/media/laserRetro_000.mp3").toString());
    // sound when enemy files a projectile
    final AudioClip AUDIO_ENEMYFIRE = new AudioClip(getClass().getResource("/media/laserSmall_000.mp3").toString());
    // sound when pause button is pressed
    final AudioClip AUDIO_ESCAPEMENUOPEN = new AudioClip(getClass().getResource("/media/doorOpen_000.mp3").toString());
    // sound when game is resumed
    final AudioClip AUDIO_ESCAPEMENUCLOSED = new AudioClip(
            getClass().getResource("/media/doorClose_000.mp3").toString());
    // sound when game ends
    final AudioClip AUDIO_GAMEOVER = new AudioClip(getClass().getResource("/media/Enders_GameOver.mp3").toString());
    // sound when cheat mode is activated
    final AudioClip AUDIO_CHEATMODE = new AudioClip(getClass().getResource("/media/forceField_000.mp3").toString());
    // sound when mega bomb is activated
    final AudioClip AUDIO_MEGABOMB = new AudioClip(getClass().getResource("/media/megaBomb_000.wav").toString());
    FloatControl gainControl;

    @FXML
    // initializes game screen
    void initialize(Scene scene, boolean loadGame) {

        AUDIO_INGAMEMUSIC.setCycleCount(AudioClip.INDEFINITE);
        AUDIO_INGAMEMUSIC.play();
        currentScene = scene;
        vboxController.getStyleClass().add("backgroundgame");

        hboxMenu.getStyleClass().add("gamemenubutton");
        PlayerDisplayInfo.getStyleClass().add("gameplayerdisplaylabel");
        world.setBoundries(PANE_X_MIN, PANE_X_MAX, PANE_Y_MIN, PANE_Y_MAX);

        // checks if new game or loaded game
        if (loadGame) {
            if (!GameController.instance().load(GameController.getGamestateFilename())) {
                // System.out.println("error");
                return;
            } // loads the serialization data
        }

        playership = world.getPlayer(); // get player from instance rather than create a new one (Swaim)
        playerimg = new DisplayActor(playership, "/images/player_ship.png");
        displayImageView(playerimg);

        scene.setOnKeyPressed((EventHandler<? super KeyEvent>) new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                if (!inputKeys.contains(code))
                    inputKeys.add(code);
                onKeyPressedEvent(e);
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                inputKeys.remove(code);
                AUDIO_PLAYERMOVEMENT.stop();
            }
        });

        keyframeAnimation = new KeyFrame(Duration.millis(15), e -> beginAnimations()); // roughly 60fps

        // temporary until i figure out a better way to do this
        keyframeSpawningEnemies = new KeyFrame(Duration.seconds(6), e -> beginSpawning());

        gameClockMovement = new Timeline(keyframeAnimation);
        gameClockSpawning = new Timeline(keyframeSpawningEnemies);
        gameClockMovement.setCycleCount(Timeline.INDEFINITE);
        gameClockSpawning.setCycleCount(Timeline.INDEFINITE);
        gameClockMovement.play();
        gameClockSpawning.play();
    }

    @FXML
    // handles when save button is clicked
    void onSaveGameBtnClicked(ActionEvent event) {
        AUDIO_BUTTONCLICKED.play();
        GameController.instance().save(GameController.getGamestateFilename());
    }

    @FXML
    // handles when exit button is clicked
    void onExitBtnClicked(ActionEvent event) {
        AUDIO_BUTTONCLICKED.play();
        onExit();
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    // stops music when exit button is clicked, as well as pauses and resets the GameController
    void onExit() {
        AUDIO_INGAMEMUSIC.stop();
        AUDIO_GAMEOVER.stop();
        gameClockMovement.stop();
        gameClockSpawning.stop();
        pauseGame = true;
        GameController.reset();
    }

    // spawns actors into the world
    void beginSpawning() {
        if (pauseGame) {
            return;
        }
        if (!world.isGameover()) {
            world.spawnActors();
        }
    }

    // runs the game based on the game clock timeline
    void beginAnimations() {

        if (pauseGame) {
            return;
        } // returns void if game is paused

        if (!world.isGameover()) {
            try {
                setActors();
                world.run(); // updates the position of each actors; if an actor goes off screen, remove it
                removeActors();
                updatePlayerInfoDisplay();
            } catch (ConcurrentModificationException CME) {
                // do nothing.
            }
        } else {
            onGameOver();
        }

        // below code for debugging purposes to detect possible resource leak
        /*
         * int temp = actorDisplayList.size(); if (temp !=
         * TEMPCOUNTER_DELETEAFTERDEBUGGING) { TEMPCOUNTER_DELETEAFTERDEBUGGING = temp;
         * System.out.println(TEMPCOUNTER_DELETEAFTERDEBUGGING); }
         */
    }

    // acts on any key event
    // manipulates game based on key pressed
    void onKeyPressedEvent(KeyEvent keyEvent) {
        // String key = keyEvent.getCode().toString();
        // System.out.println("Pressed: " + key);
        // above code for debugging purposes

        if (world.isGameover()) {
            return;
        } // no key presses registered if game is over

        if (keyEvent.getCode().toString().equals("ESCAPE")) {
            pauseGame = !pauseGame;
            onPauseKeyPressed();
            return;
        } else if (pauseGame == false) {
            // world.updatePlayerPosition(inputKeys);

            if (inputKeys.contains("W")) {
                // move the ship up the screen
                soundeffectPlayerMovement();
                world.playerMoveForward();
            } else if (inputKeys.contains("A")) {
                // move the ship to the left
                soundeffectPlayerMovement();
                world.playerMoveLeft();
            } else if (inputKeys.contains("S")) {
                // move the ship down the screen
                soundeffectPlayerMovement();
                world.playerMoveBackward();
            } else if (inputKeys.contains("D")) {
                // move the ship to the right
                soundeffectPlayerMovement();
                world.playerMoveRight();
            } else if (inputKeys.contains("UP")) {
                // move the ship up the screen
                soundeffectPlayerMovement();
                world.playerMoveForward();
            } else if (inputKeys.contains("LEFT")) {
                // move the ship to the left
                soundeffectPlayerMovement();
                world.playerMoveLeft();
            } else if (inputKeys.contains("DOWN")) {
                // move the ship down the screen
                soundeffectPlayerMovement();
                world.playerMoveBackward();
            } else if (inputKeys.contains("RIGHT")) {
                // move the ship to the right
                soundeffectPlayerMovement();
                world.playerMoveRight();
            } else if (inputKeys.contains("F")) {
                // shoot the projectile
                world.playerFireProjectile();
            } else if (inputKeys.contains("P")) {
                // this would be for the cheat button
                AUDIO_CHEATMODE.play();
                world.playerCheatModePressed();
            } else if (inputKeys.contains("M")) {
                if (!world.getPlayer().isMegaBombUsed()) {
                    megaBombGraphic();
                    AUDIO_MEGABOMB.play();
                }
                world.getPlayer().useMegaBomb(); // activates mega bomb
            }
        }

    }

    // activates when mega bomb is used
    // changes the background to a mega bomb flash for 2.5 seconds, then
    // returns to normal background
    void megaBombGraphic() {
        vboxController.getStyleClass().remove("backgroundgame");
        vboxController.getStyleClass().add("megaBombFlash");

        keyframeAnimation = new KeyFrame(Duration.seconds(4), e -> {
            vboxController.getStyleClass().remove("megaBombFlash");
            vboxController.getStyleClass().add("backgroundgame");
        });
        gameClockMovement = new Timeline(keyframeAnimation);
        gameClockMovement.setCycleCount(1);
        gameClockMovement.play();
    }

    // loads specific user choice data from MainWindow into the world
    void loadUserChoiceData(StageTypes chosenGameMode, DifficultyTypes chosenDifficulty, String username) {
        difficultyAdjuster = new Difficulty(chosenDifficulty);
        world.setDifficultyLevel(difficultyAdjuster);
        world.beginGame(chosenGameMode, username, chosenDifficulty);
    }

    // takes in a DisplayActor and displays the appropriate image on the game screen
    // binds the ImageView with the appropriate model Actor
    void displayImageView(DisplayActor actorimg) {
        Actor actor = actorimg.getActorObject();
        actor.setImgwidth(actorimg.getActorImage().getWidth());
        actor.setImglength(actorimg.getActorImage().getHeight());

        ImageView actordisplay = new ImageView(actorimg.getActorImage());
        actordisplay.setUserData(actorimg); // please do not change this back!!!
        actordisplay.setRotate(actorimg.getActorObject().getDirection());
        actordisplay.layoutXProperty().bind(actorimg.getActorObject().getLocation().getXProperty());
        actordisplay.layoutYProperty().bind(actorimg.getActorObject().getLocation().getYProperty());
        actordisplay.rotateProperty().bind(actorimg.getActorObject().getDirectionProperty());

        if (actor instanceof ProjectileAlien2) {
            ProjectileAlien2 laserbeam = (ProjectileAlien2) actor;
            actordisplay.setFitHeight(laserbeam.getHeightStretch());
        }

        actorDisplayList.add(actordisplay);
        paneGameStage.getChildren().add(actordisplay);
    }

    // sets actors on the game screen based on actors in world.activeActors
    void setActors() {
        for (Actor actor : world.getActiveActors()) {
            Random rand = new Random();
            int choice = 0;
            String imgurl = "/images/";
            ActorTypes type = actor.getActorType();

            if (actor.getIsPlacedOnScreen() == false) {
                actor.setIsPlacedOnScreen(true);
                // places actor on game screen
                switch (type) {
                    case ALIENSHIP1: {
                        choice = rand.nextInt(3);
                        if (choice == 0) {
                            imgurl += "type1_var1.png";
                        } else if (choice == 1) {
                            imgurl += "type1_var2.png";
                        } else if (choice == 2) {
                            imgurl += "type1_var3.png";
                        }
                        displayImageView(new DisplayActor(actor, imgurl));
                        break;
                    }
                    case ALIENSHIP2: {
                        choice = rand.nextInt(3);
                        if (choice == 0) {
                            imgurl += "type2_var1.png";
                        } else if (choice == 1) {
                            imgurl += "type2_var2.png";
                        } else if (choice == 2) {
                            imgurl += "type2_var3.png";
                        }
                        displayImageView(new DisplayActor(actor, imgurl));
                        break;
                    }
                    case ALIENSHIP3: {
                        choice = rand.nextInt(2);
                        if (choice == 0) {
                            imgurl += "type3_var1.png";
                        } else if (choice == 1) {
                            imgurl += "type3_var2.png";
                        }
                        displayImageView(new DisplayActor(actor, imgurl));
                        break;
                    }
                    case ARTIFACT: {
                        displayImageView(new DisplayActor(actor, "/images/ufoYellow.png"));
                        break;
                    }
                    case ASTEROID: {
                        displayImageView(new DisplayActor(actor, "/images/meteorBrown_big1.png"));
                        break;
                    }
                    case PROJECTILE1_PLAYER: {
                        displayImageView(new DisplayActor(actor, "/images/laserBlue01.png"));
                        AUDIO_PLAYERFIRE.play();
                        break;
                    }
                    case PROJECTILE1_ALIEN: {
                        displayImageView(new DisplayActor(actor, "/images/laserGreen02.png"));
                        AUDIO_ENEMYFIRE.play();
                        break;
                    }
                    case PROJECTILE2_ALIEN: {
                        displayImageView(new DisplayActor(actor, "/images/laser-beam.gif"));
                        break;
                    }
                }
            }
        }
    }

    // removes actors from the screen if they need to be removed per the removeActors list in world
    void removeActors() {
        for (Actor actor : world.getRemovedActors()) {
            for (ImageView actordisplay : actorDisplayList) {
                DisplayActor dActor = (DisplayActor) actordisplay.getUserData();
                int id = dActor.getActorObject().getID();
                if (id == actor.getID()) {
                    // actorDisplayList.remove(actordisplay);
                    // add code here that removes actor from removedActor List to prevent data
                    // bloating

                    if (!actor.getForceOutOfScreen()) {
                        actor.setForceOutOfScreen(true);
                        if (actor.getActorType().equals(ActorTypes.ALIENSHIP1)
                                || actor.getActorType().equals(ActorTypes.ALIENSHIP2)
                                || actor.getActorType().equals(ActorTypes.ALIENSHIP3)) {
                            if (actor.getCollidedSoundPlay() == true) {
                                actor.setCollidedSoundPlay(false);
                                AUDIO_PLAYERHIT.play(); // play sound when aliens are hit
                            }
                        }
                        removeActors.add(actordisplay); // added by Nathan Swaim
                        actorDisplayList.remove(actordisplay);
                    } else {
                        removeActors.add(actordisplay); // added by Nathan Swaim
                    }
                }
            }
        }

        // removes deleted actors from the screen
        for (ImageView actor : removeActors) {
            paneGameStage.getChildren().remove(actor);
        }
        removeActors.clear();
    }

    // updates the player's HUD with current information
    void updatePlayerInfoDisplay() {
        playership = world.getPlayer();
        if (playership.getCollided() == true) {
            if (AUDIO_PLAYERHIT.isPlaying() == false) {
                AUDIO_PLAYERHIT.play();
            }
        }
        playership.incrementGunCooldownTimer();
        lblPlayerName.setText(
                playership.getUsername() + " : " + playership.getTotalPoints(world.getDifficultyLevel()) + " points");
        // double healthpercent = (playership.getHealth() / 10) * 100;
        // lblPlayerHealth.setText(healthpercent + "%");
        if (world.getCheat()) {
            // https://stackoverflow.com/questions/49016202/using-unicode-characters-with-javafx
            lblPlayerHealth.setText("\u221E"); // Infinity Symbol
        } else {
            lblPlayerHealth.setText(playership.getHealth() * 10 + "%");
        }
        lblAliensKilled.setText("x" + playership.getTotalAliensKilled());
        /*
         * if (lastKilledEnemyPicture != null) {
         * lastKilledEnemyPicture.setPreserveRatio(true);
         * lastKilledEnemyPicture.prefHeight(80);
         * lblLastKilledEnemy.setGraphic(lastKilledEnemyPicture); }
         */
        lblArtifactsCollected.setText("x" + playership.getTotalArtifactsCollected());

        if (playership.getLocation().getY() <= PANE_Y_MIN - 20) {
            secretLocationEasterEgg();
        }
    }

    // pauses the game or plays the game when the pause button is pressed
    void onPauseKeyPressed() {
        if (pauseGame == true) {
            AUDIO_ESCAPEMENUOPEN.play();
            gameClockMovement.pause();
            gameClockSpawning.pause();
            lblPauseText.setText("PAUSED");
            lblPauseText.getStyleClass().add("gameplayerdisplaylabel");
            AUDIO_INGAMEMUSIC.stop();
        } else if (pauseGame == false) {
            AUDIO_ESCAPEMENUCLOSED.play();
            gameClockMovement.play();
            gameClockSpawning.play();
            lblPauseText.getStyleClass().remove("gameplayerdisplaylabel");
            lblPauseText.setText("");
            AUDIO_INGAMEMUSIC.play();
        }
    }

    // handles the game over scenario in the Game Window
    void onGameOver() {
        if (AUDIO_GAMEOVER.isPlaying() == false) {
            AUDIO_GAMEOVER.play();
        }
        if (AUDIO_INGAMEMUSIC.isPlaying()) {
            AUDIO_INGAMEMUSIC.stop();
        }
        borderpaneGameWindow.getCenter().setOpacity(0.0);
        Label lblGameOverText = new Label("GAME OVER" + "\n" + playership.getUsername() + ": "
                + world.getPlayer().getTotalPoints(world.getDifficultyLevel()) + " points");
        lblGameOverText.getStyleClass().add("gameplayerdisplaylabel");
        borderpaneGameWindow.setCenter(lblGameOverText);
        GameController.reset(); // resets the game world
    }

    // plays the sound effects when the player moves
    void soundeffectPlayerMovement() {
        if (AUDIO_PLAYERMOVEMENT.isPlaying() != true) {
            AUDIO_PLAYERMOVEMENT.play();
        }
    }

    // Taken from:
    // https://stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
    void playBackgroundMusic() throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("/media/Final_Test.mp3"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.start();
    }

    // handles the easter egg logic when found by the player
    void secretLocationEasterEgg() {
        if (easterEgg) {
            return; // returns if easter egg has already been found
        }

        pauseGame = true;
        onPauseKeyPressed(); // pauses the game

        try {
            // AUDIO_BUTTONCLICKED.play();
            world.getPlayer().setHitpoints(10);
            var loader = new FXMLLoader(getClass().getResource("EasterEggWindow.fxml"));
            var scene = new Scene(loader.load());
            scene.getRoot().requestFocus();

            var stage = new Stage();
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Easter Egg");

            EasterEggWindow easterEggWindow = loader.getController();
            easterEggWindow.initialize();
            easterEgg = true;

            // this sometimes causes the player to be unable to move
            world.getPlayer().getLocation().setX(250);
            world.getPlayer().getLocation().setY(25);
        } catch (Exception e) {
            // do nothing
        }

    }
}
