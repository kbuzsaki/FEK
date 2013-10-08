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

public class MenuSelectionStaff extends MenuSelection{
    private PanelNamePlate infoPanel;
    
    private StaffInteraction staffInteraction;
    
    private static final int ANIM_X = 9;
    private static final int ANIM_OFFSET_Y = 16;
    private static final int namePanelUnitHeight = 4;
    private static final int offset = 16;
    private static final int unitInfoHeight = 20;
    
    public MenuSelectionStaff(Level level, GameScreen gameScreen, 
            SoundManager soundManager, Cursor cursor) {
        super(level, gameScreen, soundManager, cursor);
        
        infoPanel = new PanelNamePlate();
        
        add(infoPanel);
        
        setSize(infoPanel.getSize());
        setLayout(null);
        setOpaque(false);
        setVisible(false);
    }
    
    @Override
    void openQuietly(CancelListener cancelListener) {
        openQuietly(level.getMap().getUnitsAt(getActor().getStaffPoints()), cancelListener);
        level.getPanelEffectsTile().updateStaffPoints(getActor().getStaffPoints());
        infoPanel.open();
    }
    @Override
    public void close() {
        super.close();
        infoPanel.close();
    }
    @Override
    protected void reconstructMenu(int index) {
        staffInteraction = new StaffInteraction(cursor.getSelectedUnit(), getTarget(index));
        infoPanel.setInfo(getTarget(index), 
                infoPanel.getAttributeImage(getTarget(index).getStats().getHP()));
    }
    @Override
    public void setTick(int tick) {
        infoPanel.setTick(tick);
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
