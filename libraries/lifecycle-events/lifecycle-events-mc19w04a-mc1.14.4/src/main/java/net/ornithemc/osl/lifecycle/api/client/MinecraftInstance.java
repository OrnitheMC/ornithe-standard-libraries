package net.ornithemc.osl.lifecycle.api.client;

import net.minecraft.client.Minecraft;

import net.ornithemc.osl.lifecycle.impl.client.MinecraftAccess;

/**
 * A wrapper class for getting the {@linkplain Minecraft} instance.
 */
public class MinecraftInstance {

	/**
	 * Retrieves the current Minecraft game instance,
	 * or throws an exception.
	 * 
	 * @return the current Minecraft game instance
	 */
	public static Minecraft get() {
		return MinecraftAccess.getInstance();
	}
}
