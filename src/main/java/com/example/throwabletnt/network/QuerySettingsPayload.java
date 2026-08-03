package com.example.throwabletnt.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record QuerySettingsPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<QuerySettingsPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("throwabletnt", "query_settings"));
    public static final StreamCodec<ByteBuf, QuerySettingsPayload> STREAM_CODEC =
            StreamCodec.of((buf, payload) -> {}, buf -> new QuerySettingsPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
