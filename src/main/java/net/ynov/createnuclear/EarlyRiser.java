package net.ynov.createnuclear;

import com.chocohead.mm.api.ClassTinkerers;

public class EarlyRiser implements Runnable{
    @Override
    public void run() {
        ClassTinkerers.enumBuilder("net.ynov.createnuclear.TestEnum", long.class, int[].class).addEnum("TEST", 0L, new int[0]).build();
    }
}
