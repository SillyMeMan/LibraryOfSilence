package net.vinh.library_of_silence.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.random.Random;
import net.vinh.library_of_silence.handlers.ScreenshakeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void silentlibrary$tick(CallbackInfo ci) {
        ScreenshakeHandler.clientTick(MinecraftClient.getInstance().gameRenderer.getCamera(), Random.createLocal());
    }
}
