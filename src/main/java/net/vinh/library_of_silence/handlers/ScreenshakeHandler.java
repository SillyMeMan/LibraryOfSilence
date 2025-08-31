package net.vinh.library_of_silence.handlers;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.vinh.library_of_silence.systems.screenshake.ScreenshakeInstance;

import java.util.ArrayList;

public class ScreenshakeHandler {
	private static final PerlinNoiseSampler sampler = new PerlinNoiseSampler(Random.create());
	public static final ArrayList<ScreenshakeInstance> INSTANCES = new ArrayList<>();
	public static float intensity;
	public static float yawOffset;
	public static float pitchOffset;

	public static void init() {
		WorldRenderEvents.START.register(context -> {
			MatrixStack matrices = context.matrixStack();

			if (intensity > 0) {
				yawOffset = randomizeOffset(10);
				pitchOffset = randomizeOffset(-10);

				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawOffset));
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchOffset));
			}
		});
	}

	public static void clientTick(Camera camera, Random random) {
		double sum = Math.min(INSTANCES.stream().mapToDouble(i1 -> i1.updateIntensity(camera, random)).sum(), 2f);

		intensity = (float) Math.pow(sum, 3);
		INSTANCES.removeIf(i -> i.progress >= i.duration);
	}

	public static void addScreenshake(ScreenshakeInstance instance) {
		INSTANCES.add(instance);
	}

	public static float randomizeOffset(int offset) {
		float min = -intensity * 2;
		float max = intensity * 2;
		float sampled = (float) sampler.sample((MinecraftClient.getInstance().world.getTime() % 24000L + MinecraftClient.getInstance().getTickDelta())/intensity, offset, 0) * 1.5f;
		return min >= max ? min : sampled * max;
	}
}
