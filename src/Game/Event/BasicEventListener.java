/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Game.Game;
import Game.Level;
import Game.Sound.DefaultTrack;
import Game.Sound.SoundManager;
import Units.Faction;
import Units.Inventory;
import Units.Items.Equipment;
import Units.Items.ItemFactory;
import Units.Items.WeaponType;
import Units.Unit;
import Units.UnitClass;
import javax.swing.SwingUtilities;

public class BasicEventListener extends LevelEventListener {
    
    public BasicEventListener(Level level, SoundManager soundManager) {
        super(level, soundManager);
    }
    
    @Override
    public void initializeLevel() {
        soundManager.setMusic(DefaultTrack.COMPANIONS);
        level.setFactionPhase(Faction.BLUE);
        Unit bandit1 = new Unit(soundManager, level.getMap(), 6, 2, Faction.RED, UnitClass.BANDIT, 1, new Inventory(ItemFactory.axeIron(), ItemFactory.axeSteel(), ItemFactory.axeHand()));
        bandit1.getStats().setWeaponXP(WeaponType.AXE, Equipment.WEAPON_LEVEL_S);
        Unit bandit2 = new Unit(soundManager, level.getMap(), 4, 4, Faction.RED, UnitClass.BANDIT, 1, new Inventory(ItemFactory.axeIron(), ItemFactory.axeSteel(), ItemFactory.vulnerary()));
        bandit2.getStats().setWeaponXP(WeaponType.AXE, Equipment.WEAPON_LEVEL_S);
        Unit merc1 =   new Unit(soundManager, level.getMap(), 5, 5, Faction.BLUE, UnitClass.MERCENARY, 1, new Inventory(ItemFactory.swordIron(), ItemFactory.swordSteel(), ItemFactory.vulnerary()));
        merc1.getStats().setWeaponXP(WeaponType.SWORD, Equipment.WEAPON_LEVEL_S);
        Unit merc2 =   new Unit(soundManager, level.getMap(), 6, 6, Faction.BLUE, UnitClass.MERCENARY, 1, new Inventory(ItemFactory.swordIron(), ItemFactory.swordSteel()));
        merc2.getStats().setWeaponXP(WeaponType.SWORD, Equipment.WEAPON_LEVEL_S);
        Unit cleric1 = new Unit(soundManager, level.getMap(), 7, 5, Faction.BLUE, UnitClass.CLERIC, 1, new Inventory(ItemFactory.staffHeal(), ItemFactory.staffMend()));
        cleric1.getStats().setWeaponXP(WeaponType.STAFF, Equipment.WEAPON_LEVEL_S);
        level.addUnit(bandit1);
        level.addUnit(bandit2);
        level.addUnit(merc1);
        level.addUnit(merc2);
        level.addUnit(cleric1);
    }

    // also never called
    @Override
    public void handleMoveEvent(MoveEvent event) {
        
    }
    @Override
    // never called
    public void handleMoveCompletionEvent(MoveCompletionEvent event) {
        
    }
    @Override
    public void handleDeathEvent(DeathEvent event) {
        boolean hasAllies = false;
        for (Unit unit : level.getUnitList())
        {
            if ((event.getDeadUnit().getFaction() == unit.getFaction())
                    && (unit != event.getSource()))
            {
                hasAllies = true;
                break;
            }
        }
        if(!hasAllies)
        {
            Game.logInfo(event.getDeadUnit().getFaction().name + " has lost.");
        }
            
    }
    @Override
    public void handleEndTurnEvent(EndTurnEvent event) {
        Game.logInfo("Turn " + event.getTurnCount() + ": " 
                + event.getLevel().getCurrentPhaseFaction().name + "'s turn.");
        if(event.getTurnCount() == 4) {
            Unit merc =   new Unit(soundManager, level.getMap(), 6, 6, Faction.BLUE, 
                    UnitClass.MERCENARY, 1, new Inventory(ItemFactory.swordIron(), ItemFactory.swordSteel()));
            merc.getStats().setWeaponXP(WeaponType.SWORD, Equipment.WEAPON_LEVEL_S); 
            level.addUnit(merc);
        }
    }
    @Override
    public void handlePhaseChangeEvent(final PhaseChangeEvent event) {
        System.out.println("Ending faction: " + event.getEndingFaction());
        System.out.println("Starting faction: " + event.getStartingFaction());
        if(event.getStartingFaction() == Faction.RED)
        {
            runAiMoves(event.getStartingFaction());
        }
    }
    
}
