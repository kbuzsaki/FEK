/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Maps;

public enum Terrain {
    //        sym  name       avd def res status heal mov
    PLAINS  ( '_', "Plains",   0,  0,  0, false, 0,   1 ),
    FOREST  ( 'F', "Forest",   20, 1,  0, false, 0,   2 ),
    FORT    ( '#', "Fort",     20, 2,  0, false, .2,  2 ),
    HOUSE   ( 'H', "House",    10, 0,  0, false, 0,   1 ),
    MOUNTAIN( 'M', "Mountain", 10, 0,  0, false, 0,   4 ),
    RIVER   ( 'R', "River",    20, 0,  0, false, 0,   Terrain.UNCROSSABLE ),
    SEA     ( 'S', "Sea",      10, 2,  0, false, 0,   Terrain.UNCROSSABLE );
    
    public final char C;
    public final String name;
    public final int avoidBonus;
    public final int defenseBonus;
    public final int resistanceBonus;
    public final boolean restoreStatus;
    public final double healBonus;
    
    public final int moveCost;
    
    public static final int UNCROSSABLE = 99;
    
    Terrain(char C, String name, int avoidBonus, int defenseBonus, 
            int resistanceBonus, boolean restoreStatus, double healBonus, 
            int moveCost)
    {
        this.C = C;
        this.name = name;
        
        this.avoidBonus = avoidBonus;
        this.defenseBonus = defenseBonus;
        this.resistanceBonus = resistanceBonus;
        this.restoreStatus = restoreStatus;
        this.healBonus = healBonus;
        
        this.moveCost = moveCost;
    }
    
    public int getMoveCost(Units.MoveType moveT) {
        switch(this)
        {
            case FOREST:
                switch(moveT)
                {
                    case KNIGHTA:
                    case KNIGHTB:
                        return 3;
                    case FLIER:
                        return 1;
                }
                break;
            case MOUNTAIN:
                switch(moveT)
                {
                    case KNIGHTA:
                    case NOMADA:
                    case ARMOR:
                        return UNCROSSABLE;
                    case FIGHTER:
                    case BANDIT:
                    case PIRATE:
                    case BERZERKER:
                        return 3;
                    case NOMADB:
                        return 5;
                    case KNIGHTB:
                        return 6;
                    case FLIER:
                        return 1;
                }
                break;
            case RIVER:
                switch(moveT)
                {
                    case FOOT:
                    case BANDIT:
                    case NOMADB:
                        return 5;
                    case PIRATE:
                    case BERZERKER:
                        return 2;
                    case FLIER:
                        return 1;
                }
                break;
            case SEA:
                switch(moveT)
                {
                    case PIRATE:
                    case BERZERKER:
                        return 2;
                    case FLIER:
                        return 1;
                }
                break;
        }
        return moveCost;
    }
    
    void run(Units.Unit unit)
    {
        unit.getStats();
    }
    
}
