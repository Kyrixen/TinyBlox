package io.kyrixen.tinyblox.utils;

public class Logger {
    
    public static final Logger LOGGER = new Logger();

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String AQUA = "\u001B[96m";
    public static final String GRAY = "\u001B[90m";
    public static final String BLUE = "\u001B[34m";

    public boolean debug = false;

    public void info(String category, String text) {
        System.out.println(BLUE + "[INFO] " + AQUA + "[" + category + "] " + RESET + text);
    }
    public void warn(String category, String text) {
        System.out.println(BLUE + "[WARN] " + AQUA + "[" + category + "] " + YELLOW + text + RESET);
    }

    public void error(String category, String text) {
        System.out.println(BLUE + "[ERROR] " + AQUA + "[" + category + "] " + RESET + RED + text + RESET);
    }

    public void debug(String category, String text) {
        if(!debug) return;
        System.out.println(BLUE + "[DEBUG] " + AQUA + "[" + category + "] " + RESET + GRAY + text + RESET);
    }

    public void setDebug(boolean debugState) {
        this.debug = debugState;
    }

}
