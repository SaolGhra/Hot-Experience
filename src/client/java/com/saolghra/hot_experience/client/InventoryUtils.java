package com.saolghra.hot_experience.client;

import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtils {

    @Environment(net.fabricmc.api.EnvType.CLIENT)
    public static void swapExperience(MinecraftClient client) {

        // Check if the Players Client is not nothing (null)
        if (client.player == null) return;

        // Check if the player is actually a player
        if (!(client.player instanceof ClientPlayerEntity)) return;

        // Check if the player is dead or not
        if (client.player.isDead()) return;

        // Slots to Swap to
        int expBottleSlot = -1;
        int totemSlot = -1;

        int HOTBAR_SIZE = PlayerInventory.getHotbarSize(); // Listing to the code that 9 slots exist
        int MAIN_SIZE = PlayerInventory.MAIN_SIZE; // 36
        int TOTAL_SIZE = MAIN_SIZE + 1; // Offhand

        int[] range = new int[TOTAL_SIZE];

        // Main Inventory
        for (int i = 0; i < MAIN_SIZE - HOTBAR_SIZE; i++) {
            range[i] = i + HOTBAR_SIZE;
        }

        // Offhand
        range[MAIN_SIZE - HOTBAR_SIZE] = PlayerInventory.OFF_HAND_SLOT; // 40

        // Hotbar
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            range[i + MAIN_SIZE - HOTBAR_SIZE + 1] = i;
        }

        // Find EXPBottlesSlots and TotemSlots
        for (int slot : range) {

            // Get the ItemStack in the slot
            ItemStack stack = client.player.getInventory().getStack(slot);

            // Check if the ItemStack is empty
            if (stack.isEmpty()) {
                continue;
            }

            else {

                // Check if the item is an experience Bottle
                if (isExpBottle(stack) && expBottleSlot < 0) {
                    expBottleSlot = slot;
                }

                // Check if the item is a totem of undying
                else if (isTotem(stack) && totemSlot < 0) {
                    totemSlot = slot;
                }
            }
        }

        ItemStack wearedItemStack = client.player.getInventory().getStack(PlayerInventory.OFF_HAND_SLOT);

        if (wearedItemStack.isEmpty() && expBottleSlot >= 0) {
            sendSwapPackets(expBottleSlot, client);
        }

        else if (isExpBottle(wearedItemStack) && totemSlot >= 0) {
            sendSwapPackets(totemSlot, client);
        }

        else if (isTotem(wearedItemStack) && expBottleSlot >= 0) {
            sendSwapPackets(expBottleSlot, client);
        }
    }

    private static boolean isExpBottle(ItemStack stack) {
        return stack.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private static boolean isTotem(ItemStack stack) {
        return stack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    private static void sendSwapPackets(int slot, MinecraftClient client) {
        int sendSlot = slot;

        if (sendSlot == PlayerInventory.OFF_HAND_SLOT) sendSlot += 5; // Offset of the offhand

        if (sendSlot < PlayerInventory.getHotbarSize()) sendSlot += PlayerInventory.MAIN_SIZE; // Offset of the toolbar

        // Take the item to swap to
        assert client.interactionManager != null;
        assert client.player != null;
        client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, sendSlot, 0, SlotActionType.PICKUP, client.player);

        // Put it in the offhand slot
        client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, client.player);

        // Put back what was in the offhand slot (can be air)
        client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, sendSlot, 0, SlotActionType.PICKUP, client.player);
    }
}
