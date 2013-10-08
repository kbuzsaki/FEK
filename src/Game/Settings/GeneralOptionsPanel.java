/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Game;
import Sprites.ColumnLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class GeneralOptionsPanel extends JPanel implements Updateable {
    private GameSettings settings;
    private Game game;
    
    private static final String[] zooms = {"1x", "2x", "3x"};
    private JPanel zoomPanel;
    private JLabel zoomLabel;
    private JComboBox zoomComboBox;
    
    private JPanel snapPanel;
    private JLabel snapLabel;
    private JButton snapButton;
    
    private JPanel resetPanel;
    private JLabel resetLabel;
    private JButton resetButton;

    public GeneralOptionsPanel(GameSettings settings, Game game) {
        this.settings = settings;
        this.game = game;
        setLayout(new ColumnLayout(ColumnLayout.LEFT));
        
        zoomLabel = new JLabel("Zoom: ");
        
        zoomComboBox = new JComboBox(zooms);
        zoomComboBox.setSelectedIndex(settings.getZoom() - 1);
        zoomComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                zoomComboBoxActionPerformed();
            }
        });
        
        zoomPanel = new JPanel();
        zoomPanel.add(zoomLabel);
        zoomPanel.add(zoomComboBox);
        
        snapLabel = new JLabel("Snap Window to Zoom: ");
        
        snapButton = new JButton("Snap!");
        snapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                snapButtonActionPerformed();
            }
        });
        
        snapPanel = new JPanel();
        snapPanel.add(snapLabel);
        snapPanel.add(snapButton);
        
        resetLabel = new JLabel("Reset Settings: ");
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                resetButtonActionPerformed();
            }
        });
        
        resetPanel = new JPanel();
        resetPanel.add(resetLabel);
        resetPanel.add(resetButton);
        
        add(zoomPanel);
        add(snapPanel);
        add(resetPanel);
    }
    
    private void zoomComboBoxActionPerformed() {
        int zoom = zoomComboBox.getSelectedIndex() + 1;
        
        Game.logInfo("Setting zoom to: " + zoomComboBox.getSelectedItem());
        settings.setZoom(zoom);
    }
    private void snapButtonActionPerformed() {
        Game.logInfo("Snapping to zoom.");
        game.snapToZoom();
    }
    private void resetButtonActionPerformed() {
        settings.resetDefault();
    }
    
    public void update() {
        zoomComboBox.setSelectedIndex(settings.getZoom() - 1);
        
    }
    
}
