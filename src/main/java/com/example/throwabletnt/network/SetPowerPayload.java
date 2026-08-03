package com.example.throwabletnt.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetPowerPayload(float power) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetPowerPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("throwabletnt", "set_power"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetPowerPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.FLOAT, SetPowerPayload::power, SetPowerPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
