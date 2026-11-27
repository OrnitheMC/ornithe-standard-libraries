package net.ornithemc.osl.networking.impl.access;

import net.minecraft.server.PlayerManager;

public interface MinecraftServerAccess {

	default PlayerManager getPlayerManager() {
		throw new AbstractMethodError();
	}
}
