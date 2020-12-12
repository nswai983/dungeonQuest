package model;

/**
 * File: World.java
 * Desc: Model class that holds data for the game world.
 *
 * @author  Jeremy Tan
 * @author  Nathan Swaim
 */

import java.util.*;
import java.util.stream.Stream;

import model.GameObject.*;

public class World {

    ArrayList<Actor> activeActors = new ArrayList<Actor>(); // list of actors active in the world
    ArrayList<Actor> removedActors = new ArrayList<Actor>(); // list of actors that need removed from the world

    Difficulty difficultyLevel; // difficulty for the world
    StageTypes gameMode; // what type of game the world is running

    boolean gameover; // tells if game is over
    StageTypes stage; // tells what stage the game is on

    boolean isLoadedGame = false; // tells if game is a new game or loaded game

    Player player = new Player(); // current player for game - Nathan Swaim
    Boolean cheat = false; // current status for cheat mode - Nathan Swaim
    int wave; // tells what wave level the game is on

    int xyoffset = 100; //determines how far below should actors spawn (to avoid the appearance of actors suddenly showing up on screen)

    final int gameTicksPerSecond = 60; // each 'tick' represents a second in the game world. Also means 60 fps

    EndlessMode endless = new EndlessMode(); //instance of the private class Endless Mode

    /**
      * Constructor
      * Instantiates instance variable
      * @param N/A
      * @return N/A
     */
    public World() {
        gameover = false;
    }

    /**
      * Instantiates instance variables based off of parameter
      * @param boundaries the GameWindow Pane's dimensions
      * @return N/A
     */
    public void setBoundries(int xMin, int xMax, int yMin, int yMax) {
        Actor.setStageBounds(xMin, xMax, yMin, yMax, xyoffset);
    }

    /**
      * Calls the player's object to move forward 
      * @param . N/A
      * @return N/A
     */
    public void playerMoveForward() {
        player.setKeyDirection("forward");
        player.updatePosition();
    }

    /**
      * Calls the player's object to move backward 
      * @param . N/A
      * @return N/A
     */
    public void playerMoveBackward() {
        player.setKeyDirection("backward");
        player.updatePosition();
    }

    /**
      * Calls the player's object to move left 
      * @param . N/A
      * @return N/A
     */
    public void playerMoveLeft() {
        player.setKeyDirection("left");
        player.updatePosition();
    }

    /**
      * Calls the player's object to move right 
      * @param . N/A
      * @return N/A
     */
    public void playerMoveRight() {
        player.setKeyDirection("right");
        player.updatePosition();
    }

    /**
      * Calls the player's object to fire its projectile
      * Stores the fired projectile in the array list of active actors 
      * @param . N/A
      * @return N/A
     */
    public void playerFireProjectile() {
        activeActors.add(player.fireProjectile());
    }

    /**
      * Calls the player's object to activate cheat mode 
      * @param . N/A
      * @return N/A
     */
    public void playerCheatModePressed() {
        toggleCheat();
    }

    /**
      * initialize game settings based on chosen selections
      * @param chosenGameMode either Endless or Campaign mode
      * @param playername the entered username from the player
      * @param chosenDifficulty the chosen difficulty from menu selection
      * @return N/A
     */
    public void beginGame(StageTypes chosenGameMode, String playername, DifficultyTypes chosenDifficulty) {
        switch (chosenGameMode) {
            case ENDLESS:
                player.setUsername(playername);
                gameMode = chosenGameMode;
                difficultyLevel = new Difficulty(chosenDifficulty);
                break;

            case LEVEL1:
                //
                break;

            case LEVEL2:
                //
                break;

            case LEVEL3:
                //
                break;
        }

    }

    /**
      * handles movement, exiting, and collision detection of each active actors
     */
    public void playActors() {
        for (Actor actor : activeActors) {
            if (actor.isDead()) {
                actor.setForceOutOfScreen(true);
            }
            actor.updatePosition();
            collisionHandling(actor);
        }
        player.collisionCooldown();
    }

