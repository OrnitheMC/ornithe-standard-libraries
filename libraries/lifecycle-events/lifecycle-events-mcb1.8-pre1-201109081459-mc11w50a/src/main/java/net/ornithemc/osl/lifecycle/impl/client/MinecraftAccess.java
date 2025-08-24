package net.ornithemc.osl.lifecycle.impl.client;

import net.minecraft.client.Minecraft;

public class MinecraftAccess {

	public static Minecraft INSTANCE;

	public static Minecraft getInstance() {
		if (INSTANCE == null) {
			throw new IllegalStateException("no Minecraft instance available right now");
		}

		return INSTANCE;
	}
}
