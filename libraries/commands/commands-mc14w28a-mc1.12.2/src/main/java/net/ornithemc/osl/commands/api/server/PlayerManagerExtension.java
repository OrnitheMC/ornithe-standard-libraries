package net.ornithemc.osl.commands.api.server;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public interface PlayerManagerExtension {

	boolean allowCommands();

	void updatePermissions(ServerPlayerEntity player);

}
