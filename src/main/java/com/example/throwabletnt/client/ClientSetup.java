package com.example.throwabletnt.client;

import com.example.throwabletnt.ThrowableTnt;
import com.example.throwabletnt.entity.client.HomingTntRenderer;
import com.example.throwabletnt.entity.client.ThrowableTntRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ThrowableTnt.MODID, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ThrowableTnt.THROWABLE_TNT_ENTITY.get(), ThrowableTntRenderer::new);
        event.registerEntityRenderer(ThrowableTnt.HOMING_TNT_ENTITY.get(), HomingTntRenderer::new);
    }
}
