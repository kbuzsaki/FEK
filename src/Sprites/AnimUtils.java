/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * 
 * AnimUtils is a static utilities class for dealing with the movement of 
 * unit animations on the board.
 */
package Sprites;

import Units.Unit;
import java.awt.Point;

public class AnimUtils {

    // private constructor to prevent accidental instantiation
    private AnimUtils() {} 
    
    /**
     * "Nudges" a unit's animation a set distance towards its target.
     * @param unitAnim the animation to be moved.
     * @param position the starting position of the unit
     * @param target the target position that the unit is moving towards
     * @param distance the number of pixels to move the unit
     */
    public static void nudgeUnit(AnimationMapUnit unitAnim, Point position, Point target, int distance) {
        int difX = (target.x - position.x);
        int difY = (target.y - position.y);

        unitAnim.translate(
                distance*Integer.signum(difX),
                distance*Integer.signum(difY));
    }
    public static void nudgeUnit(AnimationMapUnit unitAnim, Point position, Point target) {
        nudgeUnit(unitAnim, position, target, 1);
    }
    public static void setAppropriateAnimation(Unit unit, Point postion, Point target) {
        setAppropriateAnimation(unit, 
                target.x - postion.x, 
                target.y - postion.y);
    }
    private static void setAppropriateAnimation(Unit unit, int difX, int difY) {
        if (difX > 0) {
            unit.getMapAnim().setMoveEast();
        } else if (difX < 0) {
            unit.getMapAnim().setMoveWest();
        } else if (difY > 0) {
            unit.getMapAnim().setMoveSouth();
        } else if (difY < 0) {
            unit.getMapAnim().setMoveNorth();
        } 
    }
}
