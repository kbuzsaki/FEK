/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Battle;
import Game.Battle;
import Game.Cursor;
import Game.Cursor;
import Game.Level;
import Game.Level;
import Sprites.Character;
import Sprites.ImageComponent;
import Units.Items.WeaponFactory;
import Units.Unit;
import java.awt.Dimension;
import java.util.ArrayList;

public class MenuSelectionAttack extends MenuSelection{
    
    private ImageComponent background;
    
    private ImageComponent[] labels;
    private final static String[] labelsName = {"HP", "Dmg", "Acc", "Crit" };
    
    private ImageComponent attackerName;
        private ImageComponent attackerItemIcon;
        private ImageComponent attackerHP;
        private ImageComponent attackerDmg;
        private ImageComponent attackerAcc;
        private ImageComponent attackerCrit;
    
    private ImageComponent defenderName;
        private ImageComponent defenderItemIcon;
        private ImageComponent defenderItemName;
        private ImageComponent defenderHP;
        private ImageComponent defenderDmg;
        private ImageComponent defenderAcc;
        private ImageComponent defenderCrit;
        
    private Battle battle;
    // TODO: weapon triangle advantage stuff
    
    public MenuSelectionAttack(Level level, Dimension mapSize, Cursor cursor) {
        
        super(level, mapSize, cursor);
        
        background = new ImageComponent("resources/gui/window/attackMenuBlank.png");
        
        labels = new ImageComponent[labelsName.length];
        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = Character.getImageComponent(labelsName[i], Character.YELLOW);
            labels[i].setLocation(27, 22 + 16*i);
            add(labels[i]);
        }
        
        attackerName = Character.getImageComponent("Alan");
        attackerName.setLocation(21 + (43 - attackerName.getWidth()) / 2, 6);
        add(attackerName);
        attackerItemIcon = WeaponFactory.swordIron().getIcon();
        attackerItemIcon.setLocation(4, 3);
        add(attackerItemIcon);
        attackerHP = Character.getImageComponent(String.valueOf(0));
        attackerHP.setLocation(68 - attackerHP.getWidth(), 22);
        add(attackerHP);
        attackerDmg = Character.getImageComponent(String.valueOf(0));
        attackerDmg.setLocation(68 - attackerDmg.getWidth(), 38);
        add(attackerDmg);
        attackerAcc = Character.getImageComponent(String.valueOf(0));
        attackerAcc.setLocation(68 - attackerAcc.getWidth(), 54);
        add(attackerAcc);
        attackerCrit = Character.getImageComponent(String.valueOf(0));
        attackerCrit.setLocation(68 - attackerCrit.getWidth(), 70);
        add(attackerCrit);
        
        defenderName = Character.getImageComponent("Damas");
        defenderName.setLocation(5 + (43 - defenderName.getWidth()) / 2, 86);
        add(defenderName);
        defenderItemIcon = WeaponFactory.axeIron().getIcon();
        defenderItemIcon.setLocation(52, 83);
        add(defenderItemIcon);
        defenderItemName = Character.getImageComponent("Steel Axe");
        defenderItemName.setLocation(5 + (51 - defenderItemName.getWidth()) / 2, 102);
        add(defenderItemName);
        defenderHP = Character.getImageComponent(String.valueOf(0));
        defenderHP.setLocation(20 - defenderHP.getWidth(), 22);
        add(defenderHP);
        defenderDmg = Character.getImageComponent(String.valueOf(0));
        defenderDmg.setLocation(20 - defenderDmg.getWidth(), 38);
        add(defenderDmg);
        defenderAcc = Character.getImageComponent(String.valueOf(0));
        defenderAcc.setLocation(20 - defenderAcc.getWidth(), 54);
        add(defenderAcc);
        defenderCrit = Character.getImageComponent(String.valueOf(0));
        defenderCrit.setLocation(20 - defenderCrit.getWidth(), 70);
        add(defenderCrit);
        
        add(background);
        
        setSize(background.getWidth(), background.getHeight());
        setPreferredSize(getSize());
    }
    
    public final void reconstructMenu() {
        battle = new Battle(level, cursor.getSelectedUnit(), getTargetedUnit());
        
        attackerName.setImage(Character.getImageComponent(battle.getAttacking().getName()));
        attackerName.setLocation(21 + (43 - attackerName.getWidth()) / 2, 6);
        
        attackerItemIcon.setImage(battle.getAttackerEquiped().getIcon());
        
        attackerHP.setImage(Character.getImageComponent(String.valueOf(battle.getAttackerHP())));
        attackerHP.setLocation(68 - attackerHP.getWidth(), 22);
        
        attackerDmg.setImage(Character.getImageComponent(String.valueOf(battle.getAttackerDamage())));
        attackerAcc.setImage(Character.getImageComponent(String.valueOf(battle.getAttackerAccuracy())));
        attackerCrit.setImage(Character.getImageComponent(String.valueOf(battle.getAttackerCriticalChance())));
        attackerDmg.setLocation(68 - attackerDmg.getWidth(), 38);
        attackerAcc.setLocation(68 - attackerAcc.getWidth(), 54);
        attackerCrit.setLocation(68 - attackerCrit.getWidth(), 70);
        
        defenderName.setImage(Character.getImageComponent(battle.getDefending().getName()));
        defenderName.setLocation(5 + (43 - defenderName.getWidth()) / 2, 86);
        
        defenderItemIcon.setImage(battle.getDefenderEquiped().getIcon());
        
        defenderItemName.setImage(Character.getImageComponent(battle.getDefenderEquiped().getName()));
        defenderItemName.setLocation(5 + (51 - defenderItemName.getWidth()) / 2, 102);
        
        defenderHP.setImage(Character.getImageComponent(String.valueOf(battle.getDefenderHP())));
        defenderHP.setLocation(20 - defenderHP.getWidth(), 22);
        
        if (battle.getDefending().canAttack(battle.getAttacking()))
        {
        defenderDmg.setImage(Character.getImageComponent(String.valueOf(battle.getDefenderDamage())));
        defenderAcc.setImage(Character.getImageComponent(String.valueOf(battle.getDefenderAccuracy())));
        defenderCrit.setImage(Character.getImageComponent(String.valueOf(battle.getDefenderCriticalChance())));
        }
        else
        {
        defenderDmg.setImage(Character.getImageComponent("-- "));
        defenderAcc.setImage(Character.getImageComponent("-- "));
        defenderCrit.setImage(Character.getImageComponent("-- "));
        }
        defenderDmg.setLocation(20 - defenderDmg.getWidth(), 38);
        defenderAcc.setLocation(20 - defenderAcc.getWidth(), 54);
        defenderCrit.setLocation(20 - defenderCrit.getWidth(), 70);
    }
    
    public void open(ArrayList<Unit> selectableUnits) {
        super.open(selectableUnits);
        this.cursor.getMapAnim().setAttack();
    }
    
    public void setTick(int tick) {
        // number of attacks stuff goes here.
    }
    
    protected void performAction() {
        cursor.hideCursor();
        level.initiateBattle(battle);
    }
}
