package commandPattern;
import model.GameObject.*;

public class MoveCommand extends WorldCommand {

    private Player player;
    private String direction;

    // constructor
    public MoveCommand(Player player, String direction) {
        this.player = player;
        this.direction = direction;
    }

    @Override
    public void execute() {
        switch(this.direction) {
            case "left": {
                // method to move player
                break;
            }
            case "right": {
                // method to move player
                break;
            }
            case "up": {
                // method to move player
                break;
            }
            case "down": {
                // method to move player
                break;
            }
        }
    }
}
