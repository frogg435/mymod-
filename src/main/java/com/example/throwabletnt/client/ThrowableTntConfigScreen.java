package com.example.throwabletnt.client;

import com.example.throwabletnt.ThrowableTntSettings;
import com.example.throwabletnt.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ThrowableTntConfigScreen extends Screen {
    private final Screen parent;
    private PowerSlider powerSlider;
    private Button saveButton;

    public ThrowableTntConfigScreen(Screen parent) {
        super(Component.translatable("screen.throwabletnt.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ModNetwork.sendQuerySettings();
        int centerX = this.width / 2;
        int startY = this.height / 4;
        this.powerSlider = new PowerSlider(centerX - 100, startY + 40, 200, 20,
                Math.max(0.0D, Math.min(1.0D, (double) ThrowableTntSettings.power / 100.0D)));
        this.addRenderableWidget(this.powerSlider);
        this.saveButton = Button.builder(Component.translatable("screen.throwabletnt.save"), btn -> {
            ModNetwork.sendSetPower((float) (this.powerSlider.getValue() * 100.0D));
            this.onClose();
        }).bounds(centerX - 100, startY + 70, 98, 20).build();
        this.addRenderableWidget(this.saveButton);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), btn -> this.onClose())
                .bounds(centerX + 2, startY + 70, 98, 20).build());
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

    private static class PowerSlider extends AbstractSliderButton {
        PowerSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Component.empty(), value);
            this.updateMessage();
        }

        double getValue() {
            return this.value;
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.translatable("screen.throwabletnt.power", Math.round(this.value * 100.0D)));
        }

        @Override
        protected void applyValue() {
        }
    }
}
