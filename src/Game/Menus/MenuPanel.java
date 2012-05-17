/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Command;
import Game.Command;
import Game.Sound.SoundManager;
import Maps.Map;
import Sprites.Animation;
import Sprites.BoardElement;
import Sprites.Character;
import Sprites.ImageComponent;
import java.awt.Dimension;
import javax.swing.JPanel;

public abstract class MenuPanel extends JPanel implements Menu{
    private final static int tileS = Map.tileS;
    private final static int[][] sequence = {{0,1,2,3,3,2,1,0}};
    private final static int POINTER_OFFSET = 12;
    
    private ImageComponent backgroundTop;
    private ImageComponent backgroundBottom;
    private final ImageComponent menuEntries[];
    private Animation pointer;
    protected Dimension mapSize;
    protected int index = 0; // the index of the user's choice in the menu
    
    private boolean isOpen = false;
    private int MAX_INDEX;
    
    private SoundManager soundManager;
    
    public MenuPanel(Dimension mapSize, SoundManager soundManager, String[] menuEntries) {
        this.mapSize = mapSize;
        this.soundManager = soundManager;
        this.MAX_INDEX = menuEntries.length;
        
        pointer = new Animation("pointer", 1, 4, sequence);
        setPointerIndex(0);
        add(pointer);
        
        this.menuEntries = new ImageComponent[10];
        for (int i = 0;i < this.menuEntries.length; i++)
        {
            if (i < menuEntries.length)
            {
                this.menuEntries[i] = Character.getImageComponent(menuEntries[i]);
            }
            else
            {
                this.menuEntries[i] = ImageComponent.blank;
            }
            this.menuEntries[i].setLocation(POINTER_OFFSET + 4, 7 + 16*i);
            add(this.menuEntries[i]);
        }
        
        backgroundTop = new ImageComponent("resources/gui/window/menuBlankTop.png");
        backgroundTop.setBounds(POINTER_OFFSET, 0, backgroundTop.getWidth(), 16*menuEntries.length + 2);
        add(backgroundTop);
        
        backgroundBottom = new ImageComponent("resources/gui/window/menuBlankBottom.png");
        backgroundBottom.setLocation(POINTER_OFFSET, 16*menuEntries.length + 2);
        add(backgroundBottom);
        
        setLayout(null);
        setOpaque(false);
        setVisible(false);
        
        setSize(backgroundTop.getWidth() + POINTER_OFFSET, backgroundTop.getHeight() + backgroundBottom.getHeight());
        setPreferredSize(getSize());
    }
    
    public void reconstructMenu(String[] menuEntries) {
        MAX_INDEX = menuEntries.length;
        
        backgroundTop.setSize(backgroundTop.getWidth(), 16*menuEntries.length + 2);
        backgroundBottom.setLocation(POINTER_OFFSET, 16*menuEntries.length + 2);
        
        for (int i = 0;i < this.menuEntries.length; i++)
        {
            if (i < menuEntries.length)
            {
                this.menuEntries[i].setImage(Character.getImageComponent(menuEntries[i]));
            }
            else 
            {
                this.menuEntries[i].setImage();
            }
        }
    }
    
    public final void open(int x) {
        soundManager.playSoundEffect(SoundManager.confirm);
        updatePosition(x);
        setVisible(true);
        isOpen = true;
    }
    public void close() {
        setVisible(false);
        isOpen = false;
        setPointerIndex(0);
    }
    public final boolean isOpen() {
        return isOpen;
    }
    public abstract void cancel();
    
    public final void setTick(int tick) {
        pointer.setTick(tick);
    }
    /**
     * Handles input given by the cursor
     * @param button the command sent
     * @return true if the menu is finished taking commands
     */
    public final boolean keyHandle(Command button) {
        switch (button)
        {
            case A:
                soundManager.playSoundEffect(SoundManager.confirm);

                performAction();
                close();
                return true;
            case B:
                soundManager.playSoundEffect(SoundManager.cancel);
                cancel();
                return true;
            case UP:
                soundManager.playSoundEffect(SoundManager.menuBlip1);
                setPointerIndex(index - 1);
                break;
            case DOWN:
                soundManager.playSoundEffect(SoundManager.menuBlip1);
                setPointerIndex(index + 1);
                break;
        }
        return false;
    }
    protected abstract void performAction();
    private void setPointerIndex(int index) {
        if (index < 0)
        {
            this.index = (MAX_INDEX - 1);
        }
        else 
        {
            this.index = (index % MAX_INDEX);
        }
        pointer.setLocation(0, 7 + this.index * 16);
    }
    
    // FIXME: Magic numbers (make work for different menu sizes?)
    public void updatePosition(int x) {
        // If the cursor is on the left half of the screen
        if (x + 1 < (mapSize.width / tileS) / 2) 
        {
            setLocation((mapSize.width * 3/4), mapSize.height * 1/6);
        }
        else // Otherwise the cursor must be on the right half of the screen
        {
            setLocation((mapSize.width * 1/4) - getWidth(), mapSize.height * 1/6);
        }
    }
}
