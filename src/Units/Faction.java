/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

public enum Faction {
    BLUE("Blue"),
    RED("Red"),
    GREEN("Green");
    
    public final String name;
    
    Faction (String name) {
        this.name = name;
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
