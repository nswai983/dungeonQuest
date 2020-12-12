import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * File: HelpWindow.java 
 * Desc: View Class, the window that contains all the lore and insructions related to the game 
 *
 * @author Jeremy Tan
 * @coauthor Nathan Swaim
 */

public class HelpWindow {

    @FXML VBox vboxController;
    @FXML VBox vboxInstructions;
    @FXML Button btnExit;

    @FXML Label lblInstruction0;
    @FXML Label lblInstruction1;
    @FXML Label lblInstruction2;
    @FXML Label lblInstruction3;
    @FXML Label lblInstruction4;
    @FXML Label lblInstruction5;

    @FXML ScrollPane scrollpaneInstructionsPane;

    @FXML
    void initialize() {
        vboxController.getStyleClass().add("backgroundcredits");
        scrollpaneInstructionsPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollpaneInstructionsPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setInstructionText();
    }

    @FXML
    void onExitBtnClicked(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    void setInstructionText() {
        lblInstruction0.setText("The year is 4040. Aliens have become humanity's greatest threat. In order\nto protect and defend the earth from invasion, the Supreme Chancellor\nof Humanity Abroad and Under Benevolence (SCHAUB) has united all nations\nto go forth and destroy the alien threat.\n\nYour mission is simple: destroy all aliens on contact. You will wade\nthrough space, dodging asteroids and shooting enemy ships, in hopes to end\nhumanity's great threat.\nMeanwhile, there are outlandish rumors that the alien hive mind can be\nreasoned with, but earth's greatest scientists and military personnel\nsee these rumors as nothing more than false hope. They warn against\nany attempts to make contact or make peace. Should you risk your life\nto make peace with the aliens by committing as little blood-shed as possible\nwhile attempting to communicate with them? Or will you carve your path of\nvictory through carnage and leave a trail alien corpses behind you? Take up\nyour console and defend your planet, for the fate of humanity lies in your\nhands!");
        lblInstruction1.setText("CONTROLS:\nMOVE UP: W\nMOVE LEFT: A\nMOVE DOWN: S\nMOVE RIGHT: D\nFIRE PROJECTILE: F\nMEGA BOMB: M\nPAUSE GAME: ESC");
        lblInstruction2.setText("RULES :" + "\n" + "Game Play begins after pressing 'New Game', choosing difficulty and entering a name." + "\n" + "The player is then presented with a spaceship that he/she can control. " + "\n" + "After a brief count down, the game will start and streams of alien enemy ships will appear continuously. " + "\n" + "Aliens will come towards the player from the bottom of the screen, and will fire at the player. " + "\n" + "The player can either kill the aliens or dodge them. " + "\n" + "The player will also have the opportunity to collect space artifacts to learn more about the alien species. " + "\n" + "The player survives by dodging enemy projectiles and enemy ships. " + "\n" + "Points are accumulated through either defeating as many enemy ships as possible or collecting as many artifacts as possible." + "\n" + "The game ends when the player dies.");
        lblInstruction3.setText("This is a standard alien cruiser. The cruiser automatically is more of a transport ship than a combat ship." + "\n" + "If the player goes in front of it, then it will fire laser beams. They take around 2 shots to kill in normal difficulty.");
        lblInstruction4.setText("This is a standard alien fighter. Alien fighters do not fire anything; instead, they are willing to sacrifice themselves for the Hive" + "\n" + "Once entering the scene, the fighters will take time to align themselves at the player." + "\n" + "After prepping their engines, they will charge straight at the player. They take around a single shot to kill in normal difficulty.");
        lblInstruction5.setText("There are rumors of a third type of alien ship, the mothership. Few things are known about motherships." + "\n" + "Scant intel collected about them reveal that they are armed with a laser beam weapon and are tough to kill.");
        for (Node n : vboxInstructions.getChildren()) {
            n.getStyleClass().add("helpdisplaylabel");
        }
    }


}
