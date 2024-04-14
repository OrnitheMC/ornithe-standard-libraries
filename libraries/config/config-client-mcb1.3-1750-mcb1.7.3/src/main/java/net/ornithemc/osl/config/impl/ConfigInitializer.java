package net.ornithemc.osl.config.impl;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.WorldStorage;
import net.minecraft.world.storage.WorldStorageSource;

import net.ornithemc.osl.config.api.ConfigScope;
import net.ornithemc.osl.config.api.LoadingPhase;
import net.ornithemc.osl.config.impl.interfaces.mixin.IWorldStorage;
import net.ornithemc.osl.core.api.events.Event;
import net.ornithemc.osl.entrypoints.api.ModInitializer;
import net.ornithemc.osl.lifecycle.api.MinecraftEvents;

public class ConfigInitializer implements ModInitializer {

	public static final Event<BiConsumer<Minecraft, String>> START_GAME = Event.biConsumer();
	// keep this here until Lifecycle Events has this...
	public static final Event<Consumer<Minecraft>> CLOSE_WORLD = Event.consumer();

	@Override
	public void init() {
		START_GAME.register((minecraft, saveName) -> {
			WorldStorageSource storageSource = minecraft.getWorldStorageSource();
			WorldStorage storage = storageSource.get(saveName, false);
			File worldDir = ((IWorldStorage)storage).osl$config$getDir();

			ConfigManagerImpl.setUp(ConfigScope.WORLD, worldDir.toPath());
			ConfigManagerImpl.load(ConfigScope.WORLD, LoadingPhase.START);
		});
		MinecraftEvents.READY_WORLD.register(minecraft -> {
			ConfigManagerImpl.load(ConfigScope.WORLD, LoadingPhase.READY);
		});
		CLOSE_WORLD.register(minecraft -> {
			ConfigManagerImpl.unload(ConfigScope.WORLD);
			ConfigManagerImpl.destroy(ConfigScope.WORLD);
		});
	}
}
