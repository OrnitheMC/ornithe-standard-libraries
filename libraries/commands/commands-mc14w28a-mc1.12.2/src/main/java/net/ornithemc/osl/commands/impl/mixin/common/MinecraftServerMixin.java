package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.OpEntry;
import net.minecraft.server.PlayerManager;

import net.ornithemc.osl.commands.api.server.MinecraftServerExtension;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerExtension {

	@Shadow
	private boolean isSingleplayer() { return false; }
	@Shadow
	private PlayerManager getPlayerManager() { return null; }
	@Shadow
	private String getUsername() { return null; }
	@Shadow
	private int getOpPermissionLevel() { return 0; }

	@Override
	public int getPermissions(GameProfile profile) {
		if (this.getPlayerManager().isOp(profile)) {
			OpEntry op = this.getPlayerManager().getOps().get(profile);

			if (op != null) {
				return op.getPermissionLevel();
			} else if (this.isSingleplayer()) {
				if (this.getUsername().equalsIgnoreCase(profile.getName()) || this.getPlayerManager().allowCommands()) {
					return 4;
				}
			} else {
				return this.getOpPermissionLevel();
			}
		}

		return 0;
	}
}
