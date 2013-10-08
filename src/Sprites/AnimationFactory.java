/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

public class AnimationFactory {
    
    
    
    public static Animation newBlankAnimation() {
        int[][] sequence = {{0}};
        return new Animation(Animation.getFilename("blank"), 1, 3, sequence);
    }
    public static Animation newPointer() {
        int[][] sequence = {{0,1,2,3,3,2,1,0}};
        return new Animation(Animation.getFilename("pointer"), 1, 4, sequence);
    }
    public static Animation newArrowUp() {
        int[][] sequence = {{0,0,0,1,1,2,2,2,1,1}};
        return new Animation(Animation.getFilename("arrowUp"), 1, 3, sequence);
    }
    public static Animation newArrowDown() {
        int[][] sequence = {{0,0,0,1,1,2,2,2,1,1}};
        return new Animation(Animation.getFilename("arrowDown"), 1, 3, sequence);
    }
}
