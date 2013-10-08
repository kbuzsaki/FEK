/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Game;
import Game.Sound.SoundManager;
import java.io.Serializable;

public class GameSettings implements Serializable {
    public static final int MAX_VOLUME = 100;
    public static final int MIN_VOLUME = 0;
    
    private transient Game game;
    private transient SoundManager soundManager;
    private KeyMapper keyMapper;
    private boolean musicOn;
    private int musicVolume;
    private boolean soundEffectsOn;
    private int soundEffectsVolume;
    private int zoom;

    public GameSettings() {
        resetDefault();
    }
    
    public void setGame(Game game, SoundManager soundManager) {
        this.game = game;
        this.soundManager = soundManager;
    }
    
    public KeyMapper getKeyMapper() {
        return keyMapper;
    }
    
    public boolean isMusicOn() {
        return musicOn;
    }
    /*  */ void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
        
        if(soundManager != null)
            soundManager.setMusicOn(musicOn);
    }
    public int getMusicVolume() {
        return musicVolume;
    }
    /*  */ void setMusicVolume(int musicVolume) {
        if(musicVolume < MIN_VOLUME || musicVolume > MAX_VOLUME)
        {
            throw new IllegalArgumentException("Volume must be between " 
                    + MIN_VOLUME + " and " + MAX_VOLUME + ": " + musicVolume);
        }
        this.musicVolume = musicVolume;
        
        if(soundManager != null)
            soundManager.setMusicVolume(musicVolume);
    }
    public boolean isSoundEffectsOn() {
        return soundEffectsOn;
    }
    /*  */ void setSoundEffectsOn(boolean soundEffectsOn) {
        this.soundEffectsOn = soundEffectsOn;
        
        if(soundManager != null)
            soundManager.setSoundEffectsOn(soundEffectsOn);
    }
    public int getSoundEffectsVolume() {
        return soundEffectsVolume;
    }
    /*  */ void setSoundEffectsVolume(int soundEffectsVolume) {
        if(soundEffectsVolume < MIN_VOLUME || soundEffectsVolume > MAX_VOLUME)
        {
            throw new IllegalArgumentException("Volume must be between " 
                    + MIN_VOLUME + " and " + MAX_VOLUME + ": " + soundEffectsVolume);
        }
        this.soundEffectsVolume = soundEffectsVolume;
        
        if(soundManager != null)
            soundManager.setSoundEffectsVolume(soundEffectsVolume);
    }
    
    public int getZoom() {
        return zoom;
    }
    /*  */ void setZoom(int zoom) {
        this.zoom = zoom;
        
        if(game != null)
            game.setZoom(zoom);
    }
    
    public final void resetDefault() {
        if(keyMapper == null)
        {
            keyMapper = new KeyMapper();
        }
        else
        {
            keyMapper.resetDefaultBindings();
        }
        setMusicOn(true);
        setMusicVolume(50);
        setSoundEffectsOn(true);
        setSoundEffectsVolume(50);
        setZoom(1);
    }
}
