package net.vinh.library_of_silence.handlers;

import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class AbilityHandler {
    public static BiConsumer<Identifier, String> HANDLER = (id, effect) -> {};

    public static void onAbilityTriggered(Identifier abilityId, String effectKey) {
        HANDLER.accept(abilityId, effectKey);
    }
}
