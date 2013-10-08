/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Battle;
import Game.Cursor;
import Game.Level;
import Game.Sound.SoundManager;
import Sprites.Animation;
import Sprites.AnimationFactory;
import Sprites.Text;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import Units.Items.Equipment;
import Units.Items.Item;
import Units.Items.ItemFactory;
import Units.Items.Weapon;

public class MenuSelectionAttack extends MenuSelection{
    
    private ImageComponent background;
    
    private ImageComponent[] labels;
    private final static String[] labelsName = {"HP", "Dmg", "Acc", "Crit" };
    
    private ImageComponent attackerName;
        private Animation attackerWeaponTriangleIndicator;
        private ImageComponent attackerItemIcon;
        private ImageComponent attackerHP;
        private ImageComponent attackerDmg;
        private ImageComponent attackerAcc;
        private ImageComponent attackerCrit;
    
    private ImageComponent defenderName;
        private Animation defenderWeaponTriangleIndicator;
        private ImageComponent defenderItemIcon;
        private ImageComponent defenderItemName;
        private ImageComponent defenderHP;
        private ImageComponent defenderDmg;
        private ImageComponent defenderAcc;
        private ImageComponent defenderCrit;
        
    private Battle battle;
    
    public MenuSelectionAttack(Level level, GameScreen gameScreen, 
            SoundManager soundManager, Cursor cursor) {
        
        super(level, gameScreen, soundManager, cursor);
        
        background = new ImageComponent("resources/gui/window/attackMenuBlank.png");
        
        labels = new ImageComponent[labelsName.length];
        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = Text.getImageComponent(labelsName[i], Text.YELLOW);
            labels[i].setLocation(27, 22 + 16*i);
            add(labels[i]);
        }
        
        attackerName = Text.getImageComponent("Alan");
        attackerName.setLocation(21 + (43 - attackerName.getWidth()) / 2, 6);
        add(attackerName);
        attackerWeaponTriangleIndicator = AnimationFactory.newArrowUp();
        attackerWeaponTriangleIndicator.setLocation(11,8);
        add(attackerWeaponTriangleIndicator);
        attackerItemIcon = ItemFactory.swordIron().getIcon();
        attackerItemIcon.setLocation(4, 3);
        add(attackerItemIcon);
        attackerHP = Text.getImageComponent(String.valueOf(0));
        attackerHP.setLocation(68 - attackerHP.getWidth(), 22);
        add(attackerHP);
        attackerDmg = Text.getImageComponent(String.valueOf(0));
        attackerDmg.setLocation(68 - attackerDmg.getWidth(), 38);
        add(attackerDmg);
        attackerAcc = Text.getImageComponent(String.valueOf(0));
        attackerAcc.setLocation(68 - attackerAcc.getWidth(), 54);
        add(attackerAcc);
        attackerCrit = Text.getImageComponent(String.valueOf(0));
        attackerCrit.setLocation(68 - attackerCrit.getWidth(), 70);
        add(attackerCrit);
        
        defenderName = Text.getImageComponent("Damas");
        defenderName.setLocation(5 + (43 - defenderName.getWidth()) / 2, 86);
        add(defenderName);
        defenderWeaponTriangleIndicator = AnimationFactory.newArrowDown();
        defenderWeaponTriangleIndicator.setLocation(63,88);
        add(defenderWeaponTriangleIndicator);
        defenderItemIcon = ItemFactory.axeIron().getIcon();
        defenderItemIcon.setLocation(52, 83);
        add(defenderItemIcon);
        defenderItemName = Text.getImageComponent("Steel Axe");
        defenderItemName.setLocation(5 + (51 - defenderItemName.getWidth()) / 2, 102);
        add(defenderItemName);
        defenderHP = Text.getImageComponent(String.valueOf(0));
        defenderHP.setLocation(20 - defenderHP.getWidth(), 22);
        add(defenderHP);
        defenderDmg = Text.getImageComponent(String.valueOf(0));
        defenderDmg.setLocation(20 - defenderDmg.getWidth(), 38);
        add(defenderDmg);
        defenderAcc = Text.getImageComponent(String.valueOf(0));
        defenderAcc.setLocation(20 - defenderAcc.getWidth(), 54);
        add(defenderAcc);
        defenderCrit = Text.getImageComponent(String.valueOf(0));
        defenderCrit.setLocation(20 - defenderCrit.getWidth(), 70);
        add(defenderCrit);
        
        add(background);
        
