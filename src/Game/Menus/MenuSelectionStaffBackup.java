/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Cursor;
import Game.Level;
import Game.Sound.SoundManager;
import Game.StaffInteraction;
import Sprites.AnimationMapUnit;
import Sprites.Text;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import Units.Items.Item;
import Units.Items.Staff;
import Units.UnitClass;
import javax.swing.JPanel;

public class MenuSelectionStaffBackup extends MenuSelection{
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
    
    private static final int ANIM_X = 9;
    private static final int ANIM_OFFSET_Y = 16;
    private static final int namePanelUnitHeight = 4;
    private static final int offset = 16;
    private static final int unitInfoHeight = 20;
    
    public MenuSelectionStaffBackup(Level level, GameScreen gameScreen, 
            SoundManager soundManager, Cursor cursor) {
        super(level, gameScreen, soundManager, cursor);
        
        background = new ImageComponent(panelFactory.getNamePanel(namePanelUnitHeight));
        setSize(background.getSize());
        
        unitInfoAnim = new AnimationMapUnit(UnitClass.BANDIT.spriteFileName, null);
        unitInfoAnim.setLocationCentered(ANIM_X, panelFactory.getNamePlateHeight() - ANIM_OFFSET_Y);
        name = Text.getImageComponent("Kent");
        name.setLocation(28, 7); // names are always at this set of coords
        
        statName = Text.getImageComponent("HP", Text.YELLOW);
        statName.setLocation(4, unitInfoHeight + 3);
        statStartSeparator = Text.getImageComponent("/", Text.YELLOW);
        statStartSeparator.setLocation(44, unitInfoHeight + 4);
        statStartCurrent = Text.getImageComponent("22");
        statStartCurrent.setLocation(44 - statStartCurrent.getWidth(), unitInfoHeight + 4);
        statStartTotal = Text.getImageComponent("39");
        statStartTotal.setLocation(52, unitInfoHeight + 4);
        
        statEndSeparator = Text.getImageComponent("/", Text.YELLOW);
        statEndSeparator.setLocation(44, unitInfoHeight + 4 + offset);
        statEndCurrent = Text.getImageComponent("22");
        statEndCurrent.setLocation(44 - statEndCurrent.getWidth(), unitInfoHeight + 4 + offset);
        statEndTotal = Text.getImageComponent("39");
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
    public void openQuietly(CancelListener cancelListener) {
        openQuietly(level.getMap().getUnitsAt(getActor().getStaffPoints()), cancelListener);
        level.getPanelEffectsTile().updateStaffPoints(getActor().getStaffPoints());
    }
    @Override
    protected void reconstructMenu(int index) {
        staffInteraction = new StaffInteraction(cursor.getSelectedUnit(), getTarget(index));
        
        unitInfoAnim.loadAnimation(getTarget(index).getMapAnim());
        name.setImage(Text.getImageComponent(getTarget(index).getName()));
        
        statName.setImage(Text.getImageComponent(staffInteraction.getStatName(), Text.YELLOW));
        statStartCurrent.setImage(Text.getImageComponent(String.valueOf(staffInteraction.getStatStartCurrent())));
        statStartCurrent.setLocation(44 - statStartCurrent.getWidth(), unitInfoHeight + 4);
        statStartTotal.setImage(Text.getImageComponent(String.valueOf(staffInteraction.getStatStartTotal())));
        
        statEndCurrent.setImage(Text.getImageComponent(String.valueOf(staffInteraction.getStatEndCurrent())));
        statEndCurrent.setLocation(44 - statEndCurrent.getWidth(), unitInfoHeight+offset+4);
        statEndTotal.setImage(Text.getImageComponent(String.valueOf(staffInteraction.getStatEndTotal())));
    }
    @Override
    public void setTick(int tick) {
        unitInfoAnim.setTick(tick);
    }
    
    @Override
    public boolean isTargetable(Item item) {
        if(item instanceof Staff)
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
        if(item instanceof Staff)
        {
            level.getPanelEffectsTile().updateStaffPoints(getActor().getPointsInRangeWith((Staff)item));
        }
        else
        {
            level.getPanelEffectsTile().reset();
        }
    }
    
    @Override
    protected void performAction(int index) {
        cursor.hideCursor();
        level.initiateStaffInteraction(staffInteraction);
    }
    
}
