/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Logging;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameLogger extends PrintStream {
    private static final String LOG_DIRECTORY = "logs/";
    private static final DateFormat dateFormat = new SimpleDateFormat("_MM_dd_kk-mm");
    private static final DateFormat dateFormatSecond = new SimpleDateFormat("_MM_dd_kk-mm-ss");
    private PrintStream consoleOut;
    private ErrorRedirector errorRedirector;
    
    public GameLogger(File file) throws FileNotFoundException {
        super(file);
        consoleOut = new PrintStream(new FileOutputStream(FileDescriptor.out));
        System.setOut(this);
        errorRedirector = new ErrorRedirector(this);
        System.out.println("SysInf: Log file successfully opened at: " + file.getPath());
    }
    public GameLogger(String filepath) throws FileNotFoundException {
        this(new File(filepath));
    }
    
    public static GameLogger getDatedLoggerStream() {
        File loggerDirectory = new File(LOG_DIRECTORY);
        if(!loggerDirectory.exists())
            if(loggerDirectory.mkdirs())
                System.out.println("SysInf: Creating logging directory.");
            else
                System.out.println("SysInf: Unable to create logging directory.");
        
        GameLogger gameLogger = null;
        
        try {
            gameLogger = new GameLogger(getDateFilepath());
        } catch (FileNotFoundException ex) {
            System.out.println("Logger failed to initialize.");
        }
        
        return gameLogger;
    }
    
    @Override
    public void print(String s) {
        print(s, true);
    }
    @Override
    public void println(String s) {
        println(s, true);
    }
    
    public void print(String s, boolean shouldPrintToConsole) {
        super.print(s);
        if(shouldPrintToConsole)
            consoleOut.print(s);
        
    }
    public void println(String s, boolean shouldPrintToConsole) {
            print(s, shouldPrintToConsole);
            println();
            if(shouldPrintToConsole)
                consoleOut.println();
    }

    private static String getDateFilepath() {
        GregorianCalendar calendar = new GregorianCalendar();
        
        String dateFilepath = LOG_DIRECTORY + "log" + dateFormat.format(calendar.getTime()) + ".txt";
        
        if((new File(dateFilepath).exists()))
        {
            System.out.println("SysInf: Log file \"" + dateFilepath + "\" already exists. Getting new filepath...");
            dateFilepath = LOG_DIRECTORY + "log" + dateFormatSecond.format(calendar.getTime()) + ".txt";
        }
        
        return dateFilepath;
    }
}
