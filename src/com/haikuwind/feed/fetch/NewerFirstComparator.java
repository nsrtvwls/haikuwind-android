package com.haikuwind.feed.fetch;

import java.util.Comparator;

import com.haikuwind.feed.Haiku;

public class NewerFirstComparator implements Comparator<Haiku> {

    @Override
    public int compare(Haiku one, Haiku another) {
        //newer is more lightweight
        return -one.getTime().compareTo(another.getTime());
    }

}
