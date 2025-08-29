package net.vinh.library_of_silence.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.*;
import net.vinh.library_of_silence.systems.IDamageCalculationType;
import net.vinh.library_of_silence.systems.IDamageContext;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class SilentLibraryUtil {
    private static final RandomGenerator random = RandomGenerator.getDefault();

    public static int getInt(int origin, int bound) {
        return random.nextInt(origin, bound);
    }

    public static List<Integer> getIntList(int amount, int origin, int bound) {
        List<Integer> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(getInt(origin, bound));
        }

        return list;
    }

    public static double getDouble(double origin, double bound) {
        return random.nextDouble(origin, bound);
    }

    public static List<Double> getDoubleList(int amount, double origin, double bound) {
        List<Double> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(getDouble(origin, bound));
        }

        return list;
    }

    public static float getFloat(float origin, float bound) {
        return random.nextFloat(origin, bound);
    }

    public static List<Float> getFloatList(int amount, float origin, float bound) {
        List<Float> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(getFloat(origin, bound));
        }

        return list;
    }

    public static long getLong(long origin, long bound) {
        return random.nextLong(origin, bound);
    }

    public static List<Long> getLongList(int amount, long origin, long bound) {
        List<Long> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(getLong(origin, bound));
        }

        return list;
    }

    public static List<Vec3d> getVec3dListFromBox(int amount, Box box) {
        List<Vec3d> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(new Vec3d(getDouble(box.minX, box.maxX), getDouble(box.minY, box.maxY), getDouble(box.minZ, box.maxZ)));
        }

        return list;
    }

    public static List<BlockPos> getBlockPosListFromBox(int amount, Box box) {
        List<BlockPos> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(new BlockPos((int) getDouble(box.minX, box.maxX), (int) getDouble(box.minY, box.maxY), (int) getDouble(box.minZ, box.maxZ)));
        }

        return list;
    }

    //Honestly, IDK why u would ever use this... but who cares?
    public static List<Position> getPositionListFromBox(int amount, Box box) {
        List<Position> list = new ArrayList<>();

        for (int i = amount; i > 0; i--) {
            list.add(new Position() {
                @Override
                public double getX() {
                    return getDouble(box.minX, box.maxX);
                }

                @Override
                public double getY() {
                    return getDouble(box.minY, box.maxY);
                }

                @Override
                public double getZ() {
                    return getDouble(box.minZ, box.maxZ);
                }
            });
        }

        return list;
    }

    public static void damageWithCustomLogic(IDamageCalculationType damageCalculationType, IDamageContext context) {
        damageCalculationType.applyWithCustomLogic(context);
    }

    public static void applyKnockbackAndEffects(IDamageContext ctx, LivingEntity target) {
        if (!ctx.getKnockback().equals(Vec3d.ZERO)) {
            target.addVelocity(ctx.getKnockback().x, ctx.getKnockback().y, ctx.getKnockback().z);
            target.velocityModified = true;
        }

        for (StatusEffectInstance effect : ctx.getStatusEffects()) {
            target.addStatusEffect(effect);
        }
    }
}
