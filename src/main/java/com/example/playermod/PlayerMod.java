package com.example.playermod;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod("playermod")
public class PlayerMod {
    public static KeyMapping toggleEspKey;
    public static boolean isEspEnabled = false;

    public PlayerMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = "playermod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            toggleEspKey = new KeyMapping(
                    "key.playermod.toggle",
                    GLFW.GLFW_KEY_U,
                    "category.playermod"
            );
            event.register(toggleEspKey);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (toggleEspKey.consumeClick()) {
                isEspEnabled = !isEspEnabled;
                if (Minecraft.getInstance().player != null) {
                    String holat = isEspEnabled ? "§aYoqildi" : "§cO'chirildi";
                    Minecraft.getInstance().player.displayClientMessage(
                            Component.literal("Player Finder " + holat), true
                    );
                }
            }
        }
    }
}