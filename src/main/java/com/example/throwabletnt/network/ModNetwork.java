package com.example.throwabletnt.network;

import com.example.throwabletnt.ThrowableTnt;
import com.example.throwabletnt.ThrowableTntSettings;
import com.example.throwabletnt.entity.ThrowableTntEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ThrowableTnt.MODID)
public class ModNetwork {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SetPowerPayload.TYPE, SetPowerPayload.STREAM_CODEC, ModNetwork::handleSetPower);
        registrar.playToServer(QuerySettingsPayload.TYPE, QuerySettingsPayload.STREAM_CODEC, ModNetwork::handleQuerySettings);
        registrar.playToClient(SettingsSyncPayload.TYPE, SettingsSyncPayload.STREAM_CODEC, ModNetwork::handleSettingsSync);
    }

    private static void handleSetPower(SetPowerPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!context.flow().isServerbound()) {
                return;
            }
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.hasPermissions(2)) {
                    ThrowableTntEntity.power = Math.max(0.0F, Math.min(100.0F, payload.power()));
                    ThrowableTnt.LOGGER.info("[throwabletnt] {} 设置爆炸威力为 {}", serverPlayer.getName().getString(), ThrowableTntEntity.power);
                    PacketDistributor.sendToPlayer(serverPlayer, new SettingsSyncPayload(ThrowableTntEntity.power, true));
                } else {
                    serverPlayer.sendSystemMessage(Component.translatable("message.throwabletnt.no_permission"));
                }
            }
        });
    }

    private static void handleQuerySettings(QuerySettingsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!context.flow().isServerbound()) {
                return;
            }
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                boolean op = serverPlayer.hasPermissions(2);
                PacketDistributor.sendToPlayer(serverPlayer, new SettingsSyncPayload(ThrowableTntEntity.power, op));
            }
        });
    }

    private static void handleSettingsSync(SettingsSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ThrowableTntSettings.power = payload.power();
            ThrowableTntSettings.isOp = payload.op();
        });
    }

    public static void sendSetPower(float power) {
        PacketDistributor.sendToServer(new SetPowerPayload(power));
    }

    public static void sendQuerySettings() {
        PacketDistributor.sendToServer(new QuerySettingsPayload());
    }
}
