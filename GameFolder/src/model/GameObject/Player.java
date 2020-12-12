/**
 * File: Player.java
 * Desc: Model Class for the Player's ship
 *
 * @author  Jeremy Tan
 * @author  Nathan Swaim
 */

package model.GameObject;

import model.*;

// player class holds the player object for main player in game
public class Player extends Actor {

    // instance variables unique to Player's ship
    String username = "Default"; // username of player
    int distanceTraveled; // how far the player has traveled in the game (used for campaign mode)
    String inputdirection = ""; // direction to move player
    int reloadSpeedInTicks = 20; // reload speed for weapons
    boolean megaBombUsed = false; // tells if mega bomb has been used

    public final int ALIENTIER1WORTH = 100; // point value of type 1 aliens
    public final int ALIENTIER2WORTH = 250; // point value of type 2 aliens
    public final int ALIENTIER3WORTH = 500; // point value of type 3 aliens
    public final int ARTIFACTVALUE = 300; // point value of artifacts

    int totalalienskilled; // how many aliens the player has killed
    int artifactscollected; // how many artifacts the player has collected
    int totalpoints; // how many points the player has accumulated

    // player constructor
    public Player() {
        super(ActorTypes.PLAYER);
        hitpoints = 10;
        speed = 15;
        directionProperty.set(90);
    }

    // binds player's ship so it cannot move past the game window
    public void bindPlayerShip() {
        if (location.getX() <= stageXMin) {
            location.setX(stageXMin);
        } else if (location.getX() >= stageXMax) {
            location.setX(stageXMax);
        } else if (location.getY() <= stageYMin) {
            location.setY(stageYMin);
        } else if (location.getY() >= stageYMax) {
            location.setY(stageYMax);
        }
    }

    // updates player position
    public void updatePosition() {

        distanceTraveled++;

        if (inputdirection != null) {
            switch (inputdirection.toLowerCase()) {
                case ("forward"): {
                    location.setY(location.getY() - speed);
                    bindPlayerShip();
                    break;
                }
                case ("backward"): {
                    location.setY(location.getY() + speed);
                    bindPlayerShip();
                    break;
                }
                case ("left"): {
                    location.setX(location.getX() - speed);
                    bindPlayerShip();
                    break;
                }
                case ("right"): {
                    location.setX(location.getX() + speed);
                    bindPlayerShip();
                    break;
                }
            }
        }
    }

    @Override
    // overrides base collision method to work with various types of Actors
    // checks collision status, cheat mode status, adds points if needed, and removes actors from screen if needed
    public void collision(Actor actor) {
        if (!actor.getActorType().equals(ActorTypes.ARTIFACT)
                && !actor.getActorType().equals(ActorTypes.PROJECTILE1_PLAYER)) {
            if (!actor.isDead()) {
                if (collisionTimer == 100) {
                    collisionTimer = collisionTimer - 1;
                    if (!GameController.instance().getWorld().getCheat()) {
                        // if cheat mode is not enabled, player loses one hitpoint
                        hitpoints = hitpoints - actor.collisiondamage;
                    }
                    setCollided(true);
                    actor.setHitpoints(actor.getHitpoints() - 1);
                }
                if (actor.isDead()) {
                    addPoints(actor); // adds collision points to player
                }
                actor.setCollided(true); // set actor collision variable as true
            }
        } else if (actor.getActorType().equals(ActorTypes.ARTIFACT)) {
            Artifact artifact = (Artifact) actor;
            // if artifact is not collected, collect it
            if (!artifact.isCollected()) {
                addPoints(artifact);
                artifact.setCollected(true);
                actor.setCollided(true);
                GameController.instance().getWorld().getRemovedActors().add(artifact);
            }
        }
    }

    // returns true if player is still alive
    boolean isAlive() {
        return (hitpoints > 0);
    }

    // fires player projectile and returns the newly created projectile
    public ProjectilePlayer fireProjectile() {
        if (cooldowntimer >= reloadSpeedInTicks) {
            Coordinate projectileCord = new Coordinate(location.getX(), location.getY());
            ProjectilePlayer projectile = new ProjectilePlayer(projectileCord);
            projectile.setDirection(directionProperty.get());
            projectile.spawnMe();

            cooldowntimer = 0;
            return projectile;
        }
        return null;
    }

    // author: Nathan Swaim
    // clears the screen of all enemies and adds point values to player
    // can only be used once per game
    public void useMegaBomb() {

        if (megaBombUsed) {
            return;
        }

        for (Actor actor : GameController.instance().getWorld().getActiveActors()) {
            if (!actor.getActorType().equals(ActorTypes.PLAYER) && !actor.getActorType().equals(ActorTypes.ASTEROID)
                    && !actor.getActorType().equals(ActorTypes.ARTIFACT)) {
                actor.setHitpoints(0);
                if (actor.isDead()) {
                    addPoints(actor); // adds collision points to player
                }
            }
        }
        megaBombUsed = true;
    }

