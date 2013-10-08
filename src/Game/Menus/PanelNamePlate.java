/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import static Game.Menus.MenuList.ENTRY_HEIGHT;
import static Game.Menus.MenuList.TEXT_OFFSET;
import Sprites.AnimationMapUnit;
import Sprites.ImageComponent;
import Sprites.Text;
import Units.Attribute;
import Units.Unit;
import Units.UnitClass;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class PanelNamePlate extends JPanel implements GamePanel {
    
    private ImageComponent background;
    private AnimationMapUnit unitAnim;
    private ImageComponent name;
    
    private ImageComponent[] entries;
    
    private static final int ANIM_X = 9;
    private static final int ANIM_OFFSET_Y = 16;
    private static final int namePanelUnitHeight = 4;
    
    public PanelNamePlate() {
        background = new ImageComponent(panelFactory.getNamePanel(namePanelUnitHeight));
        
        unitAnim = new AnimationMapUnit(UnitClass.BANDIT.spriteFileName, null);
        unitAnim.setLocationCentered(ANIM_X, panelFactory.getNamePlateHeight() - ANIM_OFFSET_Y);
        name = Text.getImageComponent("Kent");
        name.setLocation(28, 7); // names are always at this set of coords
        
        entries = new ImageComponent[10];
        for(int i = 0; i < entries.length; i++)
        {
            entries[i] = new ImageComponent();
            entries[i].setLocation(panelFactory.getBorderLeft(), 
                    panelFactory.getNamePlateHeight() + MenuItem.ITEM_COMPONENT_HEIGHT*i);
        }
        
        add(unitAnim);
        add(name);
        for(ImageComponent imageComponent : entries)
            add(imageComponent);
        add(background);
        
        setSize(background.getSize());
        setLayout(null);
        setOpaque(false);
        setVisible(false);
    }
    
    void open() {
        setVisible(true);
    }
    @Override
    public void close() {
        setVisible(false);
    }
    @Override
    public boolean isOpen() {
        return isVisible();
    }
    @Override
    public void setTick(int tick) {
        unitAnim.setTick(tick);
    }
    
    void setInfo(Unit unit, BufferedImage... entries) {
        unitAnim.loadAnimation(unit.getMapAnim());
        name.setImage(Text.getImageComponent(unit.getName()));
        
        for(int i = 0; i < this.entries.length; i++)
            if(i < entries.length)
                this.entries[i].setImage(entries[i]);
            else 
                this.entries[i].setBlank();
        
        background.setImage(panelFactory.getNamePanel(entries.length*2));
        setSize(background.getSize());
    }
    void setInfo(Unit unit, ImageComponent... entries) {
        BufferedImage[] imageEntries = new BufferedImage[entries.length];
        for(int i = 0; i < imageEntries.length; i++)
            imageEntries[i] = entries[i].getImage();
        
        setInfo(unit, imageEntries);
    }
    
    int getWriteableWidth() {
        return background.getWidth() - panelFactory.getBorderX();
    }

    private static final BufferedImage SEPARATOR = Text.getImageComponent("/", Text.YELLOW).getImage();
    BufferedImage getCappedStatImage(String name, int value, int valueCap) {
        BufferedImage cappedStatImage = new BufferedImage(
                getWriteableWidth(), ENTRY_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D cappedStatGraphics = cappedStatImage.createGraphics();
        
        BufferedImage statName = Text.getImageComponent(name, Text.YELLOW).getImage();
        BufferedImage statValue = Text.getImageComponent(String.valueOf(value)).getImage();
        BufferedImage statCap = Text.getImageComponent(String.valueOf(valueCap)).getImage();
        
        int capX = cappedStatImage.getWidth() - statCap.getWidth();
        int separatorX = capX - SEPARATOR.getWidth();
        int valueX = separatorX - statValue.getWidth();
        
        cappedStatGraphics.drawImage(statName, 0, TEXT_OFFSET, null);
        cappedStatGraphics.drawImage(statValue, valueX, TEXT_OFFSET, null);
        cappedStatGraphics.drawImage(SEPARATOR, separatorX, TEXT_OFFSET, null);
        cappedStatGraphics.drawImage(statCap, capX, TEXT_OFFSET, null);
        
        return cappedStatImage;
    }
    BufferedImage getAttributeImage(Attribute attribute) {
        return getCappedStatImage(attribute.getName(), attribute.get(), attribute.getValT());
    }
    
    BufferedImage getStatImage(String name, int value) {
        BufferedImage statImage = new BufferedImage(
                getWriteableWidth(), ENTRY_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D statImageGraphics = statImage.createGraphics();
        
        BufferedImage statName = Text.getImageComponent(name, Text.YELLOW).getImage();
        BufferedImage statValue = Text.getImageComponent(String.valueOf(value)).getImage();
        
        int valueX = statImage.getWidth() - statValue.getWidth();
        
        statImageGraphics.drawImage(statName, 0, TEXT_OFFSET, null);
        statImageGraphics.drawImage(statValue, valueX, TEXT_OFFSET, null);
        
        return statImage;
    }
}
