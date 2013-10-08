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
    
    private CompletionListener listener;
    private int delay = 0;
    
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
    
    public void setCompletionListener(CompletionListener listener) {
        this.listener = listener;
    }
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
    public int getDelay() {
        return delay;
    }
    
    @Override
    public boolean tick() {
        if(shouldPlaySoundEffect)
        {
            playSoundEffect();
        }
        if(frame >= maxFrames)
        {
//            if(listener != null)
//            {
//                listener.handleCompletion(new CompletionEvent(this));
//            }
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
