package net.ornithemc.osl.commands.impl.server;

import com.mojang.authlib.GameProfile;

import net.ornithemc.osl.commands.api.server.MinecraftServerExtension;

public interface MinecraftServerExtensionImpl extends MinecraftServerExtension {

	@Override
	default int getPermissions(GameProfile profile) {
		throw new AbstractMethodError();
	}
}