    // takes in an actor and adds points to player depending on the type of actor
    void addPoints(Actor actor) {
        if (actor == null) {
            return;
        }
        ActorTypes type = actor.getActorType();
        switch (type) {
            case ALIENSHIP1:
                totalpoints += ALIENTIER1WORTH;
                totalalienskilled++;
                // System.out.println("Alien Killed: 1");
                break;
            case ALIENSHIP2:
                totalpoints += ALIENTIER2WORTH;
                totalalienskilled++;
                // System.out.println("Alien Killed: 2");
                break;
            case ALIENSHIP3:
                totalpoints += ALIENTIER3WORTH;
                totalalienskilled++;
                // System.out.println("Alien Killed: 3");
                break;
            case ARTIFACT:
                totalpoints += ARTIFACTVALUE;
                artifactscollected++;
                // System.out.println("Artifact Collected");
                break;
        }
    }

    // author: Nathan Swaim
    // takes in a serialized string and converts it to data for an Player object
    // Serialization Format: ID,TYPE,DIST,SPEED,X,Y,HEAD,LIVES,E_KILL,A_COLL
    public void deserialize(String serializedString) {

        String[] deserializeData = serializedString.split(",");
        setID(Integer.parseInt(deserializeData[0])); // set ID number
        setDistanceTraveled(Integer.parseInt(deserializeData[2]));
        setSpeed(Integer.parseInt(deserializeData[3]) / 100);
        Coordinate coord = new Coordinate(Integer.parseInt(deserializeData[4]), Integer.parseInt(deserializeData[5]));
        coord.setHeading(Integer.parseInt(deserializeData[6]));
        setLocation(coord); // set coordinate for x, y, and heading
        setHitpoints(Integer.parseInt(deserializeData[7]));
        setTotalAliensKilled(Integer.parseInt(deserializeData[8]));
        setTotalArtifactsCollected(Integer.parseInt(deserializeData[9]));

        if (deserializeData[10].equals("1")) {
            megaBombUsed = true;
        }

    }

    @Override
    // overrides base toString() method to get all player serialization data
    public String toString() {

        String idString = String.valueOf(getID());
        String typeString = "1";
        String distanceTraveledString = String.valueOf(distanceTraveled);
        String speedString = String.valueOf((int) (speed * 100));
        String xString = String.valueOf((int) location.X.get());
        String yString = String.valueOf((int) location.Y.get());
        String headingString = String.valueOf((int) location.getHeading());
        String hitpointsString = String.valueOf(hitpoints);
        String enemiesKilledString = String.valueOf(totalalienskilled);
        String artifactsCollectedString = String.valueOf(artifactscollected);
        String megaBombUsedString = "0";
        if (megaBombUsed) {
            megaBombUsedString = "1";
        }

        // convert ID number to proper format
        switch (idString.length()) {
            case 1: {
                idString = "00" + idString;
                break;
            }
            case 2: {
                idString = "0" + idString;
                break;
            }
        }

        // load all data into an array
        String[] data = { idString, typeString, distanceTraveledString, speedString, xString, yString, headingString,
                hitpointsString, enemiesKilledString, artifactsCollectedString, megaBombUsedString };

        String value = "";
        for (String str : data) {
            value += new String(str + ",");
        }
        return value.substring(0, value.length() - 1); // return value minus the last ","

    }

    // GETTERS AND SETTERS

    public int getTotalPoints(Difficulty difficultyLevel) {
        int points = (int) (totalpoints * difficultyLevel.getPointsMult());
        return points;
    }

    public void setTotalPoints(int totalpoints) {
        this.totalpoints = totalpoints;
    }

    public int getTotalAliensKilled() {
        return totalalienskilled;
    }

    public void setTotalAliensKilled(int totalalienskilled) {
        this.totalalienskilled = totalalienskilled;
    }

    public int getTotalArtifactsCollected() {
        return artifactscollected;
    }

    public void setTotalArtifactsCollected(int artifactscollected) {
        this.artifactscollected = artifactscollected;
    }

    public void setKeyDirection(String inputdirection) {
        this.inputdirection = inputdirection;
    }

    public void incrementGunCooldownTimer() {
        cooldowntimer++;
        if (cooldowntimer >= reloadSpeedInTicks) {
            cooldowntimer = reloadSpeedInTicks;
        }
    }

    public int getDist() {
        return distanceTraveled;
    }

    public int getHealth() {
        return hitpoints;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(int distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public boolean isMegaBombUsed() {
        return megaBombUsed;
    }

}
