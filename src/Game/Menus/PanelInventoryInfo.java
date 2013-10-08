/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import static Game.Menus.MenuList.*;
import Sprites.Animation;
import Sprites.AnimationFactory;
import Sprites.Text;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import Units.Items.Item;
import Units.Items.Weapon;
import Units.Unit;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PanelInventoryInfo extends JPanel implements GamePanel {
    private static int backgroundUnitWidth = 12;
    private static int backgroundUnitHeight = 6;
    private static String defaultMessage = "This is test text, it shouldn't appear.";
    
    private static final int COLUMN_ONE_START = 2;
    private static final int COLUMN_ONE_END = COLUMN_ONE_START + 36;
    private static final int COLUMN_TWO_START = COLUMN_ONE_END + 6;
    private static final int COLUMN_TWO_END = COLUMN_TWO_START + 48;
    private static final int ROW_ONE_START = ENTRY_HEIGHT*1 + TEXT_OFFSET;
    private static final int ROW_TWO_START = ENTRY_HEIGHT*2 + TEXT_OFFSET;
    private static final int ARROW_OVERLAP = 1;
    private static final int ARROW_DY = 1;
    
    private static final BufferedImage attackLabel = Text.getImageComponent("Atk").getImage();
    private static final BufferedImage hitLabel = Text.getImageComponent("Hit").getImage();
    private static final BufferedImage critLabel = Text.getImageComponent("Crit").getImage();
    private static final BufferedImage avoidLabel = Text.getImageComponent("Avoid").getImage();
    
    private GameScreen gameScreen;
    
    private ImageComponent background;
    private ImageComponent content;
    private JPanel animationsPanel;
    private ArrayList<Animation> animations;
    
    public PanelInventoryInfo(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        
        background = new ImageComponent(panelFactory.getPanel(
                backgroundUnitWidth, backgroundUnitHeight));
        
        content = Sprites.Text.getWrappedImageComponent(
                defaultMessage, getWriteableWidth() );
        content.setLocation(panelFactory.getBorderLeft(), 
                panelFactory.getBorderTop());
        
        animationsPanel = new JPanel();
        animationsPanel.setBounds(
                panelFactory.getBorderLeft(), 
                panelFactory.getBorderTop(),
                background.getWidth() - panelFactory.getBorderLeft(),
                background.getHeight() - panelFactory.getBorderTop());
        animationsPanel.setOpaque(false);
        
        animations = new ArrayList();
        
        add(animationsPanel);
        add(content);
        add(background);
        
        setLayout(null);
        setOpaque(false);
        setVisible(false);
        setSize(background.getSize());
        setPreferredSize(getSize());
    }
    
    public void open() {
        clearAnimations();
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
        for(Animation animation : animations)
        {
            animation.setTick(tick);
        }
    }
    
    void updatePosition() {
        // If the cursor is on the left half of the screen
        if (gameScreen.cursorIsOnLeft()) 
        {
            // set the panel's rightmost edge to be 1/12th from the edge
            // set the panel's bottommost edge to be 1/6th from the edge
            setLocation(
                    gameScreen.getScreenWidth() * 11/12 - getWidth(), 
                    gameScreen.getScreenHeight() * 5/6 - getHeight());
        }
        else // Otherwise the cursor must be on the right half of the screen
        {
            // set the panel's leftmost edge to be 1/12th from the edge
            // set the panel's bottommost edge to be 1/6th from the edge
            setLocation(
                    gameScreen.getScreenWidth() * 1/12, 
                    gameScreen.getScreenHeight() * 5/6 - getHeight());
        }
    }
    
    public void setItemInfo(Unit unit, Item item) {
        clearAnimations();
        
        if(item instanceof Weapon) {
            // process as a weapon
            content.setImage(getWeaponImage(unit, (Weapon)item));
            setWeaponComparisonAnimations(unit, (Weapon)item);
            // arrow stuff
        }
        else {
            // process as other stuff
            content.setImage(getDescriptionImage(item));
            // arrow stuff
        }
        
        addAnimations();
    }
    
    private void clearAnimations() {
        for(Animation animation : animations)
        {
            animationsPanel.remove(animation);
        }
        animations.clear();
    }
    private void addAnimations() {
        for(Animation animation : animations)
        {
            animationsPanel.add(animation);
        }
    }
    private void setWeaponComparisonAnimations(Unit unit, Weapon weapon) {
        Animation attackAnim = getComparisonAnimation(
                unit.getDamage(weapon), 
                unit.getDamage(unit.getEquipedWeapon()));
        attackAnim.setLocation(
                COLUMN_ONE_END - ARROW_OVERLAP, 
                ROW_ONE_START + ARROW_DY);
        animations.add(attackAnim);
        
        Animation hitAnim = getComparisonAnimation(
                unit.getHit(weapon), 
                unit.getHit(unit.getEquipedWeapon()));
        hitAnim.setLocation(
                COLUMN_ONE_END - ARROW_OVERLAP, 
                ROW_TWO_START + ARROW_DY);
        animations.add(hitAnim);
        
        Animation critAnim = getComparisonAnimation(
                unit.getCriticalChance(weapon), 
                unit.getCriticalChance(unit.getEquipedWeapon()));
        critAnim.setLocation(
                COLUMN_TWO_END - ARROW_OVERLAP, 
                ROW_ONE_START + ARROW_DY);
        animations.add(critAnim);
        
        Animation avoidAnim = getComparisonAnimation(
                unit.getAvoid(weapon), 
                unit.getAvoid(unit.getEquipedWeapon()));
        avoidAnim.setLocation(
                COLUMN_TWO_END - ARROW_OVERLAP, 
                ROW_TWO_START + ARROW_DY);
        animations.add(avoidAnim);
    }
    private static Animation getComparisonAnimation(int valOne, int valTwo) {
        if(valOne > valTwo)
            return AnimationFactory.newArrowUp();
        else if(valOne < valTwo) 
            return AnimationFactory.newArrowDown();
        else
            return AnimationFactory.newBlankAnimation();
    }
    
    private BufferedImage getWeaponImage(Unit unit, Weapon weapon) {
        BufferedImage image = new BufferedImage(
                getWriteableWidth(), 
                getWriteableHeight(), 
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        
        // get the weapon's stats as images
        BufferedImage attackValue = Text.getImageComponent(String.valueOf(
                unit.getDamage(weapon)), Text.BLUE).getImage();
        BufferedImage hitValue = Text.getImageComponent(String.valueOf(
                unit.getHit(weapon)), Text.BLUE).getImage();
        BufferedImage critValue = Text.getImageComponent(String.valueOf(
                unit.getCriticalChance(weapon)), Text.BLUE).getImage();
        BufferedImage avoidValue = Text.getImageComponent(String.valueOf(
                unit.getAvoid(weapon)), Text.BLUE).getImage();
        
        // paint the stats to the panel
        imageGraphics.drawImage(attackLabel, COLUMN_ONE_START, ROW_ONE_START, null);
        imageGraphics.drawImage(attackValue, COLUMN_ONE_END - attackValue.getWidth(), ROW_ONE_START, null);
        
        imageGraphics.drawImage(hitLabel, COLUMN_ONE_START, ROW_TWO_START, null);
        imageGraphics.drawImage(hitValue, COLUMN_ONE_END - hitValue.getWidth(), ROW_TWO_START, null);
        
        imageGraphics.drawImage(critLabel, COLUMN_TWO_START, ROW_ONE_START, null);
        imageGraphics.drawImage(critValue, COLUMN_TWO_END - critValue.getWidth(), ROW_ONE_START, null);
        
        imageGraphics.drawImage(avoidLabel, COLUMN_TWO_START, ROW_TWO_START, null);
        imageGraphics.drawImage(avoidValue, COLUMN_TWO_END - avoidValue.getWidth(), ROW_TWO_START, null);
        
        return image;
    }
    private ImageComponent getDescriptionImage(Item item) {
        return Text.getWrappedImageComponent(
                item.getDescription(), getWriteableWidth());
    }
    private int getWriteableWidth() {
        return background.getWidth() - (panelFactory.getBorderX());
    }
    private int getWriteableHeight() {
        return background.getHeight() - panelFactory.getBorderY();
    }
}
