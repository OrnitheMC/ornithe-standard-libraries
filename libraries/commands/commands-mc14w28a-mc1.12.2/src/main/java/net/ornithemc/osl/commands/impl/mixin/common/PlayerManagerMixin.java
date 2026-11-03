package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.OpEntry;
import net.minecraft.server.Ops;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.commands.api.server.PlayerManagerExtension;
import net.ornithemc.osl.commands.impl.server.ServerCommandsImpl;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin implements PlayerManagerExtension {

	@Shadow
	private MinecraftServer server;
	@Shadow
	private Ops ops;
	@Shadow
	private boolean allowCommands;

	@Conditional(minecraftVersion = @Version("<15w31a"))
	@Shadow
	private boolean isOp(GameProfile profile) { return false; }

	@Conditional(minecraftVersion = @Version(">=15w31a"))
	@Inject(
		method = "updatePermissions(Lnet/minecraft/server/entity/living/player/ServerPlayerEntity;I)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$commands$sendCommands(ServerPlayerEntity player, int permissions, CallbackInfo ci) {
		ServerCommandsImpl.sendCommands(player);
	}

	@Conditional(minecraftVersion = @Version("<15w31a"))
	@Inject(
		method = "onLogin",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/stat/ServerPlayerStats;markAllDirty()V"
		)
	)
	private void osl$commands$updatePermissions(CallbackInfo ci, @Local ServerPlayerEntity player) {
		this.updatePermissions(player);
	}

	@Conditional(minecraftVersion = @Version("<15w31a"))
	@Inject(
		method = "changeDimension(Lnet/minecraft/server/entity/living/player/ServerPlayerEntity;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/world/ServerWorld;removeEntityNow(Lnet/minecraft/entity/Entity;)V"
		)
	)
	private void osl$commands$updatePermissions(ServerPlayerEntity player, int dimension, CallbackInfo ci) {
		this.updatePermissions(player);
	}

	@Conditional(minecraftVersion = @Version("<15w31a"))
	@Inject(
		method = "respawn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/ChunkMap;addPlayer(Lnet/minecraft/server/entity/living/player/ServerPlayerEntity;)V"
		)
	)
	private void osl$commands$updatePermissions(ServerPlayerEntity player, int dimension, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
		this.updatePermissions(player);
	}

	@Override
	public boolean allowCommands() {
		return this.allowCommands;
	}

	@Conditional(minecraftVersion = @Version("<15w31a"))
	@Override
	public void updatePermissions(ServerPlayerEntity player) {
		GameProfile profile = player.getGameProfile();
		OpEntry op = this.ops.get(profile);
		int permissions = this.isOp(profile) && op != null ? op.getPermissionLevel() : 0;
		permissions = this.server.isSingleplayer() && this.server.worlds[0].getData().allowCommands() ? 4 : permissions;
		permissions = this.allowCommands ? 4 : permissions;

		this.updatePermissions(player, permissions);
	}

	@Conditional(minecraftVersion = @Version("<15w31a"))
	@Unique
	private void updatePermissions(ServerPlayerEntity player, int permissions) {
		ServerCommandsImpl.sendCommands(player);
	}
}
