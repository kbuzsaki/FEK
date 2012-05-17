/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Settings.GameSettings;
import Game.Settings.KeyMapper;
import Game.Settings.OptionWindow;
import Game.Sound.SoundManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Random;
import javax.swing.JFrame;
import org.apache.commons.io.IOUtils;

public class Game implements KeyListener{
    private JFrame gameWindow;
    private Level level;
    private GameSettings settings;
    private SoundManager soundManager;
    private KeyMapper keyMapper;
    private OptionWindow optionWindow;
    
    private long lastTime = 0; // last time that a command was taken
    private static long timeLim = 70; // minimum delay between commands
    
    private static int windowWidthOffset = 6; // extra width from window frames
    private static int windowHeightOffset = 28; // extra height from window frames
    
    private static Random rng = new Random();
    private static String settingsFilepath = "resources/settings";
    private static boolean isDebug = true; // enables/disables Game.log()
    
    public Game() {
        loadSettings();
        soundManager = new SoundManager(settings);
        keyMapper = settings.getKeyMapper();
        
        level = new Level(this, soundManager, "0");
            ThreadAnimation animThread = new ThreadAnimation(level);
            animThread.start();
            
        int windowWidth = level.getLevelScreen().getWidth() + windowWidthOffset;
        int windowHeight = level.getLevelScreen().getHeight() + windowHeightOffset;
        
        gameWindow = new JFrame("FEP Test");
            gameWindow.setBounds(200, 200, windowWidth, windowHeight);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setResizable(false);
            gameWindow.setFocusable(true);
            gameWindow.addKeyListener(this);
            gameWindow.add(level.getLevelScreen());
            gameWindow.setVisible(true);
        
        optionWindow = new OptionWindow(gameWindow, this, soundManager, keyMapper);
    }
    
    private void handleCommand(Command command) {
        if (command != null)
        {
        level.keyHandle(command);
        }
    }
    
    public void keyTyped (KeyEvent event) {}
    public void keyPressed (KeyEvent event){
        // sets a minimum time between key calls
        if (System.currentTimeMillis() - lastTime > timeLim)
        {
        handleCommand(keyMapper.getCommand(event));
        lastTime = System.currentTimeMillis();
        }
    }
    public void keyReleased (KeyEvent event){}
    
    public GameSettings getSettings() {
        return settings;
    }
    private void loadSettings() {
        ObjectInputStream inStream = null;
        try {
            inStream = new ObjectInputStream(new FileInputStream(settingsFilepath));
            Object objectIn = inStream.readObject();
            Game.log("Successfully loaded settings: " + settingsFilepath);
            settings = (GameSettings) objectIn;
        } catch( IOException | ClassNotFoundException ex) {
            Game.log("Failed to load settings: " + settingsFilepath);
            settings = new GameSettings();
            saveSettings(); // save the settings so that they can be loaded later
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }
    public void saveSettings() {
        ObjectOutputStream outStream = null;
        try {
            outStream = new ObjectOutputStream(new FileOutputStream(settingsFilepath));
            outStream.writeObject(settings);
            Game.log("Successfully saved settings: " + settingsFilepath);
        } catch (IOException ex) {
            Game.log("Failed to save settings: " + settingsFilepath);
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outStream);
        }
    }
    
    public void openOptions() {
        Game.log("Opening options menu");
        optionWindow.setVisible(true);
    }
    public void closeGame() {
        WindowEvent wev = new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
    
    /**
     * Standard RNG.
     * @return integer ranging from 0 (inclusive) to 100 (exclusive)
     */
    public static int getRandNum() {
        return rng.nextInt(100);
    }
    public static void log(String line) {
        if(isDebug) 
        {
            System.out.println(line);
        }
    }
    
}
