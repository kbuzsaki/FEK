/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Logging;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ErrorRedirector extends PrintStream {
    private GameLogger gameLogger;
    private PrintStream consoleErr;
    
    public ErrorRedirector(GameLogger gameLogger) {
        super(gameLogger);
        this.gameLogger = gameLogger;
        consoleErr = new PrintStream(new FileOutputStream(FileDescriptor.err));
        System.setErr(this);
    }
    
    @Override
    public void print(String s) {
        gameLogger.print(s, false);
        consoleErr.println(s); // this has to be called, not sure why
    }
    @Override
    public void println(String s) {
        gameLogger.println(s, false);
        consoleErr.println(s);
    }

}
