/**
 * File: Asteroids.java
 * Desc: Model Class, an Actor : Asteroid
 *
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

package model.GameObject;

import java.util.Random;

import model.*;

//jtan
public class Asteroid extends Actor {

    //Instance Variable Declerations
    boolean spawnonLeftSide; //sets asteroid to spawn on left side
    boolean spawnonRightSide; //sets asteroid to spawn on right side

    /**
      * Constructor: calls the super constructor of Actor class and tells it's an Asteroid type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public Asteroid() {
        super(ActorTypes.ASTEROID);
        hitpoints=2;
        collisiondamage=2;
    }

    /**
      * movement behavior of Artifact : moves the Asteroid in a linear fashion based off its defined characteristics
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        moveMe();
    }

    /**
      * spawning calculations of Asteroid : spawns randomly on any edge of the game sreen
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
        Random rand = new Random();
        int chooser = rand.nextInt(2);

            int randPosX;
            int randPosY = (int) (Math.random()*(stageYMax - stageYMin + 1 )) + stageYMin;
            if (chooser == 0) {
                directionProperty.set(rand.nextInt(90)-45);
                randPosX=stageXMin-stageOffset;
                spawnonLeftSide=true;
            }
            else {
                directionProperty.set(rand.nextInt(90)+135);
                randPosX=stageXMax+stageOffset;
                spawnonRightSide=true;
            }
            location.setX(randPosX);
            location.setY(randPosY);

    }

    /**
      * @author Nathan Swaim
      * Takes in a serialized string and converts it to data for an Asteroid object
      * Serialization Format: ID,TYPE,X,Y,HEAD
      * @param serializedString - String of the stored data fetched from saved file
      * @return N/A
     */
    @Override
    public void deserialize(String serializedString) {
        String[] deserializeData = serializedString.split(",");
        setID(Integer.parseInt(deserializeData[0])); // set ID number
        int spawnSide = Integer.parseInt(deserializeData[1]);
        if (spawnSide == 0) {
            spawnonLeftSide();
        } else {
            spawnonRightSide();
        }
        Coordinate coord = new Coordinate(Integer.parseInt(deserializeData[2]), Integer.parseInt(deserializeData[3]));
        coord.setHeading(Integer.parseInt(deserializeData[4]));
        setLocation(coord); // set coordinate for x, y, and heading
        // setSpeed(Integer.parseInt(deserializeData[5]) / 100);
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
        String typeString;
        if (spawnonLeftSide) {
            typeString = "0";
        } else {
            typeString = "1";
        }
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

    //SETTER methods

    public void spawnonLeftSide() {
        spawnonLeftSide=true;
    }

    public void spawnonRightSide() {
        spawnonRightSide=true;
    }
}
