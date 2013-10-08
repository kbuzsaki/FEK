/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

public enum AttributeType {
    
    HP("HP", null),
    STR("Str", null),
    SKL("Skl", null),
    SPD("Spd", null),
    LCK("Luck", null),
    DEF("Def", null),
    RES("Res", null),
    CON("Con", null),
    MOV("Move", null);
     
    public final String name;
    public final String description;
    
    private AttributeType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
