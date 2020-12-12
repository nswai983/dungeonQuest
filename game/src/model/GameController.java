//-----------------------------------------------------------
//File:   GameController.java
//Desc:   Model Class that handles the Game Control functionality,
//        such as saving, loading, and accessing the game via a
//        singleton implementation.
//-----------------------------------------------------------

package model;

import java.util.logging.FileHandler;

import model.GameObject.*;

/**
 * File: GameController.java
 * Desc: Model Class that handles the Game Control functionality,
 *       such as saving, loading, and accessing the game via a
 *       singleton implementation.
 *
 * @author  Nathan Swaim
 */

public class GameController {

    private World world; // instance variable containing a world object
    static final String GAMESTATE_FILENAME = "gameState.dat"; // save file name instance variable
    String error; // current error code

    // loads the gameState.txt file, extracts the data, then loads data into the model
    public boolean load(String filename) {

        // check for file; if none, return
        if (filename.equals("") || filename.equals(null)) {
            return false;
        }

        // upon success, start game
        try {
            SerializationHandler.load(filename, world);
            world.setLoadedGame(true);
            for (Actor a : world.getActiveActors()) {
                world.adjustActorDifficulty(a);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e); // print out error
            return false;
        }

    }

    // overwrites the gameState.dat file with current model data at savepoint in a
    // predefined format
    public boolean save(String filename) {
        try {
            SerializationHandler.save(filename, world);
            reset();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // returns the instantiated world
    public World getWorld() {
        return world;
    }

    // Singleton implementation

    // prevent direct instantiation outside this class
    private GameController() {
        world = new World();
    }

    private static GameController instance = new GameController(); // private instance of game controller

    // returns instance of Game Controller
    public static GameController instance() {
        return instance;
    }

    // Resets the game; resets the world (user must save first to keep any data)
    public static void reset() {
        instance = new GameController();
    }


    // GETTERS AND SETTERS

    public void setWorld(World world) {
        this.world = world;
    }

    public static String getGamestateFilename() {
        return GAMESTATE_FILENAME;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public static GameController getInstance() {
        return instance;
    }

    public static void setInstance(GameController instance) {
        GameController.instance = instance;
    }
}
