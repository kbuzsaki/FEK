/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Sound;

import Game.Settings.GameSettings;
import easyogg.ClipFinishedEvent;
import easyogg.ClipFinishedListener;
import easyogg.OggClip;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SoundManager implements ClipFinishedListener, Serializable {
    public static final String BASE_DIRECTORY = "resources/sound/";
    public static final String SFX_DIRECTORY = BASE_DIRECTORY + "soundEffects/";
    public static final String MUSIC_DIRECTORY = BASE_DIRECTORY + "music/";
    public static final String OGG = ".ogg";
    
    // Sound Effects
    public static final String cancel =     SFX_DIRECTORY + "cancel" + OGG;
    public static final String confirm =    SFX_DIRECTORY + "confirm" + OGG;
    public static final String cursorBlip = SFX_DIRECTORY + "cursorBlip" + OGG;
    public static final String forbidden =  SFX_DIRECTORY + "forbidden" + OGG;
    public static final String heal =       SFX_DIRECTORY + "heal" + OGG;
    public static final String hit =        SFX_DIRECTORY + "hit" + OGG;
    public static final String hitKill =    SFX_DIRECTORY + "hitKill" + OGG;
    public static final String hitCrit =    SFX_DIRECTORY + "hitCrit" + OGG;
    public static final String hitCritKill =SFX_DIRECTORY + "hitCritKill" + OGG;
    public static final String menuBlipLow =SFX_DIRECTORY + "menuBlipLow" + OGG;
    public static final String menuBlipHigh=SFX_DIRECTORY + "menuBlipHigh" + OGG;
    public static final String miss =       SFX_DIRECTORY + "miss" + OGG;
    public static final String moveFly =    SFX_DIRECTORY + "moveFly" + OGG;
    public static final String moveFoot =   SFX_DIRECTORY + "moveFoot" + OGG;
    public static final String select =     SFX_DIRECTORY + "select" + OGG;
    
    // Music
    public static final String Companions = MUSIC_DIRECTORY + "008" + OGG;
    
    private GameSettings settings;
    
    private OggClip backgroundClip;
    private Track musicTrack; // the previous background music played
    private ArrayList<OggClip> sfxClipList;
    
    public SoundManager(GameSettings settings) {
        this.settings = settings;
//        sfxThreadList = new ArrayList();
        sfxClipList = new ArrayList<>();
        
    }
    
    public void setMusic(Track track) {
        System.out.println("Setting music as: " + track);
        musicTrack = track;
        
        if(backgroundClip != null) 
        {
            backgroundClip.stop();
            backgroundClip = null;
        }
        
        try {
            backgroundClip = new OggClip(new File(track.getFilename()));
        } catch (IOException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        backgroundClip.setGain(convertToGain(settings.getMusicVolume()));
        if(settings.isMusicOn())
        {
            backgroundClip.loop();
        }
    }
    public Stoppable playSoundEffect(String filepath, boolean shouldLoop) {

        if(settings.isSoundEffectsOn()) // if sound effects should play
        {
            try {
                final OggClip sfxClip = new OggClip(new File(filepath));
                sfxClip.setGain(convertToGain(settings.getSoundEffectsVolume()));
                sfxClip.setClipFinishedListener(this);
                sfxClipList.add(sfxClip);
                if(shouldLoop)
                    sfxClip.loop();
                else
                    sfxClip.play();
                return new Stoppable() {
                    final OggClip clip = sfxClip;
                    public void stop() {
                        if(clip != null)
                        {
                            clip.stop();
                        }
                        else
                        {
                            System.err.println("Trying to stop clip twice");
                        }
                    }
                };
            } catch (IOException ex)
            {
                Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public Stoppable playSoundEffect(String filepath) {
        return playSoundEffect(filepath, false);
    }
    
    public Track getBackgroudMusic() {
        return musicTrack;
    }
    
    public void setMusicOn(boolean musicOn) {
        if(musicOn) // if music should now play
        {
            if(musicTrack != null) // if there is background music to play
            {
                setMusic(musicTrack); 
            }
        }
        else
        {
            if(backgroundClip != null)
            {
                backgroundClip.stop();
            }
        }
    }
    public void setMusicVolume(int musicVolume) {
        backgroundClip.setGain(convertToGain(settings.getMusicVolume()));
    }
    public void setSoundEffectsOn(boolean soundEffectsOn) {
        if(!soundEffectsOn) // if soundeffects should be off
        {
            for(OggClip sfxClip : sfxClipList)
            {
                sfxClip.stop();
            }
            sfxClipList.clear();
        }
    }
    public void setSoundEffectsVolume(int soundEffectsVolume) {
        for(OggClip sfxClip : sfxClipList)
        {
            sfxClip.setGain(convertToGain(settings.getSoundEffectsVolume()));
        }
    }
    
    @Override
    public void clipFinished(ClipFinishedEvent event) {
        if(sfxClipList.contains(event.getFinishedClip()))
        {
            sfxClipList.remove(event.getFinishedClip());
        }
    }
    
    private static float convertToGain(int volume) {
        return (float)(Math.log(volume)/Math.log(GameSettings.MAX_VOLUME));
    }
}
