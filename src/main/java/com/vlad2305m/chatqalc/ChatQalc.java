package com.vlad2305m.chatqalc;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;

import java.util.function.Consumer;

import static com.vlad2305m.chatqalc.MathEngine.reformatAnsiMinecraft;
import static com.vlad2305m.chatqalc.MathEngine.stripAnsi;

public class ChatQalc implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitializeClient() {
        MathEngine.initMathEngine();
    }

    @Contract(value = "_->_", mutates = "param1")
    public static boolean executeToChat(TextFieldWidget field) {
        String originalText = field.getText();
        if (originalText.isBlank()) return false;
        TriConsumer<Text, MessageSignatureData, MessageIndicator> textConsumer = MinecraftClient.getInstance().inGameHud.getChatHud()::addMessage;
        MathEngine.addMessage = (s)->{
            if (s!=null)
                textConsumer.accept(Text.literal(reformatAnsiMinecraft(s)), null, new MessageIndicator(15677346, null, Text.literal("Qalculate!"), "chatqalc"));
        };
        MathEngine.eval(originalText);
        MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(originalText);
        field.setText("");
        return true;
    }

    @Contract(value = "_,!null->_", mutates = "param1")
    public static boolean executeAndBroadcast(TextFieldWidget field, Consumer<String> broadcast) {
        String originalText = field.getText();
        if (originalText.isBlank()) return false;
        MathEngine.addMessage = (s)->{if (!s.isBlank() && !(s.charAt(0) =='>')) broadcast.accept(stripAnsi(s));};
        MathEngine.eval(originalText);
        MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(originalText);
        field.setText("");
        return true;
    }

    @Contract(value = "_->_")
    public static boolean executeQuietly(String expr) {
        if (expr.isBlank()) return false;
        MathEngine.addMessage = (s)->{};
        MathEngine.eval(expr);
        return true;
    }

    @Contract(value = "_->_", mutates = "param1")
    public static boolean executeToInput(TextFieldWidget field) {
        String originalText = field.getText();
        if (originalText.isBlank()) return false;
        MathEngine.addMessage = (s)->{if (!s.isBlank()) field.setText(stripAnsi(s.strip()));};
        MathEngine.eval(originalText);
        MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(originalText);
        return true;
    }

    @Contract(value = "_->_", mutates = "param1")
    public static boolean substituteWord(TextFieldWidget field) {
        String originalText = field.getText();
        if (originalText.isBlank()) return false;
        int end = field.getCursor();
        int i;
        for (i = originalText.lastIndexOf(" ", end-1); i > 0 && originalText.charAt(i-1) == '\\'; i = originalText.lastIndexOf(" ", i-1));
        int start = i + 1;
        String expr = originalText.substring(start, end).replace("\\ ", "");
        if(expr.isBlank()) return false;
        MathEngine.addMessage = (s)->{if (s!=null && !s.isBlank()) {
                field.setText(stripAnsi(originalText.substring(0, start) + s.strip() + originalText.substring(end)));
                field.setCursor(start + s.length());
        }};
        MathEngine.evalSingle(expr);
        MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(originalText);
        return true;
    }

    @Contract(value = "_->_", mutates = "param1")
    public static boolean getCompletions(TextFieldWidget field) {
        String originalText = field.getText();
        if (originalText.isBlank()) return false;
        TriConsumer<Text, MessageSignatureData, MessageIndicator> textConsumer = MinecraftClient.getInstance().inGameHud.getChatHud()::addMessage;
        MathEngine.addMessage = (s)->{
            if (s!=null && !s.isBlank() && !s.equals("  \033[0;36m0\033[0m = \033[0;36m0\033[0m")) {
                s = s.replace("\010", "");
                if (s.length() > 1 && s.charAt(0)=='>') field.setText(stripAnsi(s.substring(1).strip()));
                else textConsumer.accept(Text.literal(reformatAnsiMinecraft(s)), null, new MessageIndicator(15677346, null, Text.literal("Qalculate!"), "chatqalc"));
            }
        };
        MathEngine.tabComp(originalText);
        return true;
    }
}
