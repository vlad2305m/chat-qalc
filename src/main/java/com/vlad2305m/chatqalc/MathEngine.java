package com.vlad2305m.chatqalc;

import org.jetbrains.annotations.Contract;

import java.io.*;
import java.util.function.Consumer;

import static com.vlad2305m.chatqalc.ChatQalc.LOGGER;

public class MathEngine {

    private static Process qalc;
    public static Consumer<String> addMessage = (s)->{};

    public static void initMathEngine() {
        try {

            MathEngineInstaller.install();

            ProcessBuilder pb = new ProcessBuilder(PlatformSpecificStuff.qalcFile());

            qalc = pb.start();

            BufferedReader reader = qalc.inputReader();

            new Thread(()->readLoop(reader)).start();

        }
        catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    @Contract(value = "!null->_", pure = true)
    public static void eval(String input) {

        BufferedWriter writer = qalc.outputWriter();
        try {
            writer.write(input+"\n");
            writer.flush();
        }
        catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    @Contract(value = "_->_", pure = true)
    public static void tabComp(String input) {

        BufferedWriter writer = qalc.outputWriter();
        try {
            writer.write(input+"\t");
            writer.flush();
            writer.write("\010".repeat(input.length()+10)+"\n");
            writer.flush();
        }
        catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    @Contract(value = "!null->_", pure = true)
    public static void evalSingle(String input) {

        try {
            ProcessBuilder pb = new ProcessBuilder(PlatformSpecificStuff.qalcFile(), "-t", input);
            Process qalc2 = pb.start();
            BufferedReader reader = qalc2.inputReader();
            new Thread(()->readLoop(reader)).start();
        }
        catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    private static void readLoop(BufferedReader reader){
        while (true) {
            try { String message = reader.readLine();
                addMessage.accept(message); }
            catch (IOException e) {
                LOGGER.error(e.toString());}
        }
    }

    public static void openConfig() {
        try {
            new ProcessBuilder(PlatformSpecificStuff.qalculateFile()).start();
        }
        catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public static String reformatAnsiMinecraft(String in) {
        return in
                .replace("\033[0m", "§r") // reset
                .replace("\033[1m", "§l") // bold
                .replace("\033[3m", "§o") // italic
                .replace("\033[4m", "§n") // underline
                .replace("\033[8m", "§k") // obfuscated
                .replace("\033[9m", "§m") // strikethrough
                .replace("\033[23m", "§r") // end italic -> reset
                .replace("\033[0;30m", "§0") // black
                .replace("\033[0;34m", "§1") // blue
                .replace("\033[0;32m", "§2") // green
                .replace("\033[0;36m", "§3") // cyan
                .replace("\033[0;31m", "§4") // red
                .replace("\033[0;35m", "§5") // purple
                .replace("\033[0;33m", "§6") // gold
                .replace("\033[0;37m", "§7") // gray
                .replace("\033[0;90m", "§8") // D grey
                .replace("\033[0;94m", "§9") // B blue
                .replace("\033[0;92m", "§a") // B green
                .replace("\033[0;96m", "§b") // B cyan
                .replace("\033[0;91m", "§c") // B red
                .replace("\033[0;95m", "§d") // B purple
                .replace("\033[0;93m", "§e") // B yellow
                .replace("\033[0;97m", "§f") // white
                //.replace("\033[m", "§") //
                ;
    }

    public static String stripAnsi(String in) {
        return in
                .replace("\033[0m", "")
                .replace("\033[1m", "")
                .replace("\033[3m", "")
                .replace("\033[4m", "")
                .replace("\033[8m", "")
                .replace("\033[9m", "")
                .replace("\033[23m", "")
                .replace("\033[0;30m", "")
                .replace("\033[0;34m", "")
                .replace("\033[0;32m", "")
                .replace("\033[0;36m", "")
                .replace("\033[0;31m", "")
                .replace("\033[0;35m", "")
                .replace("\033[0;33m", "")
                .replace("\033[0;37m", "")
                .replace("\033[0;90m", "")
                .replace("\033[0;94m", "")
                .replace("\033[0;92m", "")
                .replace("\033[0;96m", "")
                .replace("\033[0;91m", "")
                .replace("\033[0;95m", "")
                .replace("\033[0;93m", "")
                .replace("\033[0;97m", "")
                //.replace("\033[m", "§") //
                ;
    }

    public static String reformatMinecraftAnsi(String in) {
        return in
                .replace("§r", "\033[0m") // reset
                .replace("§l", "\033[1m") // bold
                .replace("§o", "\033[3m") // italic
                .replace("§n", "\033[4m") // underline
                .replace("§k", "\033[8m") // obfuscated
                .replace("§m", "\033[9m") // strikethrough
                .replace("§0", "\033[0;30m") // black
                .replace("§1", "\033[0;34m") // blue
                .replace("§2", "\033[0;32m") // green
                .replace("§3", "\033[0;36m") // cyan
                .replace("§4", "\033[0;31m") // red
                .replace("§5", "\033[0;35m") // purple
                .replace("§6", "\033[0;33m") // gold
                .replace("§7", "\033[0;37m") // gray
                .replace("§8", "\033[0;90m") // D grey
                .replace("§9", "\033[0;94m") // B blue
                .replace("§a", "\033[0;92m") // B green
                .replace("§b", "\033[0;96m") // B cyan
                .replace("§c", "\033[0;91m") // B red
                .replace("§d", "\033[0;95m") // B purple
                .replace("§e", "\033[0;93m") // B yellow
                .replace("§f", "\033[0;97m") // white
                ;
    }
}
