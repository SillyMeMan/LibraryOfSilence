package net.vinh.library_of_silence.systems;

import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbstractAbility {

    protected boolean isCharging = false;
    protected int chargeTicks = 0;
    protected int cooldownTicks = 0;

    protected final int MAX_CHARGE_TICKS;
    protected final int SUCCESS_COOLDOWN_TICKS;
    protected final int FAIL_COOLDOWN_TICKS;

    public AbstractAbility(int chargeTime, int cooldownIfSuccess, int cooldownIfFailed) {
        this.MAX_CHARGE_TICKS = chargeTime;
        this.SUCCESS_COOLDOWN_TICKS = cooldownIfSuccess;
        this.FAIL_COOLDOWN_TICKS = cooldownIfFailed;
    }

    /** Call this method in your server tick event. */
    public void tick(ServerPlayerEntity player) {
        if (cooldownTicks > 0) cooldownTicks--;

        if (isCharging) {
            chargeTicks++;

            if (chargeTicks >= MAX_CHARGE_TICKS) {
                if (canCast(player)) {
                    cast(player);
                    applySuccessCooldown();
                } else {
                    cancel();
                    applyCancelledlCooldown();
                }
            }
        }
    }

    /** Starts the ability charge-up. */
    public void startCharging() {
        if (!isOnCooldown() && !isCharging()) {
            isCharging = true;
            chargeTicks = 0;
        }
    }

    /** Cancels the current ability charge. */
    public void cancel() {
        isCharging = false;
        chargeTicks = 0;
    }

    /** Applies success cooldown. */
    public void applySuccessCooldown() {
        cooldownTicks = SUCCESS_COOLDOWN_TICKS;
        isCharging = false;
        chargeTicks = 0;
    }

    /** Applies failure cooldown. */
    public void applyCancelledlCooldown() {
        cooldownTicks = FAIL_COOLDOWN_TICKS;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public boolean isOnCooldown() {
        return cooldownTicks > 0;
    }

    /** Can override this for item conditions, area checks, health gates, etc. */
    public boolean canCast(ServerPlayerEntity player) {
        return true;
    }

    /** Override to apply actual damage/effects. */
    public abstract void cast(ServerPlayerEntity player);
}
