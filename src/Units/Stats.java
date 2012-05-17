/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

import Units.Items.Equipment;
import java.util.Random;
import Units.Items.WeaponType;

// TODO: clean up all of this mov/con/aid shit
public class Stats {
    private int totalXP;
    
    private Attribute HP;  
    private Attribute STR;
    private Attribute SKL;
    private Attribute SPD;
    private Attribute LCK;
    private Attribute DEF;
    private Attribute RES;
    private Attribute CON;
//    private Attribute AID;
    private Attribute MOV;
    
    private int SwordXP;
    private int LanceXP;
    private int AxeXP;
    private int BowXP;
    private int AnimaXP;
    private int LightXP;
    private int DarkXP;
    private int StaffXP;

    public Random rng = new Random();

    public Stats (UnitClass unitClass, int level) {
        totalXP = (level - 1) * 100;
        
        HP = new Attribute(unitClass.HP);
        STR = new Attribute(unitClass.STR);
        SKL = new Attribute(unitClass.SKL);
        SPD = new Attribute(unitClass.SPD);
        LCK = new Attribute(unitClass.LCK);
        DEF = new Attribute(unitClass.DEF);
        RES = new Attribute(unitClass.RES);
        CON = new Attribute(unitClass.CON);
        MOV = new Attribute(unitClass.MOV);
        
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
    
    public Attribute getHP() {
        return HP;
    }
    public Attribute getSTR() {
        return STR;
    }
    public Attribute getSKL() {
        return SKL;
    }
    public Attribute getSPD() {
        return SPD;
    }
    public Attribute getLCK() {
        return LCK;
    }
    public Attribute getDEF() {
        return DEF;
    }
    public Attribute getRES() {
        return RES;
    }
    public Attribute getCON() {
        return CON;
    }
//    public Attribute getAID() {
//        return AID;
//    }
    public Attribute getMOV() {
        return MOV;
    }

    public void resetStats() {
        HP.reset();
        STR.reset();
        SKL.reset();
        SPD.reset();
        LCK.reset();
        DEF.reset();
        RES.reset();
//        CON.reset();
//        AID.reset();
        MOV.reset();
    }
    public void levelUp () {
        HP.levelUp();
        STR.levelUp();
        SKL.levelUp();
        SPD.levelUp();
        LCK.levelUp();
        DEF.levelUp();
        RES.levelUp();
//        CON.levelUp();
//        AID.levelUp();
        MOV.levelUp();
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
    public int getWeaponXP(Equipment equip) {
//        switch (type) {
//            case SWORD:
//                return SwordXP;
//            case LANCE:
//                return LanceXP;
//            case AXE:
//                return AxeXP;
//            case BOW:
//                return BowXP;
//            case ANIMA:
//                return AnimaXP;
//            case LIGHT:
//                return LightXP;
//            case DARK:
//                return DarkXP;
//            case STAFF:
//                return StaffXP;
//            default:
                return 10;
//        }
    }
    public void setWeaponXP(WeaponType type, int newValue) {
        switch (type) {
            case SWORD:
                SwordXP = newValue;
                break;
            case LANCE:
                LanceXP = newValue;
                break;
            case AXE:
                AxeXP = newValue;
                break;
            case BOW:
                BowXP = newValue;
                break;
            case ANIMA:
                AnimaXP = newValue;
                break;
            case LIGHT:
                LightXP = newValue;
                break;
            case DARK:
                DarkXP = newValue;
                break;
            case STAFF:
                StaffXP = newValue;
                break;
        }
    }
    public void addWeaponXP(WeaponType type, int addition) {
        switch (type) {
            case SWORD:
                SwordXP += addition;
                break;
            case LANCE:
                LanceXP += addition;
                break;
            case AXE:
                AxeXP += addition;
                break;
            case BOW:
                BowXP += addition;
                break;
            case ANIMA:
                AnimaXP += addition;
                break;
            case LIGHT:
                LightXP += addition;
                break;
            case DARK:
                DarkXP += addition;
                break;
            case STAFF:
                StaffXP += addition;
                break;
        }
    }
    public boolean hasSRank() {
        if (SwordXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (LanceXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (AxeXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (BowXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (AnimaXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (LightXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (DarkXP == Equipment.WEAPON_LEVEL_S)
            return true;
        if (StaffXP == Equipment.WEAPON_LEVEL_S)
            return true;
        else 
        {
            return false;
        }
    }
}
