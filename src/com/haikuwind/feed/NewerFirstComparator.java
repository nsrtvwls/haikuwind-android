package com.haikuwind.feed;

import java.util.Comparator;

public class NewerFirstComparator implements Comparator<Haiku> {

    @Override
    public int compare(Haiku one, Haiku another) {
        //newer is more lightweight
        return -one.getTime().compareTo(another.getTime());
    }

}
