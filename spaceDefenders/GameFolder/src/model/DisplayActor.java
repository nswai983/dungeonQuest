package model;
import model.GameObject.*;

import javafx.scene.image.Image;

/**
 * File: DisplayActor.java
 * Desc: Class that situates an actor with its respective game image
 *
 * @author  Jeremy Tan
 * @editor Nathan Swaim
 */

public class DisplayActor {
    Actor actor; //actor instance
    Image actorimg; //image for the actor
    Coordinate currentPos = new Coordinate(0,0); //actor current position
    double width; //width of actor image
    double length; // length/height  of actor image
    double cornerX; // not used, ignore
    double cornerY; // not used, ignore
    boolean onScreen; //tells if actor is on screen

    /**
      * Constructor
      * Instantiates instance variables based off of parameter
      * @param actor the Actor object to be displayed
      * @param imglocation the url of the image
      * @return N/A
     */
    public DisplayActor(Actor actor, String imglocation) {
        this.actor = actor;
        actorimg = new Image(imglocation);
        width = actorimg.getWidth();
        length = actorimg.getHeight();
        cornerX = currentPos.getX()+width;
        cornerY = currentPos.getY()+length;
        onScreen = true;

        actor.setWidth((int)actorimg.getWidth());
        actor.setLength((int)actorimg.getHeight());
    }

    //not used, ignore
    public void setPosition(Coordinate newPosition) {
        currentPos = newPosition;
        cornerX = currentPos.getX()+width;
        cornerY = currentPos.getY()+length;
    }

    //GETTERS

    public Image getActorImage() {
        return actorimg;
    }

    public Actor getActorObject() {
        return actor;
    }

    public boolean isActorOnStage() {
        return onScreen;
    }
}
