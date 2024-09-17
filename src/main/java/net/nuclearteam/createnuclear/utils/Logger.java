package net.nuclearteam.createnuclear.utils;

import net.nuclearteam.createnuclear.CreateNuclear;

public class Logger {
    public static void SendToLog(String message) {
        CreateNuclear.LOGGER.info(message);
    }
}
