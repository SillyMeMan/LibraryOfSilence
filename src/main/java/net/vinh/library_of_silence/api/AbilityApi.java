package net.vinh.library_of_silence.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.library_of_silence.systems.AbstractAbility;

import java.util.HashMap;
import java.util.Map;

public class AbilityApi {
    private static final Map<Identifier, AbstractAbility> ABILITIES = new HashMap<>();

    public static void register(AbstractAbility ability) {
        ABILITIES.put(ability.getId(), ability);
    }

    public static AbstractAbility get(Identifier id) {
        return ABILITIES.get(id);
    }

    public static void tickAll(ServerPlayerEntity player) {
        ABILITIES.values().forEach(ability -> ability.tick(player));
    }
}
