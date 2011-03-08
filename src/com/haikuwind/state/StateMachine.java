package com.haikuwind.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.Log;

public class StateMachine {
    private final static String TAG = StateMachine.class.getSimpleName();
    
    private State currentState = State.APP_STOPPED;
    private List<StateListener> listeners = new ArrayList<StateListener>();

    private final Map<State, Map<Event, State>> states = new HashMap<State, Map<Event, State>>();

    public StateMachine() {

       for (State s : State.values()) {
            addTransition(s, Event.APP_STOP, State.APP_STOPPED);
        }

        addTransition(State.APP_STOPPED, Event.APP_START, State.REGISTER);
        addTransition(State.REGISTER, Event.REGISTERED, State.INIT_LAYOUT);
        addTransition(State.INIT_LAYOUT, Event.LAYOUT_READY, State.STARTED);
    }

    private void addTransition(State from, Event event, State to) {
        if (!states.containsKey(from)) {
            states.put(from, new HashMap<Event, State>());
        }

        states.get(from).put(event, to);
    }

    public State getCurrentState() {
            return currentState;
    }

    public void addStateListener(StateListener l) {
        listeners.add(l);
    }

    public void removeStateListener(StateListener l) {
        listeners.remove(l);
    }
    
    public void processEvent(Event event) {
            Log.d(TAG, "currentState="+currentState+", received event: "+event);
            Map<Event, State> transition = states.get(currentState);
            if (transition == null || !transition.containsKey(event)) {
                throw new IllegalStateException(
                        "Unknown transition from state " + currentState
                                + " on event " + event);
            }

            currentState = transition.get(event);
            Log.d(TAG, "moved to="+currentState);

            if (State.APP_STOPPED == currentState) {
                // prevent memory leaks caused by stored Activity reference
                for (StateListener listener : listeners) {
                    if (listener instanceof Activity) {
                        throw new IllegalStateException(
                                "Some Activities are registered in StateMachine on stop");
                    }
                }
            }

            State state = currentState;
            for (StateListener listener : listeners) {
                Log.d(TAG, listener.getClass().getSimpleName()+".processState "+state);
                listener.processState(state);
            }

    }

}
