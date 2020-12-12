package model.GameObject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.*;

/**
 * File: Actor.java Desc: Model Class, abstract-class that classifies all
 * objects that are Actors in the game (they move & are 'interactable')
 *
 * @author Jeremy Tan
 * @co-author Nathan Swaim
 */

public abstract class Actor {

    protected ActorTypes actortype; // holds enum type of actor
    protected int id; // holds actor id number
    protected Coordinate location = new Coordinate(0, 0); // establishes a new base coordinate
    protected double speed = 1.0; // sets base speed
    protected int hitpoints; // lives for the actor
    protected IntegerProperty directionProperty = new SimpleIntegerProperty(); // holds direction of the actor
    protected int collisiondamage; // damage done to collided actor
    protected double imgwidth; // horizontal size of img display
    protected double imglength; // vertical size of img display
    protected boolean isPlacedOnScreen = false; // determines if this actor is placed on the game sreen
    protected boolean forceOutOfScreen; // determines if this actor should be forced out of the game screen
    protected boolean collided = false; // records the collision state of this actor: Not Collided and collided
    protected boolean collidedsoundplay = false; // not used, ignore

    protected static int stageXMin; // minimum x value of the game stage
    protected static int stageXMax; // max x value of the game stage
    protected static int stageYMin; // minimum y value of the game stage
    protected static int stageYMax; // max y value of the game stage
    protected static int stageOffset; // stage offset for dealing with alignment issues
    protected static Coordinate previouslocation; // previous location of actor

    static int nextID; // next ID for actor

    int cooldowntimer = 0; // cooldown timer for weapons
    int collisionTimer = 100; // collision timer for immunity

    /**
      * Constructor
      * @param actortype - type of actor that this object should become 
      * @return N/A
     */
    public Actor(ActorTypes actortype) {
        this.actortype = actortype;
        this.id = ++nextID;
    }

    /**
      * collision detection 
      * @param actor - the actor object that's being referenced to when detecting collision
      * @return N/A
     */
    public void checkCollision(Actor actor) {

        double xMin = getLocation().getX();
        double yMin = getLocation().getY();
        double xMax = getLocation().getX() + getImgwidth();
        double yMax = getLocation().getY() + getImglength();

        double actorXMin = actor.getLocation().getX();
        double actorYMin = actor.getLocation().getY();
        double actorXMax = actor.getLocation().getX() + actor.getImgwidth();
        double actorYMax = actor.getLocation().getY() + actor.getImglength();

        // source inspiration:
        // https://stackoverflow.com/questions/23302698/java-check-if-two-rectangles-overlap-at-any-point

        if (xMin < actorXMax && xMax > actorXMin && yMin < actorYMax && yMax > actorYMin) {
            collision(actor);
        }

    }

    /**
      * Supposed to be an abstract method. Determines what happens to this object when it does collide
      * @param actor - the actor object that has collided with this actor
      * @return N/A
     */
    public void collision(Actor actor) {
    }

    // runs with world.run() and acts as a cooldown mechanism for actor immunity
    // while running, the actor cannot collide with another actor
    public void collisionCooldown() {
        // collision immunity
        if (collisionTimer != 100 && collisionTimer > 0) {
            collisionTimer = collisionTimer - 1;
            // System.out.println(collisionTimer);
        } else if (collisionTimer == 0) {
            collisionTimer = 100;
            setCollided(false);
        }
    }

    /**
      * Abstract method; handles the movement behavior of the actor
      * @param - N/A
      * @return N/A
     */
    public abstract void updatePosition();

    /**
      * Abstract method; handles the spawning of the actor
      * @param - N/A
      * @return N/A
     */
    public abstract void spawnMe();

    /**
      * Abstract method; handles the movement of any actor object 
      * Breaks movement up into a vector w/ x, y, and direction components
      * @param - N/A
      * @return N/A
     */
    public void moveMe() {
        double x = location.getX();
        double y = location.getY();
        y += speed * Math.sin(directionProperty.get() * Math.PI / 180);
        x += speed * Math.cos(directionProperty.get() * Math.PI / 180);
        location.setX(x);
        location.setY(y);
    }

    /**
      * @author Nathan Swaim
      * Takes in a serialized string and converts it to data for an Actor object
      * Serialization Format: ID,TYPE,X,Y,HEAD
      * @param serializedString - String of the stored data fetched from saved file
      * @return N/A
     */
    public void deserialize(String serializedString) {
        String[] deserializeData = serializedString.split(",");
        setID(Integer.parseInt(deserializeData[0])); // set ID number
        Coordinate coord = new Coordinate(Integer.parseInt(deserializeData[2]), Integer.parseInt(deserializeData[3]));
        coord.setHeading(Integer.parseInt(deserializeData[4]));
        setLocation(coord); // set coordinate for x, y, and heading
    }

