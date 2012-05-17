/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Units;

public enum UnitClass {
       //value cap growth
    BANDIT("Bandit", "bandit", MoveType.BANDIT,
           20, 60, 82, // HP
            5, 20, 50, // STR
            1, 20, 30, // SKL
            5, 20, 20, // SPD
            0, 30, 15, // LCK
            3, 20, 10, // DEF
            0, 20, 13, // RES
           12, 20,  0, // CON
            5, 15,  0  // MOV
            ),
    CLERIC("Cleric", "cleric", MoveType.FOOT,
           16, 60, 50, // HP
            1, 20, 30, // STR
            2, 20, 35, // SKL
            2, 20, 32, // SPD
            0, 30, 45, // LCK
            0, 20,  8, // DEF
            6, 20, 50, // RES
            4, 20,  0, // CON
            5, 15,  0  // MOV
            ),
    MERCENARY("Mercenary", "mercenary", MoveType.FOOT,
           16, 60, 80, // HP
            3, 20, 40, // STR
            5, 20, 40, // SKL
            6, 20, 32, // SPD
            0, 30, 30, // LCK
            2, 20, 18, // DEF
            0, 20, 20, // RES
            8, 20,  0, // CON
            5, 15,  0  // MOV
            );
    
        public final String className;
        public final String spriteFileName;
        public final MoveType moveT;
        public final Attribute HP;
        public final Attribute STR;
        public final Attribute SKL;
        public final Attribute SPD;
        public final Attribute LCK;
        public final Attribute DEF;
        public final Attribute RES;
        public final Attribute CON;
        public final Attribute MOV;
    
        UnitClass( String className, String spriteFileName, MoveType moveT,
                   int HPvalue,  int HPcap,  int HPgrowth,
                   int STRvalue, int STRcap, int STRgrowth,
                   int SKLvalue, int SKLcap, int SKLgrowth,
                   int SPDvalue, int SPDcap, int SPDgrowth,
                   int LCKvalue, int LCKcap, int LCKgrowth,
                   int DEFvalue, int DEFcap, int DEFgrowth,
                   int RESvalue, int REScap, int RESgrowth,
                   int CONvalue, int CONcap, int CONgrowth,
                   int MOVvalue, int MOVcap, int MOVgrowth)
        {
            this.className = className;
            this.spriteFileName = spriteFileName;
            this.moveT = moveT;
            HP  = new Attribute(AttributeType.HP,  HPvalue,  HPcap,  HPgrowth);
            STR = new Attribute(AttributeType.STR, STRvalue, STRcap, STRgrowth);
            SKL = new Attribute(AttributeType.SKL, SKLvalue, SKLcap, SKLgrowth);
            SPD = new Attribute(AttributeType.SPD, SPDvalue, SPDcap, SPDgrowth);
            LCK = new Attribute(AttributeType.LCK, LCKvalue, LCKcap, LCKgrowth);
            DEF = new Attribute(AttributeType.DEF, DEFvalue, DEFcap, DEFgrowth);
            RES = new Attribute(AttributeType.RES, RESvalue, REScap, RESgrowth);
            CON = new Attribute(AttributeType.CON, CONvalue, CONcap, CONgrowth);
            MOV = new Attribute(AttributeType.MOV, MOVvalue, MOVcap, MOVgrowth);
        }
}
