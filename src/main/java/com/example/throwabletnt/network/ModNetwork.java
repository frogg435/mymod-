package com.example.throwabletnt.network;

import com.example.throwabletnt.ThrowableTnt;
import com.example.throwabletnt.ThrowableTntSettings;
import com.example.throwabletnt.entity.HomingTntEntity;
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
        PayloadRegistrar registrar = event.registrar("2");
        registrar.playToServer(SetSettingsPayload.TYPE, SetSettingsPayload.STREAM_CODEC, ModNetwork::handleSetSettings);
        registrar.playToServer(QuerySettingsPayload.TYPE, QuerySettingsPayload.STREAM_CODEC, ModNetwork::handleQuerySettings);
        registrar.playToClient(SettingsSyncPayload.TYPE, SettingsSyncPayload.STREAM_CODEC, ModNetwork::handleSettingsSync);
    }

    private static void handleSetSettings(SetSettingsPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!context.flow().isServerbound()) {
                return;
            }
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.hasPermissions(2)) {
                    ThrowableTntEntity.power = Math.max(0.0F, Math.min(100.0F, payload.power()));
                    ThrowableTntEntity.breakBlocks = payload.breakBlocks();
                    HomingTntEntity.lockStrength = Math.max(0.0F, Math.min(5.0F, payload.lockStrength()));
                    HomingTntEntity.lockDistance = Math.max(16.0D, Math.min(512.0D, payload.lockDistance()));
                    ThrowableTnt.LOGGER.info("[throwabletnt] {} 设置参数: 威力={}, 破坏方块={}, 锁定强度={}, 锁定距离={}",
                            serverPlayer.getName().getString(),
                            ThrowableTntEntity.power, ThrowableTntEntity.breakBlocks,
                            HomingTntEntity.lockStrength, HomingTntEntity.lockDistance);
                    PacketDistributor.sendToPlayer(serverPlayer, new SettingsSyncPayload(
                            ThrowableTntEntity.power, ThrowableTntEntity.breakBlocks,
                            HomingTntEntity.lockStrength, (float) HomingTntEntity.lockDistance, true));
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
                PacketDistributor.sendToPlayer(serverPlayer, new SettingsSyncPayload(
                        ThrowableTntEntity.power, ThrowableTntEntity.breakBlocks,
                        HomingTntEntity.lockStrength, (float) HomingTntEntity.lockDistance, op));
            }
        });
    }

    private static void handleSettingsSync(SettingsSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ThrowableTntSettings.power = payload.power();
            ThrowableTntSettings.breakBlocks = payload.breakBlocks();
            ThrowableTntSettings.lockStrength = payload.lockStrength();
            ThrowableTntSettings.lockDistance = payload.lockDistance();
            ThrowableTntSettings.isOp = payload.op();
        });
    }

    public static void sendSetSettings(float power, boolean breakBlocks, float lockStrength, float lockDistance) {
        PacketDistributor.sendToServer(new SetSettingsPayload(power, breakBlocks, lockStrength, lockDistance));
    }

    public static void sendQuerySettings() {
        PacketDistributor.sendToServer(new QuerySettingsPayload());
    }
}
