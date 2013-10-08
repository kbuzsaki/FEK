/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Sprites.SpriteUtil;

public enum Faction {
    BLUE("Blue", SpriteUtil.templateBlue),
    RED("Red", SpriteUtil.templateRed),
    GREEN("Green", SpriteUtil.templateBlue);
    
    public final String name;
    public final int[] colorTemplate;
    
    Faction (String name, int[] colorTemplate) {
        this.name = name;
        this.colorTemplate = colorTemplate;
    }
    
    public boolean isFriendlyTowards(Faction faction) {
        boolean canPass = false;
        
        switch(this)
        {
            case BLUE:
                switch(faction)
                {
                    case BLUE:
                    case GREEN:
                        canPass = true;
                }
                break;
            case RED:
                switch(faction)
                {
                    case RED:
                        canPass = true;
                }
                break;
            case GREEN:
                switch(faction)
                {
                    case BLUE:
                    case GREEN:
                        canPass = true;
                }
                break;
        }
        
        return canPass;
    }
}
