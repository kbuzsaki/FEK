/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 * A lightweight Thread for playing a music file. 
 */
package Game.Sound;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;

public class PlayerThread extends Thread {

    private AdvancedPlayer player;
    PlaybackListener listener;

    public PlayerThread(String filename, PlaybackListener listener, boolean shouldLoop) {
        try {
            player = new AdvancedPlayer(new File(filename));
            player.setPlayBackListener(listener);
            player.setIsLooping(shouldLoop);
        } catch (JavaLayerException ex) {
            System.out.println("Failed to open player: " + filename);
        }
        setName("PlayerThread " + filename);
    }
    
    public AdvancedPlayer getPlayer() {
        return player;
    }
    
    public void run() {
        play();
    }

    public void play() {
        try {
            player.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(PlayerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stopPlaying() {
        player.mute();
        player.setIsLooping(false);
    }
}
