package com.haikuwind.notification;

import com.haikuwind.feed.Haiku;

public interface UpdateListener {
    void processUpdate(UpdateType update, Haiku haiku);
}
