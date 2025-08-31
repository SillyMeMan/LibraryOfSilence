package net.vinh.library_of_silence;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.vinh.library_of_silence.api.AbilityApi;
import net.vinh.library_of_silence.handlers.ScreenshakeHandler;
import net.vinh.library_of_silence.systems.ServerTickScheduledExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SilentLibrary implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("silentlibrary");

    @Override
    public void onInitialize() {
        ScreenshakeHandler.init();

        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> minecraftServer.getPlayerManager().getPlayerList().forEach(AbilityApi::tickAll));
        ServerTickEvents.START_SERVER_TICK.register(ServerTickScheduledExecutorService::tick);
    }
}
