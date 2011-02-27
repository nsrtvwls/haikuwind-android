package com.haikuwind.state;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO don't need to be static. It is used only for HaikuWind class
public class StateMachine {
    private static State currentState = State.REGISTER;
    private static List<WeakReference<StateListener>> listeners = new ArrayList<WeakReference<StateListener>>();
    
    private static Map<State, Map<Event, State>> states = new HashMap<State, Map<Event,State>>();
    
    static {
        for(State s: State.values()) {
            addTransition(s, Event.APP_LAUNCH, State.APP_LAUNCH);
        }
        
        addTransition(State.APP_LAUNCH, Event.STATE_MACHINE_READY, State.REGISTER);
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
        listeners.add(new WeakReference<StateListener>(l));
    }
    
    public static void processEvent(Event event) {
        Map<Event, State> transition = states.get(currentState);
        if(transition==null || !transition.containsKey(event)) {
            throw new IllegalStateException("Unknown transition from state "+currentState+" on event "+event);
        }

        currentState = transition.get(event);
        
        if(State.APP_LAUNCH==currentState) {
            listeners.clear();
            processEvent(Event.STATE_MACHINE_READY);
        }

        for(WeakReference<StateListener> l: listeners) {
            l.get().processState(currentState);
        }
    }

}