    /**
      * checks for collisions and handles outcomes
     */
    public void collisionHandling(Actor actor) {
        // handle player collision
        player.checkCollision(actor);

        // handle player projectile
        if (actor instanceof ProjectilePlayer) {
            ProjectilePlayer playerprojectile = (ProjectilePlayer) actor;
            for (Actor nonprojectile : activeActors) {
                if (!(nonprojectile instanceof ProjectilePlayer)) {
                    playerprojectile.checkCollision(nonprojectile);
                    if (nonprojectile.getCollided() == true) {
                        // System.out.println("DEBUG: " + nonprojectile.getActorType() + " HIT BY PLAYER");
                        // player.hit(nonprojectile, playerprojectile);
                        nonprojectile.setCollidedSoundPlay(true);
                        nonprojectile.setCollided(false);
                        removedActors.add(playerprojectile);
                        // removedActors.add(nonprojectile); //TODO: is this redundant? the setForceOutOfScreen in line 106 seems to do this?
                        // playerprojectile.setForceOutOfScreen(true); // remove projectile from screen after hit
                        // nonprojectile.setForceOutOfScreen(true);
                    }
                }
            }
        }

        // handles enemy projectile
        // Suggestion to future-self: maybe apply collision rules to every actor
        if (actor instanceof ProjectileAlien1) {
            ProjectileAlien1 alienProjectile = (ProjectileAlien1) actor;
            for (Actor nonprojectile : activeActors) {
                if ((nonprojectile instanceof Asteroid) && (nonprojectile instanceof Player)){
                    alienProjectile.checkCollision(nonprojectile);
                    if (nonprojectile.getCollided() == true) {
                        // System.out.println("DEBUG: " + nonprojectile.getActorType() + " HIT BY PLAYER");
                        // player.hit(nonprojectile, alienProjectile);
                        // alienProjectile.collision(nonprojectile);
                        removedActors.add(alienProjectile);
                        nonprojectile.setCollided(false);
                        // alienProjectile.setForceOutOfScreen(true); // remove projectile from screen after hit
                        // nonprojectile.setForceOutOfScreen(true);
                    }
                }
            }
        }
    }

    /**
      * removes actors that go offscreen
     */
    public void exitActors() {
        for (Actor actor : activeActors) {
            if (actor.getLocation().getX() < (Actor.getStageXMin() - (2 * xyoffset))
                    || actor.getLocation().getX() > (Actor.getStageXMax() + (2 * xyoffset))
                    || actor.getLocation().getY() < (Actor.getStageYMin() - (2 * xyoffset))
                    || actor.getLocation().getY() > (Actor.getStageYMax() + (2 * xyoffset))) {
                removedActors.add(actor);
            }
            else if (actor.isDead()) {
                /*
                if ((actor instanceof AlienShipTier1) || (actor instanceof AlienShipTier2) || (actor instanceof AlienShipTier3)) {
                    lastKilledEnemy = actor;
                }
                */
                removedActors.add(actor);
            }
            /*
            else if (actor instanceof Artifact) {
                Artifact artifact = (Artifact) actor;
                if (artifact.isTimerUp()) {
                    removedActors.add(actor);
                }
            }
            */
        }

        // remove all exited actors
        for (Actor actor : removedActors) {
            if (activeActors.contains(actor)) {
                activeActors.remove(actor);
            }
        }
    }

    /**
      * runs one frame of the game
     */
    public void run() {
        if (player.isDead()) {
            setGameover(true);
            // gameOver();
            return;
        }

        // sets the game in motion
        playActors();
        spawnEnemyProjectiles();
        exitActors();

        // if not paused, then continue running the game
        // if dead, run the gameover scene
    }

    // author: Nathan Swaim
    // returns the current player
    public Player getPlayer() {
        return player;
    }

    // author: Nathan Swaim
    // toggles cheat mode and returns the current status of cheat mode
    public Boolean toggleCheat() {
        cheat = !cheat;
        return cheat;
    }

    // author: Nathan Swaim
    // returns current status of cheat mode without changing it
    public Boolean getCheat() {
        return cheat;
    }

    // getters and setters (autogenerated)

    public ArrayList<Actor> getActiveActors() {
        activeActors.removeIf(a -> a == null);
        return activeActors;
    }

    public void setActiveActors(ArrayList<Actor> activeActors) {
        this.activeActors = activeActors;
        activeActors.removeIf(a -> a == null);
    }

