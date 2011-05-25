package com.haikuwind.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaikuListData {
    private Date lastHaikuDate;
    private boolean dataDirty = false;
    
    private Map<String, Haiku> haikuMap =  new HashMap<String, Haiku>();
    private List<Haiku> haikuList = new ArrayList<Haiku>();
    
    private int updateCounter;
    
    synchronized public boolean isViewObsolete(Date lastViewDate) {
        if(lastHaikuDate==null) {
            return false;
        }
        
        return lastHaikuDate.after(lastViewDate);
    }
    
    synchronized public void updateHaikuList(List<Haiku> update, boolean erase) {
        if(erase) {
            haikuList.clear();
            haikuMap.clear();
        }
        haikuList.addAll(update);
        
        //newer first
        Collections.sort(haikuList, new NewerFirstComparator());

        for(Haiku h: update) {
            haikuMap.put(h.getId(), h);
        }
        
        if(haikuList.size()>0) {
            lastHaikuDate = haikuList.get(0).getTime();
        } else {
            lastHaikuDate = null;
        }
        
        updateCounter++;
     }

    synchronized public boolean isDataDirty() {
        return dataDirty;
    }

    synchronized public void setDataDirty(boolean dataDirty) {
        this.dataDirty = dataDirty;
    }

    synchronized public Map<String, Haiku> getHaikuMap() {
        return haikuMap;
    }

    synchronized public List<Haiku> getHaikuList() {
        return haikuList;
    }
    synchronized public Date getLastHaikuDate() {
        return lastHaikuDate;
    }
    synchronized public int getUpdateCounter() {
        return updateCounter;
    }

    synchronized public void resetList() {
        //do not clean haiku list to leave an ability to vote/favorite etc
        
        setDataDirty(true);
        //to get whole timeline
        lastHaikuDate = null;
        updateCounter = 0;
    }


}