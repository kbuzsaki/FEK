/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Sound;

import static Game.Sound.SoundManager.MUSIC_DIRECTORY;
import static Game.Sound.SoundManager.OGG;

public enum DefaultTrack implements Track {
    WIND_ACROSS_THE_PLAINS(6, "Wind Across the Plains", MUSIC_DIRECTORY + "006" + OGG),
    PRECIOUS_THINGS(7, "Precious Things", MUSIC_DIRECTORY + "007" + OGG),
    COMPANIONS(8, "Companions", MUSIC_DIRECTORY + "008" + OGG);
    
    private int trackNumber;
    private String trackName;
    private String filename;
    
    DefaultTrack(int trackNumber, String trackName, String filename) {
        this.trackNumber = trackNumber;
        this.trackName = trackName;
        this.filename = filename;
    }
    
    public int getTrackNumber() {
        return trackNumber;
    }
    public String getTrackName() {
        return trackName;
    }
    public String getFilename() {
        return filename;
    }
    
    @Override
    public String toString() {
        return Integer.toString(getTrackNumber()) + " - " + getTrackName();
    }
}
