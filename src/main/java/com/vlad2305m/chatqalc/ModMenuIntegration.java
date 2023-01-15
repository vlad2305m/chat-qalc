package com.vlad2305m.chatqalc;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new Screen(Text.literal("Qalculate!")){
            private boolean openedQalc = false;
            @Override
            public void close() {
                assert this.client != null;
                this.client.setScreen(screen);
            }

            @Override
            protected void init() {
                if (!openedQalc){
                    MathEngine.openConfig();
                    openedQalc = true;
                }
                close();
            }
        };

    }

}