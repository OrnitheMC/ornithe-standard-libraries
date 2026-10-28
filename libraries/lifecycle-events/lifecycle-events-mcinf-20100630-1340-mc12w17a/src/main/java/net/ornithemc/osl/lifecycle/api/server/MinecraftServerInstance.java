package net.ornithemc.osl.lifecycle.api.server;

import net.minecraft.server.MinecraftServer;

import net.ornithemc.osl.lifecycle.impl.server.MinecraftServerAccess;

/**
 * A wrapper class for getting the {@linkplain MinecraftServer} instance.
 */
public class MinecraftServerInstance {

	/**
	 * Retrieves the current Minecraft server instance,
	 * or throws an exception.
	 * 
	 * @return the current Minecraft server instance
	 */
	public static MinecraftServer get() {
		return MinecraftServerAccess.getInstance();
	}
}
