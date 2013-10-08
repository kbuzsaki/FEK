/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Settings;

import Game.Command;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EnumMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class KeyMapperPanel extends JPanel implements KeyListener, Updateable {
    private static final int labelX = 10;
    private static final int panelX = 70;
    private static final int labelY = 42;
    
    private KeyMapper keyMapper;
    
    private JPanel bindingPanel;
    private EnumMap<Command, JPanel> commandPanels;
    private EnumMap<Command, JLabel> commandLabels;
    private EnumMap<Command, JTextField> commandKeyLists;
    private EnumMap<Command, JButton> addButtons;
    private EnumMap<Command, JButton> clearButtons;
    
    private JButton resetButton;
    
    private Command markedCommand; // the command marked to recieve a new key bind
    
    public KeyMapperPanel(KeyMapper keyMapper) {
        setLayout(null);
        setOpaque(true);
        setSize(440,365);
        setPreferredSize(getSize());
        setBorder(new LineBorder(Color.GREEN));
        addKeyListener(this);
        
        this.keyMapper = keyMapper;
        
        bindingPanel = new JPanel();
        bindingPanel.setLayout(null);
        bindingPanel.setBounds(0, 20, 440, 300);
        bindingPanel.setOpaque(false);
        add(bindingPanel);
        
        resetButton = new JButton("Reset Defaults");
        resetButton.setBounds(300, 320, 120, 30);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                resetButtonActionPerformed();
            }
        });
        add(resetButton);
        
        commandPanels = new EnumMap(Command.class);
        commandLabels = new EnumMap(Command.class);
        commandKeyLists = new EnumMap(Command.class);
        addButtons = new EnumMap(Command.class);
        clearButtons = new EnumMap(Command.class);
        
        for (final Command command : Command.values())
        {
            commandLabels.put(command, new JLabel(command.name + ":"));
            commandLabels.get(command).setLocation(labelX,labelY*command.ordinal() + 5);
            commandLabels.get(command).setSize(60,20);
            bindingPanel.add(commandLabels.get(command));
            
            commandPanels.put(command, new JPanel());
            commandPanels.get(command).setLocation(panelX, labelY*command.ordinal());
            commandPanels.get(command).setSize(360, 40);
            commandPanels.get(command).setBorder(new LineBorder(Color.BLUE));
            
            commandKeyLists.put(command, new JTextField());
            commandKeyLists.get(command).setSize(200, 25);
            commandKeyLists.get(command).setPreferredSize(commandKeyLists.get(command).getSize());
            commandKeyLists.get(command).setEditable(false);
            
            addButtons.put(command, new JButton("add key"));
            addButtons.get(command).addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent event) {
                        addButtonActionPerformed(command);
                    }
                }
            );
            addButtons.get(command).addKeyListener(this); // keyListener to get keyEvents for keyBinding 
            
            clearButtons.put(command, new JButton("clear"));
            clearButtons.get(command).addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent event) {
                        clearCommand(command);
                    }
                }
            );
            
            commandPanels.get(command).add(commandKeyLists.get(command));
            commandPanels.get(command).add(addButtons.get(command));
            commandPanels.get(command).add(clearButtons.get(command));
            
            bindingPanel.add(commandPanels.get(command));
        }
        
        updateKeyLists();
        
    }
    
    private void resetButtonActionPerformed() {
        keyMapper.resetDefaultBindings();
        updateKeyLists();
    }
    private void addButtonActionPerformed(Command command) {
        if(isMarkedForBind(command))
        {
            unmarkForBind(command);
        }
        else
        {
            markForBind(command);
        }
    }
    private void markForBind(Command command) {
        markedCommand = command;
        commandKeyLists.get(command).setBackground(Color.GREEN);
    }
    private void unmarkForBind(Command command) {
        commandKeyLists.get(command).setBackground(null);
        markedCommand = null;
    }
    private boolean isMarkedForBind(Command command) {
        if(markedCommand == command)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void clearCommand(Command command) { 
        keyMapper.clearCommand(command);
        updateKeyLists(command);
    }
    
    private void updateKeyLists() {
        for (Command command : Command.values()) 
            updateKeyLists(command);
    }
    private void updateKeyLists(Command command) {
        commandKeyLists.get(command).setText(formatKeyList(keyMapper.getKeys(command)));
        commandKeyLists.get(command).setBackground(null);
    }
    
    public void keyTyped(KeyEvent event) {
        
    }
    public void keyPressed(KeyEvent event) {
        if(markedCommand != null) // if there is a marked command
        {
            keyMapper.bindKey(markedCommand, event.getKeyCode());
            updateKeyLists(markedCommand);
            markedCommand = null;
        }
    }
    public void keyReleased(KeyEvent event) {
        
    }
    
    public void update() {
        updateKeyLists();
    }
    
    private static String formatKeyList(ArrayList<Integer> keyList) {
        String keyListText = "";
        
        for (int i = 0; i < keyList.size(); i++)
        {
            keyListText += KeyEvent.getKeyText(keyList.get(i)) + ", ";
        }
        
        return keyListText;
    }
}
