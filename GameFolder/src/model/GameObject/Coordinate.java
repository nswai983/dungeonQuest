/*
//File:   Coordinate.java
//Author: Stephen Schaub
//Desc:   This class represents a Cartesian coordinate for Battleship classes to use.
*/

package model.GameObject;
import model.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Coordinate {
    protected SimpleDoubleProperty X = new SimpleDoubleProperty();
    protected SimpleDoubleProperty Y = new SimpleDoubleProperty();
    protected IntegerProperty heading = new SimpleIntegerProperty();

    public Coordinate(double x, double y) {
        X.set(x);
        Y.set(y);
    }

    public double getX() {
        return X.get();
    }

    public double getY() {
        return Y.get();
    }

    public DoubleProperty getXProperty() {
        return X;
    }

    public DoubleProperty getYProperty() {
        return Y;
    }

    public int getHeading() {
        return heading.get();
    }

    public void setX(double x) {
        X.set(x);
    }

    public void setY(double y) {
        Y.set(y);
    }

    public void setHeading(int heading) {
        this.heading.set(heading);
    }

    @Override
    public String toString() {
        return "(" + X.get() + "," + Y.get() + ")";
    }

    // Nathan Swaim - pulled from Program 5
    // calculates angle based on current Coordinate location and target Coordinate location
    // returns the angle as a double
    // source:
    // https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
    public int getAngle(Coordinate target) {
        double angle = (double) Math.toDegrees(Math.atan2(target.getY() - getY(), target.getX() - getX()));

        if (angle < 0) {
            angle += 360;
        }

        heading.set((int) angle);
        return (int) angle;
    }
}