package com.example.throwabletnt.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetSettingsPayload(float power, boolean breakBlocks, float lockStrength, float lockDistance) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetSettingsPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("throwabletnt", "set_settings"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSettingsPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.FLOAT, SetSettingsPayload::power,
                    ByteBufCodecs.BOOL, SetSettingsPayload::breakBlocks,
                    ByteBufCodecs.FLOAT, SetSettingsPayload::lockStrength,
                    ByteBufCodecs.FLOAT, SetSettingsPayload::lockDistance,
                    SetSettingsPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
