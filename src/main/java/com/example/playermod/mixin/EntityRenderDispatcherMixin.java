package com.example.playermod.mixin;

import com.example.playermod.PlayerMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private <E extends Entity> void onRender(E entity, double x, double y, double z, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (PlayerMod.isEspEnabled && entity instanceof Player && entity != Minecraft.getInstance().player) {
            RenderSystem.disableDepthTest();
            AABB box = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());
            LevelRenderer.renderLineBox(poseStack, buffer, box, 1.0F, 0.0F, 0.0F, 1.0F);
            if (entity.isInvisible()) {
                entity.setInvisible(false);
            }
            RenderSystem.enableDepthTest();
        }
    }
}