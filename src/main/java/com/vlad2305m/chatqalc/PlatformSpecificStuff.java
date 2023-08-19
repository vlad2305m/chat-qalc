package com.vlad2305m.chatqalc;

import java.io.IOException;

public class PlatformSpecificStuff {

    public static boolean isLinux() {
        return System.getProperty("os.name").matches(".*[Ll]inux.*");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").matches(".*Windows.*");
    }
    public static String qalcFile() {
        if (isLinux()) return qalcDir()+"qalc";
        if (isWindows()) return qalcDir()+"qalc.exe";
        return "qalc";
    }

    public static String qalculateFile() {
        if (isLinux()) return qalcDir()+"qalculate";
        if (isWindows()) return qalcDir()+"qalculate-gtk.exe";
        return "qalculate-gtk";
    }

    public static String qalcDir() {
        if (isLinux()) return "./config/chatqalc/qalculate-4.7.0/";
        if (isWindows()) return "./config/chatqalc/qalculate/";
        return "";
    }

    public static String zipName() {
        if (isLinux()) return "qalculate-4.7.0-x64.lin.zip";
        if (isWindows()) return "qalculate-4.7.0-x64.win.zip";
        throw new RuntimeException("Please install libqalculate or qalculate manually, check that command \"qalc\" (and optionally \"qalculate-gtk\") is working AND create folder \".minecraft/config/chatqalc/\" to confirm.");
    }

    public static void linuxPerms() throws IOException {
        if (isLinux()) {
            Runtime.getRuntime().exec("chmod +x "+qalcFile());
            Runtime.getRuntime().exec("chmod +x "+qalculateFile());
        }
    }

}
