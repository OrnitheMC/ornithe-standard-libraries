package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.AdvancementManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldData;
import net.minecraft.world.gen.loot.LootTables;
import net.minecraft.world.storage.WorldStorage;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.resource.loader.impl.mixin.common.MinecraftServerMixin;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin extends MinecraftServerMixin {

	@Inject(
		method = "loadWorld",
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/storage/WorldStorage;loadData()Lnet/minecraft/world/WorldData;"
			)
		),
		at = @At(
			// inject before the for-loop for ServerWorld instancing
			value = "FIELD",
			target = "Lnet/minecraft/server/integrated/IntegratedServer;worlds:[Lnet/minecraft/server/world/ServerWorld;",
			ordinal = 0,
			shift = Shift.BY,
			// it's extreme but this ensures that the injector is only invoked once
			by = -5
		)
	)
	private void osl$resource_loader$loadDataPacks(CallbackInfo ci, @Local WorldStorage storage, @Local WorldData data) {
		this.onLoadWorldBeforeCreateServerWorlds(storage, data);
	}

	@Conditional(minecraftVersion = @Version(">=15w43a <17w18a"))
	@Redirect(
		method = "m_90502033",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/loot/LootTables;reload()V"
		)
	)
	private void osl$resource_loader$cancelLootTablesReload(LootTables lootTables) {
		// the loot table manager is added as a resource reload listener
	}

	@Conditional(minecraftVersion = @Version(">=17w15a <17w18a"))
	@Redirect(
		method = "m_90502033",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/AdvancementManager;reload()V"
		)
	)
	private void osl$resource_loader$cancelAdvancementsReload(AdvancementManager advancements) {
		// the advancement manager is added as a resource reload listener
	}

	@Conditional(minecraftVersion = @Version(">=15w43a <17w15a"))
	@Inject(
		method = "m_90502033",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/loot/LootTables;reload()V",
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$reloadResourcesPreAdvancements(CallbackInfo ci) {
		this.reloadDataPacks(this.worlds[0].getData());
	}

	@Conditional(minecraftVersion = @Version(">=17w15a <17w18a"))
	@Inject(
		method = "m_90502033",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/PlayerManager;reloadResources()V"
		)
	)
	private void osl$resource_loader$reloadResourcesPostAdvancements(CallbackInfo ci) {
		this.reloadDataPacks(this.worlds[0].getData());
	}
}
