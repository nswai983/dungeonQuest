/**
 * File: Artifact.java
 * Desc: Model Class, an Actor : Arifact
 *
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

package model.GameObject;

import java.util.Random;

import model.*;


public class Artifact extends Actor {

    //Instance varaible declerations
    final double timelimit = 500; //how long the artifact can be on screen
    double timer = 0; //count of how long artifact has been on screen
    boolean collected = false; //tells if artifact has been collected

    /**
      * Constructor: calls the super constructor of Actor class and tells it's an Arifact type
      * Instantiates its actor's instance variables
      * @param N/A
      * @return N/A
     */
    public Artifact() {
        super(ActorTypes.ARTIFACT);
        hitpoints = 1;
        collisiondamage = 0;
        speed = 1.0;
    }

    /**
      * movement behavior of Artifact : stays still till the its timer runs out 
      * @param N/A
      * @return N/A
     */
    public void updatePosition() {
        timer += speed;
        if (timer >= 500) {
            timer = 500;
            hitpoints=0;
        }
        if (this.collided == true) {
            timer = 500;
        }
    }

    /**
      * spawning calculations of Artifact : spawns anywhere randomly on the Game Stage
      * @param N/A
      * @return N/A
     */
    public void spawnMe() {
        int randPosX = (int) (Math.random() * (stageXMax - stageXMin + 1)) + stageXMin;
        int randPosY = (int) (Math.random() * (stageYMax - stageYMin + 1)) + stageYMin;
        location.X.set(randPosX);
        location.Y.set(randPosY);
    }

    //SETTERS AND GETTERS

    /**
      * @return true if it has exceeded its expiration time
     */
    public boolean isTimerUp() {
        return (timer >= 500);
    }

    /**
      * @return the total amount of time it can stay on the screen
     */
    public double getTimelimit() {
        return timelimit;
    }

    /**
      * @return the current amount of time stayed on the screen
     */
    public double getTimer() {
        return timer;
    }

    /**
      * @param timer sets the current amount of time stayed on the screen
     */
    public void setTimer(double timer) {
        this.timer = timer;
    }

    /**
      * @return true if Artifact has been collectd 
     */
    public boolean isCollected() {
        return collected;
    }

    /**
      * @param collected sets the collison state of the Artifact
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
