/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Sprites;

import Game.Game;
import java.util.ArrayList;
import java.util.Collection;

public class CompletionNotifier implements CompletionListener {
    private CompletionListener listener;
    
    private ArrayList notifiers;
    private ArrayList<Boolean> notifiersListened;
    
    private boolean shouldListen;
    private boolean hasNotified = false;
    
    public CompletionNotifier(Collection notifiers, boolean shouldListen, CompletionListener listener) {
        this.notifiers = new ArrayList(notifiers);
        
        notifiersListened = new ArrayList(notifiers.size());
        for(int i = 0; i < notifiersListened.size(); i++) // set all to false
            notifiersListened.set(i, false);
        
        this.shouldListen = shouldListen;
        this.listener = listener;
    }
    public CompletionNotifier(Collection notifiers, boolean shouldListen) {
        this(notifiers, shouldListen, null);
    }
    public CompletionNotifier(CompletionListener listener) {
        this(new ArrayList(), false, listener);
    }
    public CompletionNotifier() {
        this(new ArrayList(), false, null);
    }

    public void setListener(CompletionListener listener) {
        this.listener = listener;
    }
    
    public void addNotifier(Object notifier) {
        if(!shouldListen) // locked down
        {
            notifiers.add(notifier);
            notifiersListened.add(false);
            Game.logDebug("Notifier Added (" + notifiers.size() + "/" 
                    + notifiersListened.size() + ") Notifier: " + notifier.toString());
        }
    }
    public void addNotifiers(Collection notifiers) {
        for(Object notifier : notifiers)
            addNotifier(notifier);
    }
    
    public void beginListening() {
        shouldListen = true;
    }

    @Override
    public void handleCompletion(CompletionEvent event) {
        Game.logDebug("CompletionNotifier notified by: " + event.toString());
        if(shouldListen)
        {
            if (notifiers.contains(event.getSource())) {
                Game.logDebug("CompletionNotifier contained: " + event.getSource().toString());
                notifiersListened.set(
                        notifiers.indexOf(event.getSource()),
                        true);

                if (shouldNotify()) {
                    notifyListener();
                }
            }
            else
            {
                Game.logDebug("CompletionNotifier did not contain: " + event.getSource().toString());
            }
        }
        else // ERROR: has not begun listening yet, not fully initialized
        {
            Game.logError("CompletionNotifier false start");
        }
    }
    
    private void notifyListener() {
        hasNotified = true;
        listener.handleCompletion(new CompletionEvent(this));
    }
    private boolean shouldNotify() {
        
        if(listener == null)
        {
            Game.logError("CompletionNotifier not fully initialized.");
            return false;
        }
        
        if(hasNotified)
        {
            Game.logError("CompletionNotifier attempting to notify more than once.");
            return false;
        }
        
        boolean shouldNotify = true;
        for(boolean listened : notifiersListened)
        {
            if(!listened)
            {
                shouldNotify = false;
                break;
            }
        }
        
        Game.logDebug("Should notify: " + shouldNotify);
        
        return shouldNotify;
    }
}
