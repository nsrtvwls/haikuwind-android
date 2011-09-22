package com.haikuwind.notification;

import java.util.HashMap;
import java.util.Map;

public enum UpdateAction {
    
    NEW_HAIKU("NEW_HAIKU"),
    ADD_FAVORITE("ADD_FAVORITE"),
    VOTE("VOTE"),
    REFRESH("REFRESH");
    
    private String name;
    
    private UpdateAction(String name) {
        this.name = String.format("%s.%s", getClass().getCanonicalName(), name);
    }

    public String toString() {
        return name;
    }
    
    private static Map<String, UpdateAction> valueMap = new HashMap<String, UpdateAction>();
    
    static {
        for(UpdateAction action: values()) {
            valueMap.put(action.toString(), action);
        }
    }
    
    public static UpdateAction fromName(String name) {
        return valueMap.get(name);
    }
    
}
