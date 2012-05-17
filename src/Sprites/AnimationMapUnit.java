/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

public class AnimationMapUnit extends Animation {
    
    public static final int defaultRowNum = 7;
    public static final int defaultColumnNum = 4;
    
    public static final int stand = 0;
    public static final int active = 1;
    public static final int moveNorth = 2;
    public static final int moveSouth = 3;
    public static final int moveWest = 4;
    public static final int moveEast = 5;
    public static final int depleted = 6;
    
    public static final int[][] sequence = { {0,0,1,2,2,2,2,1,0,0},
                                             {0,0,1,2,2,2,2,1,0,0},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,1,2,2,3,3},
                                             {0,0,1,2,2,2,2,1,0,0}};

    public AnimationMapUnit(String filepath) {
        super(filepath, defaultRowNum, defaultColumnNum, sequence);
    }
    public AnimationMapUnit(String filepath, int rowNum, int columnNum) {
        super(filepath, rowNum, columnNum, sequence);
    }
    
    public void loadAnimation(String filepath) {
        super.loadAnimation(filepath, defaultRowNum, defaultColumnNum);
    }
    public void loadAnimation(AnimationMapUnit animation) {
        super.loadAnimation(animation);
    }

    public void setStand() {
        setAnimation(stand);
    }
    public void setFocus() {
        setAnimation(active);
    }
    public void setMoveNorth() {
        setAnimation(moveNorth);
    }
    public void setMoveSouth() {
        setAnimation(moveSouth);
    }
    public void setMoveWest() {
        setAnimation(moveWest);
    }
    public void setMoveEast() {
        setAnimation(moveEast);
    }
    public void setDepleted() {
        setAnimation(depleted);
    }

}
