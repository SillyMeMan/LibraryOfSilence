package net.vinh.library_of_silence.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.library_of_silence.systems.AbstractAbility;

import java.util.HashMap;
import java.util.Map;

public class AbilityApi {
    private static final Map<Class<? extends AbstractAbility>, AbstractAbility> ABILITIES = new HashMap<>();

    public static <T extends AbstractAbility> T register(T ability) {
        ABILITIES.put(ability.getClass(), ability);
        return ability;
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractAbility> T get(Class<T> clazz) {
        return (T) ABILITIES.get(clazz);
    }

    public static void tickAll(ServerPlayerEntity player) {
        ABILITIES.values().forEach(ability -> ability.tick(player));
    }
}
