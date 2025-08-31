package net.vinh.library_of_silence.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.vinh.library_of_silence.networking.screenshake.AbilityActivationPacket;
import net.vinh.library_of_silence.networking.screenshake.PositionedScreenshakePacket;
import net.vinh.library_of_silence.networking.screenshake.ScreenshakePacket;

public class SilenceLibraryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AbilityActivationPacket.registerClientReceiver();

        ClientPlayNetworking.registerGlobalReceiver(ScreenshakePacket.ID, (client, handler, buf, responseSender) -> new ScreenshakePacket(buf).apply(client.getNetworkHandler()));
        ClientPlayNetworking.registerGlobalReceiver(PositionedScreenshakePacket.ID, (client, handler, buf, responseSender) -> PositionedScreenshakePacket.fromBuf(buf).apply(client.getNetworkHandler()));
    }
}
