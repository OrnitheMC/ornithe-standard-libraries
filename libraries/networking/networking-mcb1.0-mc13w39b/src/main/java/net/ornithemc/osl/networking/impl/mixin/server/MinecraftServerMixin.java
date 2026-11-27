package net.ornithemc.osl.networking.impl.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.networking.impl.access.MinecraftServerAccess;

@Conditional(minecraftVersion = @Version("<12w21a"))
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerAccess {

	@Shadow
	private PlayerManager playerManager;

	@Override
	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}
}
