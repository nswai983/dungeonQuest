package model.GameObject;

import model.*;

/**
 * File: ProjectilePlayer.java Desc: Model Class, an Actor : projectiles fired
 * by the Player's ship
 *
 * @author Jeremy Tan
 * @editor Nathan Swaim
 */

public class ProjectilePlayer extends Actor {

    //Instance Variable Decleartions 
    static int projectileoffsetX; //constant X offset value that helps center the projectile with respect to the player's ship
    static int projectileoffsetY; //constant Y offset value that helps center the projectile with respect to the player's ship
    static Player player = GameController.instance().getWorld().getPlayer(); //instance of player's ship

    /**
      * Constructor: calls the super constructor of Actor class and tells it's a Player projectile type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public ProjectilePlayer(Coordinate spawnLocation) {
        super(ActorTypes.PROJECTILE1_PLAYER);
        speed = 5;
        hitpoints = 1;
        collisiondamage = 1;
        location = spawnLocation;
    }

    /**
      * Overrided default constructor
      * @param N/A
      * @return N/A
     */
    public ProjectilePlayer() {
        super(ActorTypes.PROJECTILE1_PLAYER);
    }


    /**
      * movement behavior of Player Projectile : 
      * Travels in a linear path based off of defined characteristics
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        // implement behavior of movement here
        moveMe();
    }

    /**
      * spawning calculations of Player Projectile: spawns where it was shot from
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
        location.setY(location.getY() + 54);
        location.setX(location.getX() + 20);
    }

    /**
      * @param offsetValues the x and y offset values to be stored into this class; helps with centering of projectile
      * @return N/A
     */
    public static void setProjectileDisplayOffset(int xOffset, int yOffset) {
        projectileoffsetX = xOffset;
        projectileoffsetY = yOffset;
    }

    /**
      * Modifies this object when it's collided state is true
      * Decrements its own hitpoints according to actor's damage values
      * Also decrements the actor's hitpoints according to this object's damge value
      * Player can gain points w/ colliding with enemies
      * @param actor - the actor object that has collided with this actor
      * @return N/A
     */
    @Override
    public void collision(Actor actor) {
        if (!actor.getActorType().equals(ActorTypes.ARTIFACT)) {
            if (!actor.isDead()) {
                actor.setHitpoints(actor.getHitpoints() - collisiondamage);
                if (actor.isDead()) {
                    player.addPoints(actor); // adds collision points to player
                }
                actor.setCollided(true);
            }
        }
    }
}
