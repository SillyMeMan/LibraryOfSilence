package net.vinh.library_of_silence.systems;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public interface IDamageContext {
    World getWorld();
    DamageSource getSource();
    List<LivingEntity> getTargets();
    float getBaseDamage();
    int getTickInterval();
    int getDamageTicks();

    Random getRandom();

    Vec3d getKnockback();

    List<StatusEffectInstance> getStatusEffects();

    IDamageContext setTargets(List<LivingEntity> targets);
    IDamageContext setDamageSource(DamageSource source);

    IDamageContext setKnockback(Vec3d knockback);

    IDamageContext addStatusEffects(List<StatusEffectInstance> statusEffects);

    IDamageContext setBaseDamage(float baseDamage);
    IDamageContext setTickInterval(int tickInterval);
    IDamageContext setDamageTicks(int damageTicks);
}
