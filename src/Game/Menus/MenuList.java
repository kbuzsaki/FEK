/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Sound.SoundManager;
import Sprites.Animation;
import Sprites.AnimationFactory;
import Sprites.ImageComponent;
import Sprites.Panels.GameScreen;
import java.awt.image.BufferedImage;

public abstract class MenuList extends Menu{
    static final int DISPLAY_WIDTH_UNIT_ITEM = 12;
    static final int DISPLAY_WIDTH_UNIT_TEXT = 4;
    private static final int defaultUnitWidth = DISPLAY_WIDTH_UNIT_TEXT;
    private static final int defaultUnitHeight = 10;
    
    static final int POINTER_OFFSET = 12;
    protected static final int ENTRY_HEIGHT = panelFactory.getPatternWidth()*2;
    protected static final int TEXT_OFFSET = 3;
    
    protected final ImageComponent background;
    private final ImageComponent[] menuEntries;
    private Animation pointer;
    
    public MenuList(GameScreen gameScreen, SoundManager soundManager, 
            int unitWidth, int unitHeight, ImageComponent[] menuEntries) {
        super(gameScreen, soundManager);
        setMaxIndex(menuEntries.length);
        
        pointer = AnimationFactory.newPointer();
        add(pointer);
        
        this.menuEntries = new ImageComponent[10];
        for (int i = 0;i < this.menuEntries.length; i++)
        {
            if (i < menuEntries.length)
            {
                this.menuEntries[i] = menuEntries[i];
            }
            else
            {
                this.menuEntries[i] = new ImageComponent();
            }
            this.menuEntries[i].setLocation(
                    POINTER_OFFSET + panelFactory.getBorderLeft(), 
                    panelFactory.getBorderTop() + ENTRY_HEIGHT*i);
            add(this.menuEntries[i]);
        }
        
        background = new ImageComponent(panelFactory.getPanel(unitWidth, unitHeight));
        background.setLocation(POINTER_OFFSET, 0);
        add(background);
        
        setSize(background.getWidth() + POINTER_OFFSET, background.getHeight());
        setPreferredSize(getSize());
    }
    public MenuList(GameScreen gameScreen, SoundManager soundManager, ImageComponent[] menuEntries) {
        this(gameScreen, soundManager, defaultUnitWidth, defaultUnitHeight, menuEntries);
    }
    
    protected void reconstructMenu(ImageComponent[] menuEntries) {
        setMaxIndex(menuEntries.length);
        
        background.setImage(panelFactory.getPanel(
                panelFactory.getUnitWidth(background.getWidth()), 
                menuEntries.length*2));
        setSize(background.getWidth() + POINTER_OFFSET, background.getHeight());
        setPreferredSize(getSize());
        
        for (int i = 0;i < this.menuEntries.length; i++)
        {
            if (i < menuEntries.length)
            {
                this.menuEntries[i].setImage(menuEntries[i]);
            }
            else 
            {
                this.menuEntries[i].setBlank();
            }
        }
    }
    @Override
    protected void updateIndex(int index) {
        pointer.setLocation(0, 7 + index * 16);
    }
    
    @Override
    protected void setAcceptingCommands(boolean acceptingCommands) {
        super.setAcceptingCommands(acceptingCommands);
        pointer.setVisible(acceptingCommands);
    }
    
    @Override
    public void setTick(int tick) {
        pointer.setTick(tick);
    }
    
    protected static ImageComponent[] getMenuComponents(String[] menuEntries) {
        ImageComponent[] menuComponents = new ImageComponent[menuEntries.length];
        
        for(int i = 0; i < menuEntries.length; i++)
        {
            ImageComponent entryText = Sprites.Text.getImageComponent(menuEntries[i]);
            BufferedImage entryImage = new BufferedImage(entryText.getWidth(), ENTRY_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            entryImage.createGraphics().drawImage(entryText.getImage(), 0, TEXT_OFFSET, null);
            
            menuComponents[i] = new ImageComponent(entryImage);
        }
        
        return menuComponents;
    } 
}
