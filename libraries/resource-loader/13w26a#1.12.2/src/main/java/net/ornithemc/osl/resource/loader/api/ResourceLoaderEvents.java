package net.ornithemc.osl.resource.loader.api;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.events.api.Event;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.mixin.MinecraftAccessor;

public class ResourceLoaderEvents {

	public static final Event<Consumer<Consumer<ModResourcePack>>> ADD_DEFAULT_RESOURCE_PACKS = Event.simple(listener -> {
		listener.accept(pack -> {
			if (ResourceLoader.addDefaultModResourcePack(pack)) {
				((MinecraftAccessor)Minecraft.getInstance()).osl$resource_loader$defaultResourcePacks().add(pack);
			}
		});
	});
	public static final Event<Runnable>                            START_RESOURCE_RELOAD      = Event.simple(Runnable::run);
	public static final Event<Runnable>                            END_RESOURCE_RELOAD        = Event.simple(Runnable::run);

}
