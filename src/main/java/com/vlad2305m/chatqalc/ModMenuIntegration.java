package com.vlad2305m.chatqalc;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Objects;

import static com.vlad2305m.chatqalc.ChatQalc.executeQuietly;
import static com.vlad2305m.chatqalc.MathEngine.initMathEngine;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(Text.literal("Qalculate!")){
            private static boolean qalculateOpen = false;
            private boolean closed = false;
            private Process qalculateProcess = null;
            public boolean shouldCloseOnEsc() {return false;}
            @Override
            public void close() {
                if (closed) return;
                if (qalculateProcess!=null) qalculateProcess.destroy();
                assert this.client != null;
                this.client.setScreen(screen);
                closed = true;
            }

            private MultilineText message = null;
            private MultilineText warning = null;
            @Override
            protected void init() {
                Text message;
                if (!qalculateOpen){
                    message = Text.literal("Opening Qalculate!...\nSettings are in the 'Edit' tab");
                    executeQuietly("exit");
                    qalculateProcess = MathEngine.openConfig();
                    if (qalculateProcess!=null) {
                        qalculateOpen = true;
                        new Thread(() -> {
                            try {
                                qalculateProcess.waitFor();
                            } catch (InterruptedException ignored) {
                            }
                            initMathEngine();
                            qalculateOpen = false;
                            toBeClosed = true;
                        }).start();
                    } else {
                        message = Text.literal("Error: could not open Qalculate!");
                    }
                }
                else {
                    message = Text.literal("Qalculate! is already open.\nSettings are in the 'Edit' tab");
                }
                this.message = MultilineText.create(this.textRenderer, message, this.width - 50);
                warning = MultilineText.create(this.textRenderer, Text.literal("/!\\ I don't want to change any settings. I know that settings changes may not be saved if 2 calculators are open at the same time, so I want to:"), this.width - 50);
                ButtonWidget.Builder closeButton = ButtonWidget.builder(Text.literal("Close"), (button) -> {
                    close();
                });
                this.addDrawableChild(closeButton.dimensions(this.width / 2 - 100, this.height / 4 + this.message.count()*9 / 2 + 9, 200, 20).build());
                ButtonWidget.Builder qalculateButton = ButtonWidget.builder(Text.literal("Open New Window"), (button) -> {
                    MathEngine.openConfig();
                });
                this.addDrawableChild(qalculateButton.dimensions(this.width / 2 - 200, this.height / 2 + this.warning.count()*27 / 2 + 48, 195, 20).build());
                ButtonWidget.Builder qalcButton = ButtonWidget.builder(Text.literal("Continue using chatqalc"), (button) -> {
                    assert this.client != null;
                    this.client.setScreen(screen);
                    closed = true;
                    initMathEngine();
                });
                this.addDrawableChild(qalcButton.dimensions(this.width / 2 + 5, this.height / 2 + this.warning.count()*27 / 2 + 48, 195, 20).build());
            }
            private boolean toBeClosed = false;
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if (toBeClosed) close();
                this.renderBackground(matrices);
                Objects.requireNonNull(this.textRenderer);
                this.message.drawCenterWithShadow(matrices, this.width / 2, this.height / 4 - this.message.count()*9 / 2);
                this.warning.drawCenterWithShadow(matrices, this.width / 2, this.height / 2 + this.warning.count()*9 / 2+19);
                super.render(matrices, mouseX, mouseY, delta);
            }
        };

    }

}