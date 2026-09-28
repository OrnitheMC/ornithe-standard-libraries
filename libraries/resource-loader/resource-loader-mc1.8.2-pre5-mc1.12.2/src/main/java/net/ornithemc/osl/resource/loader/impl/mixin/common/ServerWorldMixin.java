package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.AdvancementManager;
import net.minecraft.server.FunctionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.loot.LootTables;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Shadow
	private MinecraftServer server;

	@Conditional(minecraftVersion = @Version(">=15w43a"))
	@Redirect(
		method = "load",
		at = @At(
			value = "NEW",
			target = "net/minecraft/world/gen/loot/LootTables"
		)
	)
	private LootTables osl$resource_loader$replaceLootTableManager(File file) {
		return this.server.getLootTables();
	}

	@Conditional(minecraftVersion = @Version(">=1.12-pre1"))
	@Redirect(
		method = "load",
		at = @At(
			value = "NEW",
			target = "net/minecraft/server/FunctionManager"
		)
	)
	private FunctionManager osl$resource_loader$replaceFunctionManager(File file, MinecraftServer server) {
		return this.server.getFunctions();
	}

	@Conditional(minecraftVersion = @Version(">=17w13a"))
	@Redirect(
		method = "load",
		at = @At(
			value = "NEW",
			target = "net/minecraft/server/AdvancementManager"
		)
	)
	private AdvancementManager osl$resource_loader$replaceAdvancementManager(File file) {
		return this.server.getAdvancements();
	}
}
