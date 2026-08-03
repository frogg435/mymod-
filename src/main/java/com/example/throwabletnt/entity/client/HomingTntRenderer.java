package com.example.throwabletnt.entity.client;

import com.example.throwabletnt.entity.HomingTntEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;

public class HomingTntRenderer extends EntityRenderer<HomingTntEntity> {
    public HomingTntRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(HomingTntEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(((float) entity.tickCount + partialTick) * 30.0F));
        poseStack.scale(0.6F, 0.6F, 0.6F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.TNT.defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(HomingTntEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
