package com.haikuwind.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.haikuwind.feed.Haiku;
import com.haikuwind.state.State;
import com.haikuwind.state.StateListener;
import com.haikuwind.state.StateMachine;

public class UpdateNotifier implements StateListener {
    
    static {
        // to make sure all activities are unregistered on application stop
        StateMachine.addStateListener(new UpdateNotifier());
    }
    
    private static Map<Update, List<UpdateListener>> listeners;
    
    public static void addUpdateListener(UpdateListener listener, Update... updates) {
        for(Update update: updates) {
            if(listeners.get(update)==null) {
                listeners.put(update, new ArrayList<UpdateListener>());
            }
            
            listeners.get(update).add(listener);
        }
    }
    
    public static void removeUpdateListener(UpdateListener listener) {
        for(List<UpdateListener> list: listeners.values()) {
            list.remove(listener);
        }
    }
    
    public static void fireUpdate(Update update, Haiku haiku) {
        List<UpdateListener> toUpdate = listeners.get(update);
        if(toUpdate==null) {
            return;
        }
        
        for(UpdateListener listener: toUpdate) {
            listener.processUpdate(update, haiku);
        }
    }

    private UpdateNotifier() {
        
    }
    
    @Override
    public void processState(State state) {
        if(State.APP_STOPPED!=state) {
            return;
        }
        
        for(List<UpdateListener> list: listeners.values()) {
            if(!list.isEmpty()) {
                throw new IllegalStateException("Found unremoved UpdateListener on app stop");
            }
        }
    }

}
