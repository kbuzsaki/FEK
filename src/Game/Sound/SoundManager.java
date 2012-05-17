/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Sound;

import Game.Settings.GameSettings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SoundManager implements PlaybackListener, Serializable {
    public static final String BASE_DIRECTORY = "resources/sound/";
    public static final String SFX_DIRECTORY = BASE_DIRECTORY + "soundEffects/";
    public static final String MUSIC_DIRECTORY = BASE_DIRECTORY + "music/";
    public static final String MP3 = ".mp3";
    
    // Sound Effects
    public static final String cancel =     SFX_DIRECTORY + "cancel" + MP3;
    public static final String confirm =    SFX_DIRECTORY + "confirm" + MP3;
    public static final String cursorBlip = SFX_DIRECTORY + "cursorBlip" + MP3;
    public static final String heal =       SFX_DIRECTORY + "heal" + MP3;
    public static final String hit =        SFX_DIRECTORY + "hit" + MP3;
    public static final String hitKill =    SFX_DIRECTORY + "hitKill" + MP3;
    public static final String hitCrit =    SFX_DIRECTORY + "hitCrit" + MP3;
    public static final String hitCritKill =SFX_DIRECTORY + "hitCritKill" + MP3;
    public static final String menuBlip1 =  SFX_DIRECTORY + "menuBlip1" + MP3;
    public static final String miss =       SFX_DIRECTORY + "miss" + MP3;
    public static final String moveFly =    SFX_DIRECTORY + "moveFly" + MP3;
    public static final String select =     SFX_DIRECTORY + "select" + MP3;
    
    // Music
    public static final String Companions = MUSIC_DIRECTORY + "008" + MP3;
    
    private GameSettings settings;
    
    private PlayerThread musicThread;
    private String musicFilepath; // holds the background music filepath in case musicThread is null
    private ArrayList<PlayerThread> sfxThreadList;
    
    public SoundManager(GameSettings settings) {
        this.settings = settings;
        sfxThreadList = new ArrayList();
    }
    
    public PlayerThread playMusic(String filepath) {
        musicFilepath = filepath;
        if(settings.isMusicOn()) // if music should play
        {
            if (musicThread != null) // if there's already background music
            {
                musicThread.stopPlaying(); // stop the background music
            }
            musicThread = new PlayerThread(filepath, this, true); // play the requested music
            musicThread.start();
            return musicThread;
        }
        return null;
    }
    public PlayerThread playSoundEffect(String filepath, boolean shouldLoop) {
        if(settings.isSoundEffectsOn()) // if sound effects should play
        {
//            for (PlayerThread sfxThread : sfxThreadList)
//            {
//                if (sfxThread.getFilename().equals(filepath))
//                {
//                    sfxThread.stopPlaying();
//                }
//            }
            Game.Game.log("Playing sound effect: " + filepath);
            PlayerThread sfxThread = new PlayerThread(filepath, this, shouldLoop);
            sfxThread.start(); // play the music 
            // this might have the sound effect finish before it is added to the list of sfx (memory leak)
            sfxThreadList.add(sfxThread); // add the sound effect to the list of sound effects
            return sfxThread;
        }
        return null;
    }
    public PlayerThread playSoundEffect(String filepath) {
        return playSoundEffect(filepath, false);
    }
    
    public boolean isMusicOn() {
        return settings.isMusicOn();
    }
    public boolean isSoundEffectsOn() {
        return settings.isSoundEffectsOn();
    }
    
    public void setMusicOn(boolean musicOn) {
        settings.setMusicOn(musicOn);
        
        if(musicOn) // if music should now play
        {
            if(musicFilepath != null) // if there is background music to play
            {
                playMusic(musicFilepath); 
            }
        }
        else
        {
            if(musicThread != null)
            {
                musicThread.stopPlaying();
            }
        }
    }
    public void setSoundEffectsOn(boolean soundEffectsOn) {
        settings.setSoundEffectsOn(soundEffectsOn);
         
        if(!soundEffectsOn) // if soundeffects should be off
        {
            while (sfxThreadList.size() > 0) // stop the soundeffects
            {
                sfxThreadList.get(0).stopPlaying();
                sfxThreadList.remove(0);
            }
        }
    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        if (evt.getSource().isLooping()) // if the player should loop
        {
            try {
                evt.getSource().replay(); // loop the player
            } catch (JavaLayerException ex) {
                ex.printStackTrace();
            }
        }
        else // otherwise (if the player should not loop)
        {
            // remove the player from the list of currently playing players
            Collection<PlayerThread> threadList = (Collection<PlayerThread>) sfxThreadList.clone();
            for (PlayerThread thread : threadList)
                if(thread.getPlayer() == evt.getSource())
                    sfxThreadList.remove(thread);
        }
    }
}
