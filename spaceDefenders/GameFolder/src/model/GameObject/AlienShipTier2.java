/**
 * File: AlienShipTier2.java
 * Desc: Model Class, an Actor : Tier 2 Alien Ship
 *
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

package model.GameObject;

import java.util.Random;

import model.*;

public class AlienShipTier2 extends Actor{

    //Instance Variable Declerations
    int chargeDistance; //distance that this alien ship will travel vertically. 
                        //After this value is reached, this alien ship will stop and 
                        //change its behavior pattern.
    int chargeTime; //when alien should charge player
    int timer=0; //countdown to charge

    /**
      * Constructor: calls the super constructor of Actor class and tells it's a Tier 2 Alien ship type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public AlienShipTier2() {
        super(ActorTypes.ALIENSHIP2);
        hitpoints=1;
        collisiondamage=2;
        directionProperty.set(-90);
        speed=2.0;
        Random rand = new Random();
        chargeDistance=rand.nextInt(100)+50;
        chargeTime=rand.nextInt(400)+300;
        //set location
        //set direction???
        //set speed
    }

    /**
      * movement behavior of Alien Tier 2 Ship : 
      * The ship will travel a short vertical distance before pausing.
      * Then it will transition to the waiting state where it adjusts its orientation towards the player
      * After a short while, it will charge at its chosen orientation 
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        //implement behavior of movement here
        Player player = GameController.instance().getWorld().getPlayer();
        if (location.getY()>=(stageYMax-chargeDistance)) {
            moveMe();
        }
        else {
            timer++;
            if (timer<=chargeTime) {
                int directiontemp=getDirectionToPlayer(player);

                if (directionProperty.get()<directiontemp) {
                    directionProperty.set(directionProperty.get()+2);
                }
                else if (directionProperty.get()>directiontemp) {
                    directionProperty.set(directionProperty.get()-2);
                }
            }
            else {
                moveMe();
            }
        }
    }

    /**
      * calculates the direction in degrees that points toward the player
      * @param player the player object
      * @return int direction in degrees
     */
    public int getDirectionToPlayer(Player player) {
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
      * spawning calculations of Alien Tier 2 : spawns randomly at the bottom edge of the screen
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
            int randPosX = (int) (Math.random()*(stageXMax - stageXMin + 1 )) + stageXMin;
            location.X.set(randPosX);
            location.Y.set(stageYMax+stageOffset);
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

    // do not need toString() or deserialize() methods if alien class is in place
}
