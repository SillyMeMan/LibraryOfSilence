package net.vinh.library_of_silence.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.vinh.library_of_silence.SilentLibrary;
import net.vinh.library_of_silence.exception.InvalidContextException;
import net.vinh.library_of_silence.systems.IDamageCalculationType;
import net.vinh.library_of_silence.systems.IDamageContext;
import net.vinh.library_of_silence.systems.ServerTickScheduledExecutorService;
import net.vinh.library_of_silence.util.SilentLibraryUtil;

import java.util.List;

/**
 * Damage calculation types can be implemented in a class, but if you're doing
 * a lot of damage calculation types, you can do it in an enum instead like this enum
 */
public enum SilentLibraryDamageCalculationTypes implements IDamageCalculationType {
    DISTRIBUTE((ctx -> {
        if (ctx.getTargets().isEmpty()) return;
        if (ctx instanceof DefaultDamageContext def) {
            float finalDamage = ctx.getBaseDamage() / ctx.getTargets().size() / def.getDamageTicks();

            for (LivingEntity target : ctx.getTargets()) {
                for (int i = 0; i < def.getDamageTicks(); i++) {
                    int delay = i * def.getTickInterval();

                    ServerTickScheduledExecutorService.schedule(delay, () -> {
                        target.damage(def.getSource(), finalDamage);
                        SilentLibraryUtil.applyKnockbackAndEffects(ctx, target);
                    });
                }
            }
        }
        else {
            throw new InvalidContextException(); // Can be replaced with more robust logging
        }
    })),
    AOE((ctx) -> {
        if(ctx instanceof DefaultDamageContext def) {
            float finalDamage = ctx.getBaseDamage() / ctx.getDamageTicks();

            for (LivingEntity target : ctx.getTargets()) {
                for (int i = 0; i < ctx.getDamageTicks(); i++) {
                    int delay = i * ctx.getTickInterval();

                    ServerTickScheduledExecutorService.schedule(delay, () -> {
                        target.damage(def.getSource(), finalDamage);
                        SilentLibraryUtil.applyKnockbackAndEffects(ctx, target);
                    });
                }
            }
        }
        else {
            throw new InvalidContextException(); // Can be replaced with more robust logging
        }
    }),
    SINGLE_TARGET ((ctx -> {
        if(ctx instanceof DefaultDamageContext def) {
            LivingEntity target;
            if(ctx.getTargets().size() > 1) {
                target = ctx.getTargets().get(ctx.getRandom().nextInt(ctx.getTargets().size() - 1));
            }
            else {
                target = ctx.getTargets().get(0);
            }

            float finalDamage = ctx.getBaseDamage() / ctx.getBaseDamage();

            for (int i = 0; i < ctx.getDamageTicks(); i++) {
                int delay = i * ctx.getTickInterval();

                ServerTickScheduledExecutorService.schedule(delay, () -> {
                    target.damage(def.getSource(), finalDamage);
                    SilentLibraryUtil.applyKnockbackAndEffects(ctx, target);
                });
            }
        }
        else {
            throw new InvalidContextException(); // Can be replaced with more robust logging
        }
    })),
    BOUNCE((ctx) -> {
        if(ctx instanceof BounceDamageContext bounce) {
            if (ctx.getTargets().isEmpty()) return;

            ctx.getTargets().removeIf(LivingEntity::isDead);
            if (ctx.getTargets().size() < 2) {
                AOE.applyWithCustomLogic(bounce.setDamageTicks(bounce.getBounceTicks()));
                return;
            }

            float finalDamage = bounce.getBaseDamage() / bounce.getDamageTicks();

            for (int i = 0; i < bounce.getDamageTicks(); ) {
                // Filter alive targets again just in case
                List<LivingEntity> aliveTargets = ctx.getTargets().stream()
                        .filter(LivingEntity::isAlive)
                        .toList();

                if (aliveTargets.size() < 2) {
                    SilentLibrary.LOGGER.warn("Insufficient valid targets for bounce. Falling back to AoE.");
                    AOE.applyWithCustomLogic(bounce.setDamageTicks(bounce.getDamageTicks() - i).setTickInterval(bounce.getTickInterval()));
                    return;
                }

                LivingEntity target = aliveTargets.get(bounce.getRandom().nextInt(aliveTargets.size() - 1));
                int delay = i * bounce.getTickInterval();

                if (ctx.getWorld() instanceof ServerWorld) {
                    ServerTickScheduledExecutorService.schedule(delay, () -> {
                        target.damage(bounce.getSource(), finalDamage);
                        SilentLibraryUtil.applyKnockbackAndEffects(ctx, target);
                    });
                }
                i++;
            }
        }
        else {
            throw new InvalidContextException(); // Can be replaced with more robust logging
        }
    });

    private final IDamageCalculationType damageCalculationTypeLogic;

    SilentLibraryDamageCalculationTypes(IDamageCalculationType damageCalculationTypeLogic) {
        this.damageCalculationTypeLogic = damageCalculationTypeLogic;
    }

    @Override
    public void applyWithCustomLogic(IDamageContext ctx) {
        damageCalculationTypeLogic.applyWithCustomLogic(ctx);
    }
}
