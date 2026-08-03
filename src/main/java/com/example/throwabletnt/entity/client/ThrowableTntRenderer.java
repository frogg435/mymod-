package com.example.throwabletnt.entity.client;

import com.example.throwabletnt.ThrowableTnt;
import com.example.throwabletnt.entity.ThrowableTntEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ThrowableTntRenderer extends EntityRenderer<ThrowableTntEntity> {
    public ThrowableTntRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ThrowableTntEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(((float) entity.tickCount + partialTick) * 30.0F));
        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) {
            stack = new ItemStack(ThrowableTnt.THROWABLE_TNT.get());
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowableTntEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
