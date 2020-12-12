package model.GameObject;
import model.*;

/**
 * File: ProjectileAlien.java
 * Desc: Model Class, an Actor : projectile fired by Alien Ships
 * 
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

public class ProjectileAlien2 extends Actor {

    //Instance Variable Declerations
    Actor owner; //not used, ignore
    double heightStretch; //the height stretch value of the image when it spawns on the screen. 
                          //Should be the distance between it and the player.
    int lifespanTime; //defines how long this beam stays on the screen
    int timer; //records the time passed since this projectile first appeared on the screen

    /**
      * Constructor: calls the super constructor of Actor class and tells it's a Alien Projectile 2 type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public ProjectileAlien2(Coordinate spawnlocation, int lifespan, int direction, Coordinate playerLocation) {
        super(ActorTypes.PROJECTILE2_ALIEN);
        hitpoints=100;
        speed=0;
        collisiondamage=1;
        setDirection(direction);
        lifespanTime=lifespan;
        location=spawnlocation;

        heightStretch = Math.sqrt(Math.pow(location.getX()-playerLocation.getX(),2)+Math.pow(location.getY()-playerLocation.getY(),2));
        heightStretch = heightStretch*2;
        location.setX((playerLocation.getX()+location.getX())/2);
        location.setY((playerLocation.getY()+location.getY())/2);

    }

    /**
      * Overrided default constructor
      * @param N/A
      * @return N/A
     */
    public ProjectileAlien2() {
        super(ActorTypes.PROJECTILE2_ALIEN);
    }

    /**
      * movement behavior of Alien Projectile 2 : 
      * It stays stationary
      * If it exceeds the time limit of staying on the sreen,
      * then set its hitpoints to 0 to despawn
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        timer++;
        if (timer>=lifespanTime) {
            timer=lifespanTime;
            hitpoints=0;
        }
    }

    /**
      * spawning calculations of Alien Projectile 1 : spawns where it was shot from
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
        location.setY(location.getY()-100);
        location.setX(location.getX()+20);
    }

    //not used, ignore
    public void setOwner(Actor owner) {
        this.owner = owner;
    }

    //returns the distance between where it was fired from and the player's location at that time
    public double getHeightStretch() {
        return heightStretch;
    }

    /**
      * Modifies this object when it's collided state is true
      * Decrements its own hitpoints according to actor's damage values
      * Also decrements the actor's hitpoints according to this object's damge value
      * @param actor - the actor object that has collided with this actor
      * @return N/A
     */
    @Override
    public void collision(Actor actor) {
        if (!actor.getActorType().equals(ActorTypes.ARTIFACT)) {
            if (!actor.getCollided()) {
                actor.setHitpoints(actor.getHitpoints() - collisiondamage);
                // if (actor.isDead()) {
                //     GameController.instance().getWorld().getRemovedActors().add(actor);
                // }
                actor.setCollided(true);
                // System.out.println("Enemy Hit");
            }
        }
    }
    
    /**
      * @author Nathan Swaim
      * serilization method of this class
      * Format: ID,TYPE,X,Y,HEAD,SPEED
      * @param N/A
      * @return actor data in a String 
     */
    @Override
    public String toString() {
        String idString = String.valueOf(getID());
        String typeString = "2";
        String xString = String.valueOf((int) location.getX());
        String yString = String.valueOf((int) location.getY());
        String headingString = String.valueOf((int) location.getHeading());
        // String speedString = String.valueOf((int) (speed * 100));

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
        String[] data = { idString, typeString, xString, yString, headingString };

        String value = "";
        for (String str : data) {
            value += new String(str + ",");
        }
        return value.substring(0, value.length() - 1); // return value minus the last ","
    }
}
