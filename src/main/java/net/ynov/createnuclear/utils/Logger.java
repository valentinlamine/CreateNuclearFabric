package net.ynov.createnuclear.utils;

import net.ynov.createnuclear.CreateNuclear;

public class Logger {
    public static void SendToLog(String message) {
        CreateNuclear.LOGGER.info(message);
    }
}
