/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Menus;

import Game.Sound.SoundManager;
import Sprites.ImageComponent;
import Sprites.SpriteUtil;
import javax.swing.JPanel;

public class Notification extends JPanel implements GamePanel {
    public static final String breakSoundEffect = null;
    
    private ImageComponent background;
    private ImageComponent content;
    private String soundEffect;

    public Notification(ImageComponent content, String soundEffect) {
        setLayout(null);
        setVisible(false);
        this.content = content;
        this.soundEffect = soundEffect;
        
        background = new ImageComponent(
                panelFactory.getPanel(
                    panelFactory.getMinimumUnitWidth(this.content.getWidth()), 
                    panelFactory.getMinimumUnitHeight(this.content.getHeight())));
        
        add(this.content);
        add(background);
        
        setSize(background.getSize());
        SpriteUtil.center(this.content);
    }
    
    public boolean hasSoundEffect() {
        return soundEffect != null;
    }
    public String getSoundEffect() {
        return soundEffect;
    }

    public void open(SoundManager soundManager) {
        updatePosition();
        setVisible(true);
        if(hasSoundEffect())
            soundManager.playSoundEffect(getSoundEffect());
    }
    @Override
    public void close() {
        setVisible(false);
    }

    @Override
    public boolean isOpen() {
        return isVisible();
    }

    protected void updatePosition() {
        SpriteUtil.center(this);
    }

    @Override
    public void setTick(int tick) {}
    
}
