package net.vinh.library_of_silence.systems;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.library_of_silence.handlers.AbilityHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractAbility {
    private static final Map<UUID, AbilityState> PLAYER_STATES = new HashMap<>();

    public abstract Identifier getId();

    public abstract int getChargeUpTicks();

    public abstract int getCooldownTicks();

    public int getInterruptedCooldownTicks() {
        return 20 * 60; // default 1 min
    }

    protected abstract void execute(ServerPlayerEntity player);

    // --- Logic ---

    public void startCharge(ServerPlayerEntity player) {
        AbilityState state = new AbilityState();
        state.charging = true;
        state.chargeEndTick = player.getServer().getTicks() + getChargeUpTicks();
        PLAYER_STATES.put(player.getUuid(), state);
    }

    public void tick(ServerPlayerEntity player) {
        AbilityState state = PLAYER_STATES.get(player.getUuid());
        if (state == null) return;

        if (state.charging && player.getServer().getTicks() >= state.chargeEndTick) {
            state.charging = false;
            state.cooldownEndTick = player.getServer().getTicks() + getCooldownTicks();
            execute(player);
            AbilityHandler.onAbilityTriggered(getId(), "cast_success");
        }

        if (state.charging && player.hurtTime > 0) {
            state.charging = false;
            state.cooldownEndTick = player.getServer().getTicks() + getInterruptedCooldownTicks();
            AbilityHandler.onAbilityTriggered(getId(), "cast_interrupted");
        }
    }

    public boolean canCast(ServerPlayerEntity player) {
        AbilityState state = PLAYER_STATES.get(player.getUuid());
        if (state == null) return true;
        return player.getServer().getTicks() >= state.cooldownEndTick;
    }

    // --- Persistence ---

    public static void save(ServerPlayerEntity player, NbtCompound tag) {
        AbilityState state = PLAYER_STATES.get(player.getUuid());
        if (state != null) tag.put("AbilityState", state.toNbt());
    }

    public static void load(ServerPlayerEntity player, NbtCompound tag) {
        if (tag.contains("AbilityState")) {
            AbilityState state = AbilityState.fromNbt(tag.getCompound("AbilityState"));
            PLAYER_STATES.put(player.getUuid(), state);
        }
    }

    // --- Helper Class ---
    private static class AbilityState {
        boolean charging = false;
        long chargeEndTick = 0;
        long cooldownEndTick = 0;

        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("charging", charging);
            nbt.putLong("chargeEnd", chargeEndTick);
            nbt.putLong("cooldownEnd", cooldownEndTick);
            return nbt;
        }

        public static AbilityState fromNbt(NbtCompound nbt) {
            AbilityState state = new AbilityState();
            state.charging = nbt.getBoolean("charging");
            state.chargeEndTick = nbt.getLong("chargeEnd");
            state.cooldownEndTick = nbt.getLong("cooldownEnd");
            return state;
        }
    }
}
