import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * File: CreditsWindow.java
 * Desc: View Class, the window that contains all the credits and citations
 *
 * @author Jeremy Tan
 */


public class CreditsWindow {

    // View variable (GUI Controls) declerations

    @FXML VBox vboxController;
    @FXML VBox vboxCreditsBox;
    @FXML Button btnExit;

    @FXML ScrollPane scrollpaneCreditsScroll;
    @FXML VBox vboxCreditsDisplay;
    @FXML Label lblCredits0;
    @FXML Label lblCredits1;
    @FXML Label lblCredits2;
    @FXML Label lblCredits3;
    @FXML Label lblCredits4;
    @FXML Label lblCredits5;
    @FXML Label lblCredits6;
    @FXML Label lblCredits7;
    @FXML Label lblCredits8;
    @FXML Label lblCredits9;
    @FXML Label lblCredits10;
    @FXML Label lblCredits11;

    /*
    * defualt run method of CreditsWindow
    */
    @FXML
    void initialize() {
        vboxController.getStyleClass().add("backgroundcredits");
        scrollpaneCreditsScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollpaneCreditsScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setInstructionText();
    }

    /**
     * Event handler for Exit Button
     */
    @FXML
    void onExitBtnClicked(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets each blank label to the appropriate text
     */
    void setInstructionText() {
        lblCredits0.setText("Main-Menu Background Art: " + "\n" + "https://besthqwallpapers.com/space/planet-earth-space-sun-earth-9648");
        lblCredits1.setText("Main-Menu Music: " + "\n" + "Ender's War (Ender's Game)");
        lblCredits2.setText("Credits-Window & Help-Window Background:" + "\n" + "Meghan Prendergast https://www.pinterest.com/pin/328551735286971250/");
        lblCredits4.setText("Game-Window Background: " + "\n" + "https://wallpapersko.com/4k-space-wallpaper.html");
        lblCredits5.setText("Game-Window Music: " + "\n" + "Final Test (Ender's Game)");
        lblCredits3.setText("Game-Over Music: " + "\n" + "Game Over (Ender's Game)");
        lblCredits6.setText("Alien Ship and Laser art:" + "\n" + "Graham Chambers MShadowy Shadowyards Faction (Starsector Modding Community)");
        lblCredits7.setText("Player Ship and Laser Art:" + "\n" + "J0hn Shm0 COPS Faction (Starsector Modding Community)");
        lblCredits8.setText("Artifact art: " + "\n" + "Kenney Vleugels : Space Shooter Redux");
        lblCredits9.setText("In Game Sound effects: " + "\n" + "Kenney Vleugels : SciFi Sounds");
        lblCredits10.setText("Mega Bomb sound effects: " + "\n" + "https://freesound.org/people/EFlexMusic/sounds/393374/");
        lblCredits11.setText("Programming: " + "\n" + "Nathan Swaim and Jeremy Tan");

        for (Node n : vboxCreditsDisplay.getChildren()) {
            n.getStyleClass().add("helpdisplaylabel");
        }
    }

}
