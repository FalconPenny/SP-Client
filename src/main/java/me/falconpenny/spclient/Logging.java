package me.falconpenny.spclient;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.logging.Level;

public class Logging {
    private Logging() {
    }

    private static void log(Level level, String message) {
        System.out.println(new GregorianCalendar().toZonedDateTime().format(DateTimeFormatter.ISO_LOCAL_TIME) + " (" + level.getLocalizedName() + ") --- " + message);
    }

    private static void log(Level level, String... messages) {
        Arrays.stream(messages).forEach(m -> log(level, m));
    }

    public static void info(String... message) {
        log(Level.INFO, message);
    }

    public static void warning(String... message) {
        log(Level.WARNING, message);
    }

    public static void severe(String... message) {
        log(Level.SEVERE, message);
    }

    public static void config(String... message) {
        log(Level.CONFIG, message);
    }
}
