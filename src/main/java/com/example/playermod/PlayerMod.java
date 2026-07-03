package com.example.playermod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

@Mod("playermod")
public class PlayerMod {
    private static KeyBinding toggleKey;
    private static boolean espEnabled = false;

    public PlayerMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // "U" tugmasini sozlash
        toggleKey = new KeyBinding("key.playermod.toggle", GLFW.GLFW_KEY_U, "key.categories.misc");
        ClientRegistry.registerKeyBinding(toggleKey);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && toggleKey != null && toggleKey.isPressed()) {
            espEnabled = !espEnabled;
            // O'yin ichida rejim o'zgarganini bildirish
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.chat("ESP " + (espEnabled ? "ON" : "OFF"));
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!espEnabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        // O'yinchilarni devor ortidan ko'rsatish (Glow effekti)
        for (PlayerEntity player : mc.level.players()) {
            if (player == mc.player) continue; // O'zimizni hisobga olmaymiz

            // Masofani tekshirish
            if (player.isAlive()) {
                player.setGlowing(true); // Minecraft'ning ichki yorug'lik (glow) effektini yoqish
            }
        }
    }
}