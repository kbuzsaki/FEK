/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import static Game.Settings.GameSettings.*;
import Game.Sound.DefaultTrack;
import Game.Sound.SoundManager;
import Game.Sound.Track;
import Sprites.ColumnLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SoundManagerPanel extends JPanel implements Updateable {
    private static final Track[] songNames = DefaultTrack.values();
    private static final int VOLUME_DISPLAY_WIDTH = 30;
    
    private GameSettings settings;
    private SoundManager soundManager;
    
    private JPanel musicPanel;
    private JLabel musicLabel;
    private JSlider musicVolumeSlider;
    private JTextField musicVolumeDisplay;
    
    private JPanel sfxPanel;
    private JLabel sfxLabel;
    private JSlider sfxVolumeSlider;
    private JTextField sfxVolumeDisplay;
    
    private JPanel musicMutePanel;
    private JLabel musicMuteLabel;
    private JCheckBox musicMuteCheckBox;
    
    private JPanel sfxMutePanel;
    private JLabel sfxMuteLabel;
    private JCheckBox sfxMuteCheckBox;
    
    private JPanel musicSelectionPanel;
    private JLabel musicSelectionLabel;
    private JComboBox musicSelectionComboBox;
    
    public SoundManagerPanel (GameSettings settings, SoundManager soundManager) {
        this.settings = settings;
        this.soundManager = soundManager;
        
        
        musicPanel = new JPanel();
        musicLabel = new JLabel("Music:");
        musicVolumeSlider = new JSlider(MIN_VOLUME, MAX_VOLUME, settings.getMusicVolume());
        musicVolumeSlider.setEnabled(settings.isMusicOn());
        musicVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                SoundManagerPanel.this.settings.setMusicVolume(musicVolumeSlider.getValue());
                musicVolumeDisplay.setText(Integer.toString(musicVolumeSlider.getValue()));
            }
        });
        musicVolumeDisplay = new JTextField(Integer.toString(settings.getMusicVolume()));
        musicVolumeDisplay.setEnabled(false);
        musicVolumeDisplay.setPreferredSize(new Dimension(VOLUME_DISPLAY_WIDTH, 
                musicVolumeDisplay.getPreferredSize().height));
        musicPanel.add(musicLabel);
        musicPanel.add(musicVolumeSlider);
        musicPanel.add(musicVolumeDisplay);
        
        sfxPanel = new JPanel();
        sfxLabel = new JLabel("SFX:");
        sfxVolumeSlider = new JSlider(MIN_VOLUME, MAX_VOLUME, settings.getSoundEffectsVolume());
        sfxVolumeSlider.setEnabled(settings.isSoundEffectsOn());
        sfxVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                SoundManagerPanel.this.settings.setSoundEffectsVolume(sfxVolumeSlider.getValue());
                sfxVolumeDisplay.setText(Integer.toString(sfxVolumeSlider.getValue()));
                if(!sfxVolumeSlider.getValueIsAdjusting())
                {
                    playTestBeep();
                }
            }
        });
        sfxVolumeDisplay = new JTextField(Integer.toString(settings.getSoundEffectsVolume()));
        sfxVolumeDisplay.setEnabled(false);
        sfxVolumeDisplay.setPreferredSize(new Dimension(VOLUME_DISPLAY_WIDTH, 
                sfxVolumeDisplay.getPreferredSize().height));
        sfxPanel.add(sfxLabel);
        sfxPanel.add(sfxVolumeSlider);
        sfxPanel.add(sfxVolumeDisplay);
        
        musicMutePanel = new JPanel();
        musicMuteLabel = new JLabel("Mute Music:");
        musicMuteCheckBox = new JCheckBox(null, null, !settings.isMusicOn());
        musicMuteCheckBox.addActionListener(new ActionListener() {
            @Override
                    public void actionPerformed(ActionEvent event) {
                        SoundManagerPanel.this.settings.setMusicOn(!musicMuteCheckBox.isSelected());
                        musicVolumeSlider.setEnabled(!musicMuteCheckBox.isSelected());
                    }
                });
        musicMutePanel.add(musicMuteLabel);
        musicMutePanel.add(musicMuteCheckBox);
        
        sfxMutePanel = new JPanel();
        sfxMuteLabel = new JLabel("Mute SFX:");
        sfxMuteCheckBox = new JCheckBox(null, null, !settings.isSoundEffectsOn());
        sfxMuteCheckBox.addActionListener(new ActionListener() {
            @Override
                    public void actionPerformed(ActionEvent event) {
                        SoundManagerPanel.this.settings.setSoundEffectsOn(!sfxMuteCheckBox.isSelected());
                        sfxVolumeSlider.setEnabled(!sfxMuteCheckBox.isSelected());
                    }
                });
        sfxMutePanel.add(sfxMuteLabel);
        sfxMutePanel.add(sfxMuteCheckBox);
        
        musicSelectionPanel = new JPanel();
        musicSelectionLabel = new JLabel("Background Track:");
        musicSelectionComboBox = new JComboBox(songNames);
//        musicSelectionComboBox.setSelectedIndex(2); // special case
        musicSelectionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setBackgroundMusic((Track) musicSelectionComboBox.getSelectedItem());
            }
        });
        musicSelectionPanel.add(musicSelectionLabel);
        musicSelectionPanel.add(musicSelectionComboBox);
        
        setLayout(new ColumnLayout(ColumnLayout.LEFT));
        add(musicPanel);
        add(sfxPanel);
        add(musicMutePanel);
        add(sfxMutePanel);
        add(musicSelectionPanel);
        
        // hackiness to allign the volume sliders
        sfxLabel.setPreferredSize(musicLabel.getPreferredSize());
        // alligns the mute check boxes, if desired
        sfxMuteLabel.setPreferredSize(musicMuteLabel.getPreferredSize());
    }
    
    private void setBackgroundMusic(Track newTrack) {
        if(!soundManager.getBackgroudMusic().equals(newTrack))
            soundManager.setMusic(newTrack);
    }
    private void playTestBeep() {
        soundManager.playSoundEffect(SoundManager.confirm);
    }
    
    @Override
    public void update() {
        if(musicMuteCheckBox.isSelected() != !settings.isMusicOn())
            musicMuteCheckBox.setSelected(!settings.isMusicOn());
        if(musicVolumeSlider.getValue() != settings.getMusicVolume())
            musicVolumeSlider.setValue(settings.getMusicVolume());
        if(sfxMuteCheckBox.isSelected() != !settings.isSoundEffectsOn())
            sfxMuteCheckBox.setSelected(!settings.isSoundEffectsOn());
        if(sfxVolumeSlider.getValue() != settings.getSoundEffectsVolume())
            sfxVolumeSlider.setValue(settings.getSoundEffectsVolume());
        
        int index = Arrays.asList(songNames).indexOf(soundManager.getBackgroudMusic());
        if(index != -1)
        {
            musicSelectionComboBox.setSelectedIndex(index);
        }
            
    }
}
