import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * File: EasterEggWindow.java 
 * Desc: View Class, the window that pops up when the player discovers an easter egg
 *
 * @author Nathan Swaim
 */

public class EasterEggWindow {

    @FXML
    VBox vboxController; // main controller for screen
    @FXML
    VBox vboxEasterEggBox; // center vBox for message and scroll pane
    @FXML
    Button btnExit; // exit button

    @FXML
    Label lblScrollpane; // scroll pane for message

    // easter egg message
    String easterEggMessage = "Congratulations! You have found a secret stash of life points!\n\nBe sure to use these life points valuably.\n\nAlso, it seems that the aliens do posses some sort of\nintelligent life. Proceed with caution out there...";

    @FXML
    void initialize() {
        // initializes window
        vboxController.getStyleClass().add("backgroundcredits");
        lblScrollpane.setText(easterEggMessage);
    }

    @FXML
    // closes stage when exit button is clicked
    void onExitBtnClicked(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

}
