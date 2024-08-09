package net.ornithemc.osl.config.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.config.impl.interfaces.mixin.IMinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements IMinecraftServer {

	@Shadow @Final private File gameDirectory;

	@Override
	public File osl$config$getRunDir() {
		return gameDirectory;
	}
}
