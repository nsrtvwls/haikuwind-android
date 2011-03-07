package com.haikuwind.state;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//TODO don't need to be static. It is used only for HaikuWind class
public class StateMachine {
    private static State currentState = State.APP_STOPPED;
    private static List<WeakReference<StateListener>> listeners = new ArrayList<WeakReference<StateListener>>();
    
    private static Map<State, Map<Event, State>> states = new HashMap<State, Map<Event,State>>();
    
    static {
        for(State s: State.values()) {
            addTransition(s, Event.APP_STOP, State.APP_STOPPED);
        }
        
        addTransition(State.APP_STOPPED, Event.APP_START, State.APP_STARTED);
        addTransition(State.APP_STARTED, Event.STATE_MACHINE_READY, State.REGISTER);
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
        
        if(State.APP_STARTED==currentState) {
            if(listeners.size()!=0) {
                throw new IllegalStateException("Unremoved state listeners");
            }
            processEvent(Event.STATE_MACHINE_READY);
        } else if (State.APP_STOPPED==currentState) {
            listeners.clear();
        }

        for (Iterator<WeakReference<StateListener>> listenerRef = listeners.iterator();
                listenerRef.hasNext();) {
            StateListener listener = listenerRef.next().get();
            if(listener != null) {
                listener.processState(currentState);
            }
        }
    }

}
