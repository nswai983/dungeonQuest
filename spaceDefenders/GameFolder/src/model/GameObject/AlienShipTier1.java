/**
 * File: AlienShipTier1.java
 * Desc: Model Class, an Actor : Tier 1 Alien Ship
 *
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

package model.GameObject;
import model.*;

public class AlienShipTier1 extends Actor {

    //Instance Variable Declerations
    int reloadSpeedInTicks = 40; //weapon reload speed
    int cooldowntimer=0; //records the amount of time passed since firing of projectile

    /**
      * Constructor: calls the super constructor of Actor class and tells it's a Tier 1 Alien ship type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public AlienShipTier1() {
        super(ActorTypes.ALIENSHIP1);
        hitpoints=2;
        collisiondamage=1;
        directionProperty.set(-90);
        speed=1.0;
    }

    /**
      * movement behavior of Alien Tier 1 Ship : just moves upward to the screen 
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        moveMe();
        cooldowntimer++;
        if (cooldowntimer>=reloadSpeedInTicks) {
            cooldowntimer=reloadSpeedInTicks;
        }
    }

    /**
      * spawning calculations of Alien Tier 1 : spawns randomly at the bottom edge of the screen
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
            int randPosX = (int) (Math.random()*(stageXMax - stageXMin + 1 )) + stageXMin;
            location.X.set(randPosX);
            location.Y.set(stageYMax+stageOffset);
    }

    /**
      * calculates when Alien Tier 1 should fire projectile : only if it gets within its line of sight will it fire
      * @param N/A
      * @return N/A
     */
    public boolean firingBehavior() {
        Player player = GameController.instance().getWorld().getPlayer();
        if ((cooldowntimer>=reloadSpeedInTicks) && (Math.abs(location.getX()-player.getLocation().getX())<=50) && (location.getY()>=player.getLocation().getY())) {
            cooldowntimer=0;
            return true;
        }
        return false;
    }

    /**
      * fires the alien projectile assigned to this alien ship
      * @param N/A
      * @return ProjectileAlien1 returns the fired projectile object
     */
    public ProjectileAlien1 fireProjectile() {
        if (firingBehavior()==true) {
        Coordinate projectileCord = new Coordinate(location.getX(),location.getY());
        ProjectileAlien1 projectile = new ProjectileAlien1(projectileCord);
        projectile.setDirection(directionProperty.get());
        projectile.spawnMe();

        //Swaim
        // GameController.instance().getWorld().getProjectiles().add(projectile);
        return projectile;
        }
        return null;
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

    // do not need toString() or deserialize() methods if alien class is in place
}
