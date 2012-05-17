/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Game;
import Game.Sound.SoundManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class OptionWindow extends JDialog {
    JTabbedPane tabbedPane;
    KeyMapperPanel keyMapperPanel;
    SoundManagerPanel soundManagerPanel;
    
    public OptionWindow(JFrame appWindow, final Game game, SoundManager soundManager, KeyMapper keyMapper) {
        super(appWindow, true);
        setLocation(appWindow.getX() + 30, appWindow.getY() + 25);
        setSize(450,420);
        setResizable(false);
        // set the window to save settings when it's closed
        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }
            public void windowClosing(WindowEvent e) {
                game.saveSettings();
            }
            public void windowClosed(WindowEvent e) {
            }
            public void windowIconified(WindowEvent e) {
            }
            public void windowDeiconified(WindowEvent e) {
            }
            public void windowActivated(WindowEvent e) {
            }
            public void windowDeactivated(WindowEvent e) {
            }
            
        });
        
        tabbedPane = new JTabbedPane();
        
        keyMapperPanel = new KeyMapperPanel(keyMapper);
        tabbedPane.addTab("Key Map", keyMapperPanel);
        
        soundManagerPanel = new SoundManagerPanel(soundManager);
        tabbedPane.addTab("Sound", soundManagerPanel);

        add(tabbedPane);
    }
}
