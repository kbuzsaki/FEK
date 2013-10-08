/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game;

import Game.Logging.GameLogger;
import Game.Settings.GameSettings;
import Game.Settings.OptionWindow;
import Game.Sound.SoundManager;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game implements KeyListener{
    // Logging stuff
    private static GameLogger gameLogger = GameLogger.getDatedLoggerStream();
    private static boolean isDebug = false; // enables/disables logDebug()
    private static boolean isAudio = false; // enables/disables logSound()
    private static boolean isInfo = true; // enable/disables logInfo()
    private static boolean isSystem = true; // enable/disables logSystem()
    
    private JFrame gameWindow;
    private Level level;
    private GameSettings settings;
    private SoundManager soundManager;
    private OptionWindow optionWindow;
    
    private long lastTime = 0; // last time that a command was taken
    private static final long timeLim = 70; // minimum delay between commands
    
    private static final int windowWidthOffsetNotResizeable = 6;
    private static final int windowHeightOffsetNotResizeable = 28;
    private static final int windowWidthOffsetResizeable = 16;
    private static final int windowHeightOffsetResizeable = 38;
    private static int windowWidthOffset = windowWidthOffsetResizeable; // extra width from window frames
    private static int windowHeightOffset = windowHeightOffsetResizeable; // extra height from window frames
    
    private static Random rng = new Random();
    private static String settingsFilepath = "resources/settings";
    
    public Game() {
        loadSettings();
        soundManager = new SoundManager(settings);
        settings.setGame(this, soundManager);
        
        level = new Level(this, soundManager, "0");
        
        gameWindow = new JFrame("FEP Test");
            gameWindow.setMinimumSize(new Dimension(
                    level.getMinimumWidth() + windowWidthOffset,
                    level.getMinimumHeight() + windowHeightOffset));
            centerWindow();
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setFocusable(true);
            gameWindow.addKeyListener(this);
            gameWindow.addComponentListener(new ComponentListener() {

                @Override
                public void componentResized(ComponentEvent e) {
//                    System.out.println("Resized to: Width: " + gameWindow.getWidth() + " Height: " + gameWindow.getHeight());
                    onResize();
                }
                @Override
                public void componentMoved(ComponentEvent e) {}
                @Override
                public void componentShown(ComponentEvent e) {}
                @Override
                public void componentHidden(ComponentEvent e) {}
            });
            gameWindow.add(level.getLevelScreen());
        
        optionWindow = new OptionWindow(gameWindow, this, settings, soundManager);
    }
    public void startGame() {
        ThreadAnimation animThread = new ThreadAnimation(level);
        animThread.start();
        
        setZoom(settings.getZoom());
        snapToZoom();
        
        gameWindow.setVisible(true);
    }
    
    private void handleCommand(final Command command) {
        if (command != null)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    level.keyHandle(command);
                }
            }).start();
        }
    }
    
    @Override
    public void keyTyped (KeyEvent event) {}
    @Override
    public void keyPressed (KeyEvent event){
        // sets a minimum time between key calls
        if (System.currentTimeMillis() - lastTime > timeLim)
        {
        handleCommand(settings.getKeyMapper().getCommand(event));
        lastTime = System.currentTimeMillis();
        }
    }
    @Override
    public void keyReleased (KeyEvent event){}
    
    public GameSettings getSettings() {
        return settings;
    }
    private void loadSettings() {
        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(settingsFilepath))) {
            settings = (GameSettings) inStream.readObject();
            Game.logSystem("Successfully loaded settings: " + settingsFilepath);
        } catch( IOException | ClassNotFoundException ex) {
            Game.logSystem("Failed to load settings: " + settingsFilepath);
            settings = new GameSettings();
            saveSettings(); // save the settings so that they can be loaded later
        }
    }
    public void saveSettings() {
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(settingsFilepath))) {
            outStream.writeObject(settings);
            Game.logSystem("Successfully saved settings: " + settingsFilepath);
        } catch (IOException ex) {
            Game.logSystem("Failed to save settings: " + settingsFilepath);
            ex.printStackTrace();
        } 
    }
    
    public final void setZoom(int zoom) {
        level.setZoom(zoom);
        
//        int windowWidth = level.getLevelScreen().getWidth() + windowWidthOffset;
//        int windowHeight = level.getLevelScreen().getHeight() + windowHeightOffset;
//        
//        gameWindow.setSize(windowWidth, windowHeight);
    }
    public final void snapToZoom() {
        level.snapToZoom();
        centeredResize(level.getLevelScreen().getWidth() + windowWidthOffset,
                level.getLevelScreen().getHeight() + windowHeightOffset);
    }
    private void centeredResize(int width, int height) {
        int centerX = gameWindow.getX() + gameWindow.getWidth()/2;
        int centerY = gameWindow.getY() + gameWindow.getHeight()/2;
        
        gameWindow.setLocation(centerX - width/2, centerY - height/2);
        gameWindow.setSize(width, height);
    }
    private void centerWindow() {
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        int positionX = (screenSize.width - gameWindow.getWidth())/2;
        int positionY = (screenSize.height - gameWindow.getHeight())/2;
        gameWindow.setLocation(positionX, positionY);
    }
    private void onResize() {
        int levelScreenWidth = gameWindow.getWidth() - windowWidthOffset;
        int levelScreenHeight = gameWindow.getHeight() - windowHeightOffset;
        
//        if(levelScreenWidth < level.getMinimumWidth())
//        {
//            levelScreenWidth = level.getMinimumWidth();
//            gameWindow.setSize(levelScreenWidth + windowWidthOffset, gameWindow.getHeight());
//        }
//        
//        if(levelScreenHeight < level.getMinimumHeight())
//        {
//            levelScreenHeight = level.getMinimumHeight();
//            gameWindow.setSize(gameWindow.getWidth(), levelScreenHeight + windowHeightOffset);
//        }
        
        level.setSize(levelScreenWidth, levelScreenHeight);
    }
    
    public void openOptions() {
        Game.logInfo("Opening options menu");
        optionWindow.setVisible(true);
    }
    public void closeGame() {
        WindowEvent wev = new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
    
    /**
     * Standard FE RNG.
     * The number is generated by taking the mean average of two random numbers
     * generated from 0 to 100.
     * @return integer ranging from 0 (inclusive) to 100 (exclusive)
     */
    public static int getRandNum() {
        // rng in FE returns the average of two randomly generated numbers from 0 to 100
        return (rng.nextInt(100) + rng.nextInt(100)) / 2;
    }
    
    private static void log(String line, boolean shouldLogToConsole) {
        gameLogger.println(line, shouldLogToConsole);
    }
    public static void logDebug(String line) {
        log("Debug: " + line, isDebug);
    }
    public static void logAudio(String line) {
        log("Sound: " + line, isAudio);
    }
    public static void logInfo(String line) {
        log("Info: " + line, isInfo);
    }
    public static void logSystem(String line) {
        log("SysInf: " + line, isSystem); 
    }
    public static void logError(String line) {
        log("Error: " + line, true); // always log errors
    }
    
}
