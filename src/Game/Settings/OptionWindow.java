/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Game;
import Game.Level;
import Game.Sound.SoundManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OptionWindow extends JDialog {
    private JTabbedPane tabbedPane;
    private GeneralOptionsPanel generalOptionsPanel;
    private KeyMapperPanel keyMapperPanel;
    private SoundManagerPanel soundManagerPanel;
    
    public OptionWindow(JFrame appWindow, final Game game, GameSettings settings, SoundManager soundManager) {
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
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ((Updateable)tabbedPane.getSelectedComponent()).update();
            }
        });
        
        generalOptionsPanel = new GeneralOptionsPanel(settings, game);
        tabbedPane.addTab("General", generalOptionsPanel);
        
        keyMapperPanel = new KeyMapperPanel(settings.getKeyMapper());
        tabbedPane.addTab("Key Binds", keyMapperPanel);
        
        soundManagerPanel = new SoundManagerPanel(settings, soundManager);
        tabbedPane.addTab("Sound", soundManagerPanel);

        add(tabbedPane);
    }
}
