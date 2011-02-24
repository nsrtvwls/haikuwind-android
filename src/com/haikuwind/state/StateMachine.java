package com.haikuwind.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateMachine {
    private static State currentState = State.REGISTER;
    private static List<StateListener> listeners = new ArrayList<StateListener>();
    
    private static Map<State, Map<Event, State>> states = new HashMap<State, Map<Event,State>>();
    
    static {
        for(State s: State.values()) {
            addTransition(s, Event.APP_LAUNCH, State.REGISTER);
        }
        addTransition(State.REGISTER, Event.REGISTERED, State.INIT_LAYOUT);
        addTransition(State.INIT_LAYOUT, Event.LAYOUT_READY, State.STARTED);
    }
    
    private static void addTransition(State from, Event event, State to) {
        if(!states.containsKey(from)) {
            states.put(from, new HashMap<Event, State>());
        }
        
        states.get(from).put(event, to);
    }
    
    public static State getCurrentState() {
        return currentState;
    }
    
    public static void addStateListener(StateListener l) {
        listeners.add(l);
    }
    
    public static void processEvent(Event event) {
        Map<Event, State> transition = states.get(currentState);
        if(transition==null || !transition.containsKey(event)) {
            throw new IllegalStateException("Unknown transition from state "+currentState+" on event "+event);
        }
        
        currentState = transition.get(event);
        
        for(StateListener l: listeners) {
            l.processState(currentState);
        }
    }

    public static void clearListeners() {
        listeners.clear();
    }
    
}
