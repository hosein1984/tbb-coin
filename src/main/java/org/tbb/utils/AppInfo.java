package org.tbb.utils;

public class AppInfo {
    public static final String NAME = "tbb";
    public static final String MAJOR = "0";
    public static final String MINOR = "1";
    public static final String PATCH = "0";

    public static String getVersion() {
        return String.format("%s.%s.%s", MAJOR, MINOR, PATCH);
    }
}
