package com.example.throwabletnt.client;

import com.example.throwabletnt.ThrowableTntSettings;
import com.example.throwabletnt.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleFunction;

public class ThrowableTntConfigScreen extends Screen {
    private final Screen parent;
    private ConfigSlider powerSlider;
    private ConfigSlider strengthSlider;
    private ConfigSlider distanceSlider;
    private Button breakBlocksButton;
    private Button saveButton;
    private boolean breakBlocks = true;

    public ThrowableTntConfigScreen(Screen parent) {
        super(Component.translatable("screen.throwabletnt.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ModNetwork.sendQuerySettings();
        this.breakBlocks = ThrowableTntSettings.breakBlocks;
        int centerX = this.width / 2;
        int startY = this.height / 4;

        this.powerSlider = new ConfigSlider(centerX - 100, startY + 40, 200, 20,
                Math.max(0.0D, Math.min(1.0D, (double) ThrowableTntSettings.power / 100.0D)),
                v -> Component.translatable("screen.throwabletnt.power", Math.round(v * 100.0D)));
        this.strengthSlider = new ConfigSlider(centerX - 100, startY + 65, 200, 20,
                Math.max(0.0D, Math.min(1.0D, ((double) ThrowableTntSettings.lockStrength - 0.5D) / 2.5D)),
                v -> Component.translatable("screen.throwabletnt.lock_strength",
                        Math.round((v * 2.5D + 0.5D) * 10.0D) / 10.0D));
        this.distanceSlider = new ConfigSlider(centerX - 100, startY + 90, 200, 20,
                Math.max(0.0D, Math.min(1.0D, ((double) ThrowableTntSettings.lockDistance - 32.0D) / 224.0D)),
                v -> Component.translatable("screen.throwabletnt.lock_distance", Math.round(v * 224.0D + 32.0D)));
        this.addRenderableWidget(this.powerSlider);
        this.addRenderableWidget(this.strengthSlider);
        this.addRenderableWidget(this.distanceSlider);

        this.breakBlocksButton = Button.builder(this.breakBlocksMessage(), btn -> {
            this.breakBlocks = !this.breakBlocks;
            btn.setMessage(this.breakBlocksMessage());
        }).bounds(centerX - 100, startY + 115, 200, 20).build();
        this.addRenderableWidget(this.breakBlocksButton);

        this.saveButton = Button.builder(Component.translatable("screen.throwabletnt.save"), btn -> {
            ModNetwork.sendSetSettings(
                    (float) (this.powerSlider.getValue() * 100.0D),
                    this.breakBlocks,
                    (float) (this.strengthSlider.getValue() * 2.5D + 0.5D),
                    (float) Math.round(this.distanceSlider.getValue() * 224.0D + 32.0D));
            this.onClose();
        }).bounds(centerX - 100, startY + 145, 98, 20).build();
        this.addRenderableWidget(this.saveButton);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), btn -> this.onClose())
                .bounds(centerX + 2, startY + 145, 98, 20).build());
    }

    private Component breakBlocksMessage() {
        return Component.translatable("screen.throwabletnt.breakblocks",
                Component.translatable(this.breakBlocks ? "screen.throwabletnt.on" : "screen.throwabletnt.off"));
    }

    @Override
    public void tick() {
        if (this.saveButton != null) {
            this.saveButton.active = ThrowableTntSettings.isOp;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 4 - 20, 0xFFFFFF);
        graphics.drawCenteredString(this.font,
                ThrowableTntSettings.isOp ? Component.translatable("screen.throwabletnt.op_yes")
                        : Component.translatable("screen.throwabletnt.op_no"),
                this.width / 2, this.height / 4 - 4,
                ThrowableTntSettings.isOp ? 0x55FF55 : 0xFF5555);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private static class ConfigSlider extends AbstractSliderButton {
        private final DoubleFunction<Component> display;

        ConfigSlider(int x, int y, int width, int height, double value, DoubleFunction<Component> display) {
            super(x, y, width, height, Component.empty(), value);
            this.display = display;
            this.updateMessage();
        }

        double getValue() {
            return this.value;
        }

        @Override
        protected void updateMessage() {
            this.setMessage(this.display.apply(this.value));
        }

        @Override
        protected void applyValue() {
        }
    }
}
