package net.vinh.library_of_silence.networking.screenshake;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.library_of_silence.handlers.AbilityHandler;

public class AbilityActivationPacket {
    public static final Identifier ABILITY_SYNC = new Identifier("compass", "ability_sync");

    // Server -> Client
    public static void sendAbilitySync(ServerPlayerEntity player, Identifier abilityId, String effectKey) {
        PacketByteBuf buf = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
        buf.writeIdentifier(abilityId); // e.g. silence:excalibur
        buf.writeString(effectKey);     // e.g. "blackhole", "screenshake"

        ServerPlayNetworking.send(player, ABILITY_SYNC, buf);
    }

    // Register client-side receiver (library only, no rendering here!)
    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ABILITY_SYNC, (client, handler, buf, responseSender) -> {
            Identifier abilityId = buf.readIdentifier();
            String effectKey = buf.readString();

            // Pass to a callback — Compass itself does nothing visual
            client.execute(() -> AbilityHandler.onAbilityTriggered(abilityId, effectKey));
        });
    }
}