    // In my opinion, we don't need a setter for this variable
    public ArrayList<Actor> getRemovedActors() {
        return removedActors;
    }

    public DifficultyTypes getChosenDifficulty() {
        return difficultyLevel.chosenDifficulty;
    }

    public void setChosenDifficulty(DifficultyTypes chosenDifficulty) {
        difficultyLevel.setChosenDifficulty(chosenDifficulty);
    }

    public Difficulty getDifficultyLevel() {
        return difficultyLevel;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public boolean isLoadedGame() {
        return isLoadedGame;
    }

    public void setLoadedGame(boolean isLoadedGame) {
        this.isLoadedGame = isLoadedGame;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCheat(Boolean cheat) {
        this.cheat = cheat;
    }

    public int getGameTicksPerSecond() {
        return gameTicksPerSecond;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public void setStageType(int wave) {
        switch (wave) {
            case 0: {
                stage = StageTypes.ENDLESS;
                break;
            }
            case 1: {
                stage = StageTypes.LEVEL1;
                break;
            }
            case 2: {
                stage = StageTypes.LEVEL2;
                break;
            }
            case 3: {
                stage = StageTypes.LEVEL3;
                break;
            }
        }
    }

    public StageTypes getStageType() {
        return stage;
    }

    // spawns actors for endless mode
    public void spawnActors() {
        endless.spawnAliensTier1();
        endless.spawnAliensTier2();
        endless.spawnAliensTier3();
        endless.spawnArtifacts();
        endless.spawnAsteroids();
    }

    // spawns enemy projectiles for the game
    void spawnEnemyProjectiles() {

        for (Actor enemy : activeActors) {
            if (enemy instanceof AlienShipTier1) {
                AlienShipTier1 enemyAlienT1 = (AlienShipTier1) enemy;
                ProjectileAlien1 alienprojectile = new ProjectileAlien1(new Coordinate(0, 0));
                alienprojectile = enemyAlienT1.fireProjectile();
                if (alienprojectile != null) {
                    activeActors.add(alienprojectile);
                }
            }
            if (enemy instanceof AlienShipTier3) {
                AlienShipTier3 enemyAlienT3 = (AlienShipTier3) enemy;
                ProjectileAlien2 alienprojectile = enemyAlienT3.fireProjectile();
                if (alienprojectile != null) {
                    activeActors.add(alienprojectile);
                }
            }
        }
    }

    // adjust actor hitpoints and speed based on difficulty level
    public void adjustActorDifficulty(Actor a) {
        a.initializeHitpoints((int) (a.getHitpoints() * difficultyLevel.getEnemyHealthMult()));
        a.setSpeed(a.getSpeed() * difficultyLevel.getEnemySpeedMult());
    }


    //Private class that handles the Endless Mode 
    private class EndlessMode {

        /**
        * handles the overall spawning process: spawning, overlap prevention, then confirming spawnings 
        * @param difficultyLevel the difficulty object that contains all spawning information
        * @param actortype the actortype to be spawned
        * @return N/A
        */
        void spawnSpecifiedActor(Difficulty difficultyLevel, ActorTypes actortype) {
            List<Actor> spawnedActorThisRound = spawnActorsProcess(difficultyLevel, actortype);

            while (checkForOverlap(spawnedActorThisRound)<(xyoffset*2)) {
                spawnedActorThisRound.clear();
                spawnedActorThisRound = spawnActorsProcess(difficultyLevel, actortype);
            }
            for (Actor actor : spawnedActorThisRound) {
                activeActors.add(actor);
            }
            spawnedActorThisRound.clear();
        }

        /**
        * Spawns multiple enemies in a single wave based on chosen difficulty and actortype 
        * @param difficultyLevel the difficulty object that contains all spawning information
        * @param actortype the actortype to be spawned
        * @return N/A
        */
        List<Actor> spawnActorsProcess(Difficulty difficultyLevel, ActorTypes actortype) {
            Random rand = new Random();
            List<Actor> spawnedActorThisRound = new ArrayList<Actor>();
            int numEnemies = 0;
            if (difficultyLevel.getSpawnRateForActor(actortype)!=0) {
            numEnemies = rand.nextInt(difficultyLevel.getSpawnRateForActor(actortype)+1);
            }

            for (int i = 0; i < numEnemies; i++) {
                Actor actor = new Artifact(); // to instantiate object
                if (actortype == ActorTypes.ALIENSHIP1) {
                    actor = new AlienShipTier1();
                }
                else if (actortype == ActorTypes.ALIENSHIP2) {
                    actor = new AlienShipTier2();
                }
                else if (actortype == ActorTypes.ALIENSHIP3) {
                    actor = new AlienShipTier3();
                }
                else if (actortype == ActorTypes.ASTEROID) {
                    actor = new Asteroid();
                } else if (actortype == ActorTypes.ARTIFACT) {
                    actor = new Artifact();
                }
                adjustActorDifficulty(actor);
                actor.spawnMe();
                spawnedActorThisRound.add(actor);
            }
            return spawnedActorThisRound;
        }

        /**
        * Returns the minimum length that occured between the two closest actors
        * @param spawnedActorForRound a placeholder list that contains spawned enemies
        * @return N/A
        */
        double checkForOverlap(List<Actor> spawnedActorForRound) {
            double min=(xyoffset*2);
            for (int j=0; j<spawnedActorForRound.size(); j++) {
                for (int k=j+1; k<spawnedActorForRound.size(); k++) {

                    if (spawnedActorForRound.get(j).getActorType()==ActorTypes.ALIENSHIP1) {
                        double x1 = spawnedActorForRound.get(j).getLocation().getX();
                        double x2= spawnedActorForRound.get(k).getLocation().getX();
                        double tempmin = Math.abs(x1-x2);
                        if (tempmin<=min) {
                            min = tempmin;
                        }
                    }
                    else if (spawnedActorForRound.get(j).getActorType()==ActorTypes.ASTEROID) {
                        double y1 = spawnedActorForRound.get(j).getLocation().getY();
                        double y2 = spawnedActorForRound.get(k).getLocation().getY();
                        double tempmin = Math.abs(y1-y2);
                        if (tempmin<=min) {
                            min = tempmin;
                        }
                    }
                }
            }
            return min;

        }

        //spawn Tier 1 enemies
        void spawnAliensTier1() {
            spawnSpecifiedActor(difficultyLevel, ActorTypes.ALIENSHIP1);
        }

        //spawn Tier 2 enemies
        void spawnAliensTier2() {
            spawnSpecifiedActor(difficultyLevel, ActorTypes.ALIENSHIP2);
        }

        //spawn Tier 3 enemies
        void spawnAliensTier3() {
            spawnSpecifiedActor(difficultyLevel, ActorTypes.ALIENSHIP3);
        }

        //spawn Asteroids
        void spawnAsteroids() {
            spawnSpecifiedActor(difficultyLevel, ActorTypes.ASTEROID);
        }

        //spawn Asteroids
        void spawnArtifacts() {
            spawnSpecifiedActor(difficultyLevel, ActorTypes.ARTIFACT);
        }

    }

    //SETTER AND GETTERS

    public void setRemovedActors(ArrayList<Actor> removedActors) {
        this.removedActors = removedActors;
    }

    public void setDifficultyLevel(Difficulty difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public StageTypes getGameMode() {
        return gameMode;
    }

    public void setGameMode(StageTypes gameMode) {
        this.gameMode = gameMode;
    }

    public StageTypes getStage() {
        return stage;
    }

    public void setStage(StageTypes stage) {
        this.stage = stage;
    }

    public int getXYoffset() {
        return xyoffset;
    }

    public void setXYoffset(int xyoffset) {
        this.xyoffset = xyoffset;
    }

    public EndlessMode getEndless() {
        return endless;
    }

    public void setEndless(EndlessMode endless) {
        this.endless = endless;
    }

    // counts the number of specified actors in activeActors
    public int count(ActorTypes type) {
        int count = 0;
        for (Actor actor : activeActors) {
            if (actor.getActorType().equals(type)) {
                count++;
            }
        }
        return count;
    }

    // returns a list of specified actors from activeActors
    public Object[] getSpecificActors(ActorTypes type) {
        List<Actor> specificActors = activeActors;
        final ActorTypes t = type;
        return specificActors.stream().filter( a -> a.getActorType().equals(t)).toArray();
    }
}
