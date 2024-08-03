package com.saolghra.hot_experience.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

public class SwapKeyBinding extends KeyBinding {

    private Key key;

    private boolean pressedBypass;

    public SwapKeyBinding(String translationKey, int defaultkey, String category) {
        super(
                translationKey,
                InputUtil.Type.KEYSYM,
                defaultkey,
                category
        );

        key = this.getDefaultKey();
        pressedBypass = false;
    }

    @Override
    public void setBoundKey(Key boundKey) {
        key = boundKey;
        super.setBoundKey(boundKey);
    }

    public Key getKey() {
        return key;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if(pressed) onPressed();
    }

    public void onPressed() {
        InventoryUtils.swapExperience(MinecraftClient.getInstance());
    }

    public boolean isPressedBypass() {
        return pressedBypass;
    }

    public void setPressedBypass(boolean pressed) {
        pressedBypass = pressed;
        if (pressed) onPressedBypass();
    }

    public void onPressedBypass() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();

            // If the inventory screen, trigger a swap
            if (client.currentScreen instanceof InventoryScreen) {
                InventoryUtils.swapExperience(client);
            }
        } catch (Exception e) {
            System.out.println("ECS CRE Inventory swapping isn't compatible with this version of minecraft.");
        }
    }
}
