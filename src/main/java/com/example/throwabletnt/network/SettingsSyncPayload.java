package com.example.throwabletnt.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SettingsSyncPayload(float power, boolean op) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SettingsSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("throwabletnt", "settings_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SettingsSyncPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.FLOAT, SettingsSyncPayload::power,
                    ByteBufCodecs.BOOL, SettingsSyncPayload::op, SettingsSyncPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
