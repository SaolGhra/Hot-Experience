package com.saolghra.hot_experience.client;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class Hot_experienceClient implements ClientModInitializer {

    public static SwapKeyBinding keyBinding;

    @Override
    public void onInitializeClient() {

        // Logging the new keybinding and applying the supers
        keyBinding = new SwapKeyBinding("Totem-Enchant Swapper", GLFW.GLFW_KEY_RIGHT_BRACKET, "Hot Experience");
        KeyBindingHelper.registerKeyBinding(keyBinding);
    }
}