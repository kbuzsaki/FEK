/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import java.io.Serializable;

public class GameSettings implements Serializable {
    private KeyMapper keyMapper;
    private boolean musicOn;
    private boolean soundEffectsOn;
    private int zoom;

    public GameSettings() {
        resetDefault();
    }
    
    public KeyMapper getKeyMapper() {
        return keyMapper;
    }
    
    public boolean isMusicOn() {
        return musicOn;
    }
    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }
    public boolean isSoundEffectsOn() {
        return soundEffectsOn;
    }
    public void setSoundEffectsOn(boolean soundEffectsOn) {
        this.soundEffectsOn = soundEffectsOn;
    }
    
    public int getZoom() {
        return zoom;
    }
    
    public final void resetDefault() {
        keyMapper = new KeyMapper();
        musicOn = true;
        soundEffectsOn = true;
        zoom = 1;
    }
}
