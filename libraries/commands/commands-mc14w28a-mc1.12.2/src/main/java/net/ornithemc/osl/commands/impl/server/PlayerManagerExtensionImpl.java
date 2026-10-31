package net.ornithemc.osl.commands.impl.server;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.osl.commands.api.server.PlayerManagerExtension;

public interface PlayerManagerExtensionImpl extends PlayerManagerExtension {

	@Override
	default boolean allowCommands() {
		throw new AbstractMethodError();
	}

	@Override
	default void updatePermissions(ServerPlayerEntity player) {
		throw new AbstractMethodError();
	}
}
