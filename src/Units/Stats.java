/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Units.Items.Equipment;
import Units.Items.WeaponType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

// TODO: clean up all of this mov/con/aid shit
public class Stats {
    public static final int MOVE_CAP = 15;
    
    private int totalXP;
    
    private EnumMap<AttributeType, Attribute> attributes;
    private EnumMap<WeaponType, Integer> weaponLevels;

    public Stats (UnitClass unitClass, int level) {
        totalXP = (level - 1) * 100;
        
        attributes = new EnumMap(AttributeType.class);
        
        attributes.put(AttributeType.HP, new Attribute(unitClass.HP));
        attributes.put(AttributeType.STR, new Attribute(unitClass.STR));
        attributes.put(AttributeType.SKL, new Attribute(unitClass.SKL));
        attributes.put(AttributeType.SPD, new Attribute(unitClass.SPD));
        attributes.put(AttributeType.LCK, new Attribute(unitClass.LCK));
        attributes.put(AttributeType.DEF, new Attribute(unitClass.DEF));
        attributes.put(AttributeType.RES, new Attribute(unitClass.RES));
        attributes.put(AttributeType.CON, new Attribute(unitClass.CON));
        attributes.put(AttributeType.MOV, new Attribute(unitClass.MOVE));
        
        weaponLevels = new EnumMap(WeaponType.class);
        for(WeaponType type : WeaponType.values())
        {
            weaponLevels.put(type, Equipment.WEAPON_LEVEL_NONE);
        }
        
        for (int i = 1; i < level; i++)
        {
            levelUp();
        }
    }
    
    public int getLevel() {
        int level = 1 + (totalXP / 100);
        return level;
    }
    public int getXP() {
        int XP = (totalXP % 100);
        return XP;
    }
    public int getTotalXP() {
        return totalXP;
    }
    
    public Attribute get(AttributeType attributeType) {
        return attributes.get(attributeType);
    }
    public Attribute getHP() {
        return attributes.get(AttributeType.HP);
    }
    public Attribute getStr() {
        return attributes.get(AttributeType.STR);
    }
    public Attribute getSkl() {
        return attributes.get(AttributeType.SKL);
    }
    public Attribute getSpd() {
        return attributes.get(AttributeType.SPD);
    }
    public Attribute getLck() {
        return attributes.get(AttributeType.LCK);
    }
    public Attribute getDef() {
        return attributes.get(AttributeType.DEF);
    }
    public Attribute getRes() {
        return attributes.get(AttributeType.RES);
    }
    public Attribute getMov() {
        return attributes.get(AttributeType.MOV);
    }
    public Attribute getCon() {
        return attributes.get(AttributeType.CON);
    }

    public void resetStats() {
        // TODO: better way of dealing with not resetting HP
        Collection<Attribute> resetableAttributes = new ArrayList<>(attributes.values());
        resetableAttributes.remove(getHP());
        for (Attribute attribute : resetableAttributes)
            attribute.reset();
    }
    public void levelUp () {
        for (Attribute attribute : attributes.values())
            attribute.levelUp();
    }
    
    /**
     * Return method for the weapon experience levels of a unit.
     * <br> Integer experience is unformatted, that is, total 
     * experience, not experience since leveling up.
     * <code><pre>
     * Rank: Required: Total
     *  [-]: 0         0
     *  [E]: 1         1
     *  [D]: 30        31 
     *  [C]: 40        71
     *  [B]: 50        121
     *  [A]: 60        181
     *  [S]: 70        251
     * </pre></code>
     * 
     * @param type The weapon type that you wish to lookup.
     * @return The total experience for the specified type.
     */
    public int getWeaponXP(WeaponType type) {
        return weaponLevels.get(type);
    }
    public void setWeaponXP(WeaponType type, int newValue) {
        weaponLevels.put(type, newValue);
    }
    public void addWeaponXP(WeaponType type, int addition) {
        weaponLevels.put(type, weaponLevels.get(type) + addition);
    }
    public boolean hasSRank() {
        return false;
//        if (SwordXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (LanceXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (AxeXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (BowXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (AnimaXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (LightXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (DarkXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        if (StaffXP == Equipment.WEAPON_LEVEL_S)
//            return true;
//        else 
//        {
//            return false;
//        }
    }
}
