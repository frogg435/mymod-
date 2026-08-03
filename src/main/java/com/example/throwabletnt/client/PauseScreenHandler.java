package com.example.throwabletnt.client;

import com.example.throwabletnt.ThrowableTnt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = ThrowableTnt.MODID, value = Dist.CLIENT)
public class PauseScreenHandler {
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof PauseScreen) {
            int x = event.getScreen().width / 2 - 102;
            int y = event.getScreen().height - 40;
            event.addListener(Button.builder(Component.translatable("screen.throwabletnt.button"),
                            btn -> Minecraft.getInstance().setScreen(new ThrowableTntConfigScreen(event.getScreen())))
                    .bounds(x, y, 204, 20).build());
        }
    }
}
