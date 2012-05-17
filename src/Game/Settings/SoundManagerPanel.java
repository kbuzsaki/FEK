/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Sound.SoundManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SoundManagerPanel extends JPanel{
    private static final String[] songNames = {"006", "007", "008"};
    
    private SoundManager soundManager;
    
    private JPanel musicPanel;
    private JLabel musicLabel;
    private JCheckBox musicCheckBox;
    
    private JPanel sfxPanel;
    private JLabel sfxLabel;
    private JCheckBox sfxCheckBox;
    
    private JPanel musicSelectionPanel;
    private JLabel musicSelectionLabel;
    private JComboBox musicSelectionComboBox;
    
    public SoundManagerPanel (SoundManager soundManager) {
        this.soundManager = soundManager;
        
        musicPanel = new JPanel();
        musicLabel = new JLabel("Music:");
        
        musicCheckBox = new JCheckBox();
        musicCheckBox.setSelected(soundManager.isMusicOn());
        musicCheckBox.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent event) {
                        setIsMusicOn(musicCheckBox.isSelected());
                    }
                }
            );
        
        musicPanel.add(musicLabel);
        musicPanel.add(musicCheckBox);
        add(musicPanel);
        
        sfxPanel = new JPanel();
        sfxLabel = new JLabel("Sound Effects:");
        
        sfxCheckBox = new JCheckBox();
        sfxCheckBox.setSelected(soundManager.isSoundEffectsOn());
        sfxCheckBox.addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent event) {
                        setIsSoundEffectsOn(sfxCheckBox.isSelected());
                    }
                }
            );
        
        sfxPanel.add(sfxLabel);
        sfxPanel.add(sfxCheckBox);
        add(sfxPanel);
        
        musicSelectionPanel = new JPanel();
        musicSelectionLabel = new JLabel("Background Music:");
        
        musicSelectionComboBox = new JComboBox(songNames);
        musicSelectionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setBackgroundMusic((String) musicSelectionComboBox.getSelectedItem());
            }
        });
        
        musicSelectionPanel.add(musicSelectionLabel);
        musicSelectionPanel.add(musicSelectionComboBox);
        add(musicSelectionPanel);
    }
    
    private void setBackgroundMusic(String songName) {
        soundManager.playMusic(SoundManager.MUSIC_DIRECTORY + songName + SoundManager.MP3);
    }
    
    private void setIsMusicOn(boolean isMusicOn) {
        soundManager.setMusicOn(isMusicOn);
    }
    private void setIsSoundEffectsOn(boolean isSoundEffectsOn) {
        soundManager.setSoundEffectsOn(isSoundEffectsOn);
    }
}