    /**
      * @author Nathan Swaim
      * Stores all the actor's relevant information and stores them as a String
      * Serialization Format: ID,TYPE,X,Y,HEAD
      * @param serializedString - String of the stored data fetched from saved file
      * @return string data to be serialized
     */
    public String toString() {
        String idString = String.valueOf(getID());
        String typeString = actortype.toString().substring(actortype.toString().length() - 1);
        String xString = String.valueOf((int) location.getX());
        String yString = String.valueOf((int) location.getY());
        String headingString = String.valueOf((int) location.getHeading());

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

    /**
      * initializes the hitpoint value of the actorl; makes sure that it never reaches 0
      * @param hipoints the amount of hitpoints it should set
      * @return N/A
     */
    public void initializeHitpoints(int hitpoints) {
        if (hitpoints == 0) {
            this.hitpoints = 1;
        } else {
            this.hitpoints = hitpoints;
        }
    }

    // SETTERS AND GETTERS

    /**
      * passess in the stagebound values of the GameWindow's pane
      * @param int stage bounds and offset value in Coordinate format
     */
    public static void setStageBounds(int xMin, int xMax, int yMin, int yMax, int offset) {
        stageXMin = xMin;
        stageXMax = xMax;
        stageYMin = yMin;
        stageYMax = yMax;
        stageOffset = offset;
    }

    /**
      * @return the set X min value of the GameWindow's pane
     */
    public static int getStageXMin() {
        return stageXMin;
    }

    /**
      * @return the set X max value of the GameWindow's pane
     */
    public static int getStageXMax() {
        return stageXMax;
    }

    /**
      * @return the set Y min value of the GameWindow's pane
     */
    public static int getStageYMin() {
        return stageYMin;
    }

    /**
      * @return the set Y max value of the GameWindow's pane
     */
    public static int getStageYMax() {
        return stageYMax;
    }

    //not used, ignore
    public boolean getCollidedSoundPlay() {
        return collidedsoundplay;
    }

    //not used, ignore
    public void setCollidedSoundPlay(boolean collidedsoundplay) {
        this.collidedsoundplay = collidedsoundplay;
    }

    /**
      * @return the id value of this actor
     */
    public int getID() {
        return id;
    }

    /**
      * @param id - the new id value for this actor
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
      * @return enum type of this Actor
     */
    public ActorTypes getActorType() {
        return actortype;
    }

    /**
      * @return int direction in degrees
     */
    public int getDirection() {
        return directionProperty.get();
    }

    /**
      * @param direction - the new direction of this actor
     */
    public void setDirection(int direction) {
        directionProperty.set(direction);
    }

    /**
      * @return Property attribute of the direction
     */
    public IntegerProperty getDirectionProperty() {
        return directionProperty;
    }

    /**
      * @param bool - sets the collision state of this actor (True/False)
     */
    public void setCollided(boolean bool) {
        collided = bool;
    }
    
    /**
      * @return boolean the collision state of this actor
     */
    public boolean getCollided() {
        return collided;
    }

    /**
      * @return the speed of this actor 
     */
    public double getSpeed() {
        return speed;
    }

    /**
      * @param speed - sets the actor's new speed value
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
      * @return the current hitpoints of this actor
     */    
    public int getHitpoints() {
        return hitpoints;
    }

    /**
      * @param hitpoints - sets the actor's new hitpoints
     */
    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    /**
      * @return the current location of this actor in Coordinates
     */  
    public Coordinate getLocation() {
        return location;
    }

    /**
      * @param coord - sets the actor's new Coordinate location
     */
    public void setLocation(Coordinate coord) {
        location = coord;
    }

    /**
      * @return the actor's display-image width value
     */  
    public double getWidth() {
        return imgwidth;
    }

    /**
      * @return the actor's display-image length/height value
     */  
    public double getLength() {
        return imglength;
    }

    /**
      * @param width - sets the actor's display-image's new width value
     */
    public void setWidth(int width) {
        imgwidth = width;
    }

    /**
      * @param width - sets the actor's display-image's new length/height value
     */
    public void setLength(int length) {
        imglength = length;
    }

    /**
      * @return the state of whether this actor is ushered out of the Game Window's Pane
     */ 
    public boolean getIsPlacedOnScreen() {
        return isPlacedOnScreen;
    }

    /**
      * @param placed - sets the state of whether this actor has been ushered into the Game Window's Pane
     */
    public void setIsPlacedOnScreen(boolean placed) {
        this.isPlacedOnScreen = placed;
    }

    /**
      * @return the state whether the actor's image has successfully exited out of the Pane
     */ 
    public boolean getForceOutOfScreen() {
        return forceOutOfScreen;
    }

    /**
      * @param forcedout - sets the boolean condition of whether the actor has sucessfully exited out of the Pane
     */
    public void setForceOutOfScreen(boolean forceout) {
        forceOutOfScreen = forceout;
    }

    /**
      * @return: gets the next unique id to be generated
     */ 
    public static int getNextID() {
        return nextID;
    }

    /**
      * @param nextID - sets the next unique id to be generated 
     */
    public static void setNextID(int nextID) {
        Actor.nextID = nextID;
    }

    /**
      * @return: true if this actor has reached 0 hitpoints
     */ 
    public boolean isDead() {
        if (hitpoints <= 0) {
            hitpoints = 0;
            return true;
        }
        return false;
    }

    /**
      * @return: returns the display image's width of this actor
     */ 
    public double getImgwidth() {
        return imgwidth;
    }

    /**
      * @param imgwidth - sets the display image's width of this actor
     */
    public void setImgwidth(double imgwidth) {
        this.imgwidth = imgwidth;
    }

    /**
      * @return: returns the display image's length/height of this actor
     */ 
    public double getImglength() {
        return imglength;
    }

    /**
      * @param imgwidth - sets the display image's length/height of this actor
     */
    public void setImglength(double imglength) {
        this.imglength = imglength;
    }

    /**
      * @param directionProperty - replaced the Property attribute of the direction of this actor
     */
    public void setDirectionProperty(IntegerProperty directionProperty) {
        this.directionProperty = directionProperty;
    }

}