package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.BufferedReader;
import java.io.IOException;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.resource.Identifier;
import net.minecraft.world.gen.loot.LootTable;
import net.minecraft.world.gen.loot.LootTables;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.access.LootTablesAccess;

@Conditional(minecraftVersion = @Version(">=15w43a"))
@Mixin(LootTables.class)
public class LootTablesMixin implements LootTablesAccess, ResourceReloadListener {

	private static final String DIRECTORY = "loot_tables";
	private static final String EXTENSION = ".json";

	@Shadow @Final
	private static Gson GSON;

	@Shadow
	private LoadingCache<Identifier, LootTable> lootTables;

	@Unique
	private ResourceManager resourceManager;

	@Shadow
	private void reload() { }

	@Redirect(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/loot/LootTables;reload()V"
		)
	)
	private void osl$resource_loader$cancelReloadOnInit(LootTables lootTables) {
		// we trigger this reload through the resource manager so it can be profiled
	}

	@Override
	public Gson osl$resource_loader$getGson() {
		return GSON;
	}

	@Override
	public ResourceManager osl$resource_loader$getResourceManager() {
		return this.resourceManager;
	}

	@Override
	public void resourcesReloaded(ResourceManager manager) {
		this.resourceManager = manager;
		this.reload();
	}

	@Conditional(minecraftVersion = @Version(">=15w43a"))
	@Mixin(targets = "net/minecraft/world/gen/loot/LootTables$CacheLoader")
	private static class CacheLoaderMixin {

		@Shadow
		private LootTables f_13326223; // enclosing class instance

		@WrapOperation(
			method = "load",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/world/gen/loot/LootTables$CacheLoader;loadFromAssets(Lnet/minecraft/resource/Identifier;)Lnet/minecraft/world/gen/loot/LootTable;"
			)
		)
		private LootTable osl$resource_loader$loadFromResources(@Coerce CacheLoader<Identifier, LootTable> cacheLoader, Identifier identifier, Operation<LootTable> op) {
			if (this.resourceManager() == null) {
				return op.call(cacheLoader, identifier);
			} else {
				return this.loadFromResources(identifier);
			}
		}

		@Unique
		private LootTable loadFromResources(Identifier identifier) {
			NamespacedIdentifier location = identifier
				.prefixed(DIRECTORY + "/")
				.suffixed(EXTENSION);

			return this.resourceManager().getResource(location)
				.map(resource -> {
					try (BufferedReader br = resource.openAsReader()) {
						LootTable lootTable = this.gson().fromJson(br, LootTable.class);

						if (lootTable == null) {
							ResourceLoader.LOGGER.error("Couldn't load custom loot table {} from {} as it's empty or null", identifier, location);
						} else {
							return lootTable;
						}
					} catch (IllegalArgumentException | JsonParseException e) {
						ResourceLoader.LOGGER.error("Parsing error loading custom loot table {}: {}", identifier, e.getMessage());
					} catch (IOException e) {
						ResourceLoader.LOGGER.error("Couldn't read custom loot table {} from {}", identifier, location, e);
					}

					return LootTable.EMPTY;
				})
				.orElse(null);
		}

		@Unique
		private Gson gson() {
			return ((LootTablesAccess) this.f_13326223).osl$resource_loader$getGson();
		}

		@Unique
		private ResourceManager resourceManager() {
			return ((LootTablesAccess) this.f_13326223).osl$resource_loader$getResourceManager();
		}
	}
}
