/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Level;
import Game.StaffInteraction;
import Sprites.AnimationMapUnit;
import Sprites.Character;
import Sprites.ImageComponent;
import Units.UnitClass;
import java.awt.Dimension;
import javax.swing.JPanel;

public class MenuSelectionStaff extends MenuSelection{
    private ImageComponent background;
    private JPanel unitInfoPanel;
        private AnimationMapUnit unitInfoAnim;
        private ImageComponent name;
    private ImageComponent statName;
    private ImageComponent statStartSeparator;
    private ImageComponent statStartCurrent;
    private ImageComponent statStartTotal;
    private ImageComponent statEndSeparator;
    private ImageComponent statEndCurrent;
    private ImageComponent statEndTotal;
    
    private StaffInteraction staffInteraction;
    
    private static final int offset = 16;
    private static final int unitInfoHeight = 20;
    
    public MenuSelectionStaff(Level level, Dimension mapSize, Cursor cursor) {
        super(level, mapSize, cursor);
        
        background = new ImageComponent("resources/gui/window/staffMenuBlank.png");
        setSize(background.getSize());
        
        unitInfoAnim = new AnimationMapUnit(UnitClass.BANDIT.spriteFileName);
        unitInfoAnim.setLocationCentered(9, 4);
        name = Character.getImageComponent("Kent");
        name.setLocation(31, 6);
        
        statName = Character.getImageComponent("HP", Character.YELLOW);
        statName.setLocation(4, unitInfoHeight + 3);
        statStartSeparator = Character.getImageComponent("/", Character.YELLOW);
        statStartSeparator.setLocation(44, unitInfoHeight + 4);
        statStartCurrent = Character.getImageComponent("22");
        statStartCurrent.setLocation(44 - statStartCurrent.getWidth(), unitInfoHeight + 4);
        statStartTotal = Character.getImageComponent("39");
        statStartTotal.setLocation(52, unitInfoHeight + 4);
        
        statEndSeparator = Character.getImageComponent("/", Character.YELLOW);
        statEndSeparator.setLocation(44, unitInfoHeight + 4 + offset);
        statEndCurrent = Character.getImageComponent("22");
        statEndCurrent.setLocation(44 - statEndCurrent.getWidth(), unitInfoHeight + 4 + offset);
        statEndTotal = Character.getImageComponent("39");
        statEndTotal.setLocation(52, unitInfoHeight + 4 + offset);
        
        add(unitInfoAnim);
        add(name);
        add(statName);
        add(statStartSeparator);
        add(statStartCurrent);
        add(statStartTotal);
        add(statEndSeparator);
        add(statEndCurrent);
        add(statEndTotal);
        add(background);
    }
    
    @Override
    protected void reconstructMenu() {
        staffInteraction = new StaffInteraction(cursor.getSelectedUnit(), getTargetedUnit());
        
        unitInfoAnim.loadAnimation(getTargetedUnit().getMapAnim());
        name = Character.getImageComponent(getTargetedUnit().getName());
        
        statName.setImage(Character.getImageComponent(staffInteraction.getStatName(), Character.YELLOW));
        statStartCurrent.setImage(Character.getImageComponent(String.valueOf(staffInteraction.getStatStartCurrent())));
        statStartCurrent.setLocation(44 - statStartCurrent.getWidth(), unitInfoHeight + 4);
        statStartTotal.setImage(Character.getImageComponent(String.valueOf(staffInteraction.getStatStartTotal())));
        
        statEndCurrent.setImage(Character.getImageComponent(String.valueOf(staffInteraction.getStatEndCurrent())));
        statEndCurrent.setLocation(44 - statEndCurrent.getWidth(), unitInfoHeight+offset+4);
        statEndTotal.setImage(Character.getImageComponent(String.valueOf(staffInteraction.getStatEndTotal())));
    }
    
    @Override
    public void setTick(int tick) {
        unitInfoAnim.setTick(tick);
    }
    @Override
    protected void performAction() {
        // TODO: launch a staffInteraction thread instead
        level.initiateStaffInteraction(staffInteraction);
    }
    
}
