package net.ornithemc.osl.networking.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockableEventLoop;

import net.ornithemc.osl.networking.impl.access.TaskRunnerAccess;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends BlockableEventLoop implements TaskRunnerAccess {

	private MinecraftMixin(String name) {
		super(name);
	}

	@Override
	public boolean osl$networking$submit(Runnable task) {
		this.execute(task);
		return true;
	}
}
