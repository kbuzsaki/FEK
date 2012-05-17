/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

public class AnimationMapCursor extends Animation {
    
    public static final int defaultRowNum = 3;
    public static final int defaultColumnNum = 3;
    
    public static final int normal = 0;
    public static final int attack = 1;
    public static final int focus = 2;
    
    public static final int[][] sequence = { {0,1,2,2,2,1,0,0},
                                             {0,1,2,2,2,1,0,0},
                                             {0}                };

    public AnimationMapCursor (String filepath) {
        super(filepath, defaultRowNum, defaultColumnNum, sequence);
    }
    public AnimationMapCursor(String filepath, int rowNum, int columnNum) {
        super(filepath, rowNum, columnNum, sequence);
    }

    public void setNormal() {
        setAnimation(normal);
    }
    public void setAttack() {
        setAnimation(attack);
    }
    public void setFocus() {
        setAnimation(focus);
    }

}