        setSize(background.getWidth(), background.getHeight());
        setPreferredSize(getSize());
    }
    
    @Override
    public void openQuietly(CancelListener cancelListener) {
        openQuietly(level.getMap().getUnitsAt(getActor().getAttackablePoints()), cancelListener);
        level.getPanelEffectsTile().updateAttackablePoints(getActor().getAttackablePoints());
    }
    @Override
    public final void reconstructMenu(int index) {
        battle = new Battle(level, getActor(), getTarget(index));
        
        attackerName.setImage(Text.getImageComponent(battle.getAttacking().getName()));
        attackerName.setLocation(21 + (43 - attackerName.getWidth()) / 2, 6);
        
        attackerWeaponTriangleIndicator.loadAnimation(battle.getAttackingWeaponTriangleIndicator());
        
        attackerItemIcon.setImage(battle.getAttackerEquiped().getIcon());
        
        attackerHP.setImage(Text.getImageComponent(String.valueOf(battle.getAttackerHP())));
        attackerHP.setLocation(68 - attackerHP.getWidth(), 22);
        
        attackerDmg.setImage(Text.getImageComponent(String.valueOf(battle.getAttackerDamage())));
        attackerAcc.setImage(Text.getImageComponent(String.valueOf(battle.getAttackerAccuracy())));
        attackerCrit.setImage(Text.getImageComponent(String.valueOf(battle.getAttackerCriticalChance())));
        attackerDmg.setLocation(68 - attackerDmg.getWidth(), 38);
        attackerAcc.setLocation(68 - attackerAcc.getWidth(), 54);
        attackerCrit.setLocation(68 - attackerCrit.getWidth(), 70);
        
        defenderName.setImage(Text.getImageComponent(battle.getDefending().getName()));
        defenderName.setLocation(5 + (43 - defenderName.getWidth()) / 2, 86);
        
        if(battle.getDefending().hasEquiped())
        {
            defenderItemIcon.setImage(battle.getDefending().getEquiped().getIcon());
            defenderItemName.setImage(Text.getImageComponent(battle.getDefending().getEquiped().getName()));
        }
        else
        {
            defenderItemIcon.setBlank();
            defenderItemName.setImage(Text.getImageComponent("--"));
        }
        
        defenderWeaponTriangleIndicator.loadAnimation(battle.getDefendingWeaponTriangleIndicator());
        
        defenderItemName.setLocation(5 + (51 - defenderItemName.getWidth()) / 2, 102);
        
        defenderHP.setImage(Text.getImageComponent(String.valueOf(battle.getDefenderHP())));
        defenderHP.setLocation(20 - defenderHP.getWidth(), 22);
        
        if (battle.getDefending().canAttack(battle.getAttacking()))
        {
            defenderDmg.setImage(Text.getImageComponent(String.valueOf(battle.getDefenderDamage())));
            defenderAcc.setImage(Text.getImageComponent(String.valueOf(battle.getDefenderAccuracy())));
            defenderCrit.setImage(Text.getImageComponent(String.valueOf(battle.getDefenderCriticalChance())));
        }
        else
        {
            defenderDmg.setImage(Text.getImageComponent("-- "));
            defenderAcc.setImage(Text.getImageComponent("-- "));
            defenderCrit.setImage(Text.getImageComponent("-- "));
        }
        defenderDmg.setLocation(20 - defenderDmg.getWidth(), 38);
        defenderAcc.setLocation(20 - defenderAcc.getWidth(), 54);
        defenderCrit.setLocation(20 - defenderCrit.getWidth(), 70);
    }
    @Override
    public void setTick(int tick) {
        attackerWeaponTriangleIndicator.setTick(tick);
        defenderWeaponTriangleIndicator.setTick(tick);
        repaint();
    }
    
    @Override
    public boolean isTargetable(Item item) {
        if(item instanceof Weapon)
            return true;
        else
            return false;
    }
    @Override
    public boolean isUseable(Item item) {
        return isTargetable(item)&&getActor().isUseable(item);
    }
    
    @Override
    void updateRanges(Item item) {
        if(item instanceof Weapon)
        {
            level.getPanelEffectsTile().updateAttackablePoints(getActor().getPointsInRangeWith((Weapon)item));
        }
        else
        {
            level.getPanelEffectsTile().reset();
        }
    }
    
    @Override
    protected void performAction(int index) {
        cursor.hideCursor();
//        level.initiateBattle(battle);
        level.runBattle(battle);
        cursor.endAction();
    }
}
