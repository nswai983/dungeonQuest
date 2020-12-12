package model.GameObject;
import model.*;

/**
 * File: ProjectileAlien.java
 * Desc: Model Class, an Actor : projectile fired by Alien Ships
 *
 * @author  Jeremy Tan
 * @editor Nathan Swiam
 */

public class ProjectileAlien1 extends Actor {

    Actor owner; //not used, ignore

    /**
      * Constructor: calls the super constructor of Actor class and tells it's a Alien Projectile 1 type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public ProjectileAlien1(Coordinate spawnlocation) {
        super(ActorTypes.PROJECTILE1_ALIEN);
        hitpoints=1;
        speed=5;
        collisiondamage=1;
        location = spawnlocation;
    }

    /**
      * Overrided default constructor
      * @param N/A
      * @return N/A
     */
    public ProjectileAlien1() {
        super(ActorTypes.PROJECTILE1_ALIEN);
    }

    /**
      * movement behavior of Alien Projectile 1 : 
      * Travels in a linear path based off of defined characteristics
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        moveMe();
    }

    /**
      * spawning calculations of Alien Projectile 1 : spawns where it was shot from
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
        location.setY(location.getY()-54);
        location.setX(location.getX()+20);
    }

    //Not used, ignore
    public void setOwner(Actor owner) {
        this.owner = owner;
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
        String typeString = "1";
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
