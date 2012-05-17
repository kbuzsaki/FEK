/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Game.Sound.SoundManager;

public class AnimationEffect extends Animation implements Tickable{
    private int maxFrames;
    private int frame = 0;
    
    private boolean shouldPlaySoundEffect;
    private SoundManager soundManager;
    private String soundEffectFilename;
    
    public AnimationEffect(String filepath, int numberFrames, int[][] sequence) {
        super(filepath, 1, numberFrames, sequence);
        this.maxFrames = numberFrames;
        shouldPlaySoundEffect = false;
    }
    public AnimationEffect(String filepath, int numberFrames, int[][] sequence, 
            String soundEffectFilename) {
        this(filepath, numberFrames, sequence);
        
        this.soundEffectFilename = soundEffectFilename;
        shouldPlaySoundEffect = true;
    }
    
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }
    
    public boolean tick() {
        if(shouldPlaySoundEffect)
        {
            playSoundEffect();
        }
        if(frame >= maxFrames)
        {
            return true;
        }
        else
        {
            frame++;
            setTick(frame);
            return false;
        }
    }
    
    private void playSoundEffect() {
        soundManager.playSoundEffect(soundEffectFilename);
        shouldPlaySoundEffect = false;
    }
}
