/*
 * Copyright 2012 Kyle Buzsaki. All Rights Reserved.
 */
package Game.Event;

import Units.Faction;
import java.util.EventObject;

public class PhaseChangeEvent extends EventObject {
    
    private Faction endingFaction;
    private Faction startingFaction;
    
    public PhaseChangeEvent(Faction endingFaction, Faction startingFaction) {
        super(startingFaction);
        this.endingFaction = endingFaction;
        this.startingFaction = startingFaction;
    }
    
    public Faction getEndingFaction() {
        return endingFaction;
    }
    public Faction getStartingFaction() {
        return startingFaction;
    }
    
}
