import java.io.IOException;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import model.*;
import model.StageTypes;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * File: MainWindow.java
 * Desc: View Class, the title screen of the game
 *
 * @author Jeremy Tan
 */

public class MainWindow {

    // View variable (GUI Controls) declerations

    @FXML Label error; //not used, ignore
    @FXML VBox vboxController; 

    @FXML HBox hboxMenuController;
    @FXML VBox vboxMainMenu;
    @FXML VBox vboxGameMode;
    @FXML VBox vboxDifficultySetting;
    @FXML VBox vboxUserEntry;
    @FXML TextField txtfieldUserName;

    StageTypes userChosenGameMode;
    DifficultyTypes userChosenDifficulty;
    String username;

    //Constants declerations
    final AudioClip AUDIO_MAINMENUMUSIC = new AudioClip(getClass().getResource("/media/Ender_War.mp3").toString());
    final AudioClip AUDIO_BUTTONCLICKED = new AudioClip(getClass().getResource("/media/confirmation_001.mp3").toString());

    /*
    * defualt run method of MainWindow
    */
    @FXML
    void initialize() {
        vboxController.getStyleClass().add("backgroundmenu");
        for (Node n : hboxMenuController.getChildren()) {
            n.getStyleClass().add("mainmenubutton");
            n.managedProperty().bind(n.visibleProperty());
            n.setVisible(false);
        }
        AUDIO_MAINMENUMUSIC.setCycleCount(AudioClip.INDEFINITE);
        AUDIO_MAINMENUMUSIC.play();
        vboxMainMenu.setVisible(true);
    }

    /**
     * Event handler for New Game button
     */
    @FXML
    void onNewGameBtnClicked(ActionEvent event) {
        hideMenus();
        vboxGameMode.setVisible(true);
    }

    /**
     * Event handler for Load Game Button
     */
    @FXML
    void onLoadGameBtnClicked(ActionEvent event) throws IOException {
        AUDIO_BUTTONCLICKED.play();
        var loader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
        var scene = new Scene(loader.load());
        GameWindow game = loader.getController();

        var stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setTitle("GameWindow");

        stageHandlers(stage, game);

        game.initialize(scene, true);
        AUDIO_MAINMENUMUSIC.stop();
    }

    /**
     * Event handler for Help Button
     */
    @FXML
    void onHelpBtnClicked(ActionEvent event) throws IOException {
        AUDIO_BUTTONCLICKED.play();
        var loader = new FXMLLoader(getClass().getResource("HelpWindow.fxml"));
        var scene = new Scene(loader.load());
        scene.getRoot().requestFocus();

        var stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Help");

        HelpWindow helpWindow = loader.getController();
        helpWindow.initialize();
    }

    /**
     * Event handler for Credits Button 
     */
    @FXML
    void onCreditsBtnClicked(ActionEvent event) throws IOException {
        AUDIO_BUTTONCLICKED.play();
        var loader = new FXMLLoader(getClass().getResource("CreditsWindow.fxml"));
        var scene = new Scene(loader.load());
        scene.getRoot().requestFocus();

        var stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Credits");

        CreditsWindow creditsWindow = loader.getController();
        creditsWindow.initialize();
    }

    /**
     * Event handler for Exit Button
     */
    @FXML
    void onExitBtnClicked(ActionEvent event) {
        AUDIO_BUTTONCLICKED.play();
        System.exit(0);
    }

    /**
     * Event handler for Endless Mode Button
     */
    @FXML
    void onEndlessModeBtnClicked(ActionEvent event) {
        //set game mode to endless mode 
        userChosenGameMode = StageTypes.ENDLESS;
        hideMenus();
        vboxDifficultySetting.setVisible(true);

    }

    /*
    @FXML 
    void onCampaignModeBtnClicked(ActionEvent event) {
        //set game mode to campaign mode
        //this button does nothing since campaign mode is not implemented yet
    }
    */

    /**
     * Event handler for Back button
     */
    @FXML 
    void onBackToMainMenuBtnClicked(ActionEvent event) {
        hideMenus();
        vboxMainMenu.setVisible(true);
    }

    /**
     * Event handler for Back button
     */
    @FXML
    void onEasyDifficultyBtnClicked(ActionEvent event) throws IOException {
        userChosenDifficulty = DifficultyTypes.EASY;
        hideMenus();
        vboxUserEntry.setVisible(true);
    }

    /**
     * Event handler for Normal Difficulty Button
     */
    @FXML
    void onNormalDifficultyBtnClicked(ActionEvent event) throws IOException {
        userChosenDifficulty = DifficultyTypes.NORMAL;
        hideMenus();
        vboxUserEntry.setVisible(true);
    }

    /**
     * Event handler for Hard Difficulty Button
     */
    @FXML 
    void onHardDifficultyBtnClicked(ActionEvent event) throws IOException {
        userChosenDifficulty = DifficultyTypes.HARD;
        hideMenus();
        vboxUserEntry.setVisible(true);
    }

    /**
     * Event handler for Back button
     */
    @FXML
    void onBackToGameModeBtnClicked(ActionEvent event) {
        hideMenus();
        vboxGameMode.setVisible(true);
    }

    /**
     * Event handler for Begin button
     */
    @FXML
    void onBeginButtonClicked(ActionEvent event) throws Exception {
        username = txtfieldUserName.getText();
        AUDIO_MAINMENUMUSIC.stop();
        hideMenus();
        vboxMainMenu.setVisible(true);
        var loader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
        var scene = new Scene(loader.load());
        GameWindow game = loader.getController();
        game.loadUserChoiceData(userChosenGameMode, userChosenDifficulty, username);
        // game.initialize(scene, false);

        var stage = new Stage();
        // stage.setOnCloseRequest(e->AUDIO_MAINMENUMUSIC.play());
        stage.setScene(scene);
        stage.show();
        stage.setTitle("GameWindow");

        stageHandlers(stage, game);

        game.initialize(scene, false);
    }

    /**
     * Event handler for Back Button
     */
    @FXML
    void onBackToDifficultyChoiceBtnClicked(ActionEvent event) {
        hideMenus();
        vboxDifficultySetting.setVisible(true);
    }

    /**
     * Helps hide the menu buttons when transitions states happen
     */
    void hideMenus() {
        AUDIO_BUTTONCLICKED.play();
        for (Node n : hboxMenuController.getChildren()) {
            n.setVisible(false);
        }
    }

    /**
     * Detects whether the opened GameWindow closes in order to play back the main menu music
     */
    private void stageHandlers(Stage stage, GameWindow game) {
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                AUDIO_MAINMENUMUSIC.play();
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                game.onExit();
                AUDIO_MAINMENUMUSIC.play();
            }
        });
    }

}
