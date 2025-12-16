package net.ornithemc.osl.lifecycle.impl.server;

import net.minecraft.server.MinecraftServer;

public class MinecraftServerAccess {

	public static MinecraftServer INSTANCE;

	public static MinecraftServer getInstance() {
		if (INSTANCE == null) {
			throw new IllegalStateException("no MinecraftServer instance available right now");
		}

		return INSTANCE;
	}
}
