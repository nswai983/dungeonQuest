/**
 * File: AlienShipTier3.java
 * Desc: Model Class, an Actor : Tier 3 Alien Ship
 *
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

package model.GameObject;

import java.util.Random;

import model.*;

public class AlienShipTier3 extends Actor{

    //Instance Variable Declerations
    int boundaryDistance; //the horizontal distance needed by this alien ship to travel. 
                          //After this value is reached, it stops and transitions 
                          //to its next state of behavior
    int stayTime; //the amount of time it stays in the stationary firing behavior.
    int stationarytimer; //the timer the tracks the amount of time passed while in the stationary behavior
    int reloadSpeedInTicks = 400; //weapon reload speed
    int cooldowntimer=0; //records the amount of time passed since firing of projectile

    boolean onLeftSide; //true if spawned on left side of screen
    boolean onRightSide; //true if spawned on right side of screen

    boolean stateStationary; //true if it entered into the stationary state
    boolean stateEnter; //true if it entered the enter state
    boolean stateLeave; //true if it entered the enter state
    double xInitialPos; //Initial spawn point in X value. Helps in facilitating when to 
                        //transition from enter state to stationary state
    double slideYDistance; //Not used, ignore
    boolean slideUp; //Not used, ignore

    ProjectileAlien2 lastFired; //Stores the projectile of the last fired projectile of this ship.

    /**
      * Constructor: calls the super constructor of Actor class and tells it's a Tier 3 Alien ship type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public AlienShipTier3() {
        super(ActorTypes.ALIENSHIP3);
        hitpoints=10;
        collisiondamage=1;
        speed=0.25;
        Random rand = new Random();
        boundaryDistance=rand.nextInt(200)+200;
        stayTime=rand.nextInt(1000)+800;
    }

    /**
      * movement behavior of Alien Tier 3 Ship : 
      * The ship will travel a short horizontal distance before pausing.
      * Then it will transition to the stationary state where it adjusts its orientation towards the player
      * In this stationary state, the enemy will attempt to line up its beam weapon respective of the player's position
      * When firing, this actor cannot move
      * After some time, it will transition to the exit state, where it will travel veritcally towards the upper edge of the screen
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        //implement behavior of movement here
        if (stateEnter==true) {
            if (Math.abs(location.getX()-xInitialPos)<boundaryDistance) {
                moveMe();
            }
            else {
                stateEnter=false;
                stateStationary=true;
            }
        }
        else if (stateStationary==true) {
            cooldowntimer++;
            stationarytimer++;

            if (directionProperty.get()<getDirectionToPlayer()) {
                if (lastFired==null || lastFired.isDead()) {
                    directionProperty.set(directionProperty.get()+4);
                }
            }
            else if (directionProperty.get()>getDirectionToPlayer()) {
                if (lastFired==null || lastFired.isDead()) {
                    directionProperty.set(directionProperty.get()-4);
                }
            }

            if (stationarytimer>=stayTime) {
                if (lastFired==null || lastFired.isDead()) {
                stateStationary=false;
                stateLeave=true;
                }
            }

            /*
            else {
                if (slideUp==true) {
                    if ((Math.abs(location.getY()-stageYMin)<=50)) {
                        slideUp=false;
                    }
                    else {
                        location.setY(location.getY()-speed);
                    }
                }
                else {
                    if ((Math.abs(location.getY()-stageYMax)<=50) ) {
                        slideUp=true;
                    }
                    else {
                        location.setY(location.getY()+speed);
                    }
                }
            }
            */
        }
        else if (stateLeave==true) {
            if (onLeftSide=true) {
                if (directionProperty.get()>-90) {
                    directionProperty.set(directionProperty.get()-1);
                }
            }
            else if (onRightSide=true) {
                if (directionProperty.get()<270) {
                    directionProperty.set(directionProperty.get()+1);
                }
            }
            moveMe();
        }
    }

    /**
      * spawning calculations of Alien Tier 2 : spawns randomly at the side edges of the screen
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
        stateEnter=true;
        Random rand = new Random();
        int chooser = rand.nextInt(2);
            int randPosX;
            int randPosY = rand.nextInt(stageYMax/3)+(stageYMax/4);
            if (chooser == 0) {
                directionProperty.set(0);
                randPosX=stageXMin-stageOffset;
                onLeftSide=true;
            }
            else {
                directionProperty.set(180);
                randPosX=stageXMax+stageOffset;
                onRightSide=true;
            }
        int slidingDirectionChooser = rand.nextInt(2);
        if (slidingDirectionChooser==0) {
            slideUp=true;
        }
        else {
            slideUp=false;
        }
            xInitialPos=randPosX;
            location.setX(randPosX);
            location.setY(randPosY);
            slideYDistance = rand.nextInt(stageYMax/3);
    }

    /**
      * calculates the direction in degrees that points toward the player
      * @param N/A
      * @return int direction in degrees
     */
    public int getDirectionToPlayer() {
        Player player = GameController.instance().getWorld().getPlayer();
        double xdiff = location.getX() - player.getLocation().getX();
        double ydiff = location.getY() - player.getLocation().getY();
        double rad = Math.atan(ydiff / xdiff);
        int directiontemp=0;

        if (ydiff > 0 && xdiff > 0) {
            directiontemp = (int) (rad * (180 / Math.PI) + 180);
        } else if (ydiff < 0 && xdiff > 0) {
            directiontemp = (int) (rad * (180 / Math.PI) + 180);
        } else if (ydiff > 0 && xdiff < 0) {
            directiontemp = (int) (rad * (180 / Math.PI));
        } else if (ydiff < 0 && xdiff < 0) {
            directiontemp = (int) (rad * (180 / Math.PI));
        } else if (location.getX() > player.getLocation().getX()) {
            directiontemp = 180;
        } else if (location.getX() < player.getLocation().getX()) {
            directiontemp = 0;
        } else if (location.getY() > player.getLocation().getY()) {
            directiontemp = -90;
        } else if (location.getY() < player.getLocation().getY()) {
            directiontemp = 90;
        }
        return directiontemp;
    }

    /**
      * calculates when Alien Tier 3 should fire projectile : only if it gets within its line of sight will it fire
      * @param N/A
      * @return N/A
     */
    public boolean firingBehavior() {
        if ((cooldowntimer>=reloadSpeedInTicks) && (Math.abs(directionProperty.get()-getDirectionToPlayer())<=40) && stateStationary) {
            cooldowntimer=0;
            return true;
        }
        return false;
    }

    /**
      * fires the alien projectile assigned to this alien ship
      * @param N/A
      * @return ProjectileAlien2 returns the fired projectile object
     */
    public ProjectileAlien2 fireProjectile() {
        if (firingBehavior()==true) {
        Coordinate projectileCord = new Coordinate(location.getX(),location.getY());
        Player player = GameController.instance().getWorld().getPlayer();
        ProjectileAlien2 projectile = new ProjectileAlien2(projectileCord, reloadSpeedInTicks-10, directionProperty.get(),player.getLocation());
        lastFired=projectile;
        projectile.setDirection(directionProperty.get());
        projectile.spawnMe();
        //Swaim
        // GameController.instance().getWorld().getProjectiles().add(projectile);
        return projectile;
        }
        return null;
    }

    /**
      * serilization method of this class
      * Format: ID,TYPE,X,Y,HEAD,SPEED
      * @param N/A
      * @return actor data in a String 
     */
    @Override
    public String toString() {
        String idString = String.valueOf(getID());
        String typeString = "3";
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
