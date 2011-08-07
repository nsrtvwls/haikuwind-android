package com.haikuwind.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haikuwind.feed.Haiku;

public class UpdateNotifier {
    
    private static Map<UpdateType, List<UpdateListener>> listeners = new HashMap<UpdateType, List<UpdateListener>>();
    
    synchronized public static void addUpdateListener(UpdateListener listener, UpdateType... updates) {
        for(UpdateType update: updates) {
            if(listeners.get(update)==null) {
                listeners.put(update, new ArrayList<UpdateListener>());
            }
            
            listeners.get(update).add(listener);
        }
    }
    
    synchronized public static void removeUpdateListener(UpdateListener listener) {
        for(List<UpdateListener> list: listeners.values()) {
            list.remove(listener);
        }
    }
    
    synchronized public static void fireUpdate(UpdateType update, Haiku haiku) {
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
    
}
