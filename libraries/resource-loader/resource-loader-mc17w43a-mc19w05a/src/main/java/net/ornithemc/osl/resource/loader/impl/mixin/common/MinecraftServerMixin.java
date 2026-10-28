package net.ornithemc.osl.resource.loader.impl.mixin.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.crafting.CraftingManager;
import net.minecraft.resource.manager.ReloadableResourceManager;
import net.minecraft.resource.manager.ResourceReloadListener;
import net.minecraft.resource.pack.repository.PackRepository;
import net.minecraft.resource.pack.repository.UnopenedPack;
import net.minecraft.server.AdvancementManager;
import net.minecraft.server.FunctionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tag.TagManager;
import net.minecraft.world.gen.loot.LootTables;
import net.minecraft.world.gen.structure.template.StructureManager;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.resource.loader.api.server.resource.reload.DataReloaders;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourcePackRepositoryAdapter;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	private static final boolean TAG_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("17w49a") >= 0;
	private static final boolean CRAFTING_MANAGER_IS_RELOAD_LISTENER = MinecraftVersion.resolve().compareTo("17w48a") >= 0;

	@Shadow @Final @Mutable
	private PackRepository<UnopenedPack> packRepository;
	@Shadow @Final @Mutable
	private ReloadableResourceManager resourceManager;

	@Shadow
	private TagManager tags;
	@Shadow
	private CraftingManager craftingManager;
	@Shadow
	private LootTables lootTables;
	@Shadow
	private FunctionManager functions;
	@Shadow
	private AdvancementManager advancements;

	@Unique
	private SimpleResourcePackRepository actualPackRepository;
	@Unique
	private SimpleReloadableResourceManager actualResourceManager;

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;packRepository:Lnet/minecraft/resource/pack/repository/PackRepository;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setPackRepository(CallbackInfo ci) {
		this.actualPackRepository = SimpleResourcePackRepository.server();
		this.packRepository = new ResourcePackRepositoryAdapter<>(this.packRepository.factory, this.actualPackRepository);

		this.actualPackRepository.reset();
		this.actualPackRepository.setCallbacks(null, null);
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;resourceManager:Lnet/minecraft/resource/manager/ReloadableResourceManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setResourceManager(CallbackInfo ci) {
		this.actualResourceManager = SimpleReloadableResourceManager.server();
		this.resourceManager = new ResourceManagerAdapter(this.actualResourceManager, this::identifyReloadListener);

		this.actualResourceManager.reset();
//		this.actualResourceManager.addAll(this.actualPackRepository.openSelectedPacks()); // handled by Vanilla
	}

	@Inject(
		method = "loadDataPacks",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.actualResourceManager.init();
//		this.actualResourceManager.partialReload(); // unnecessary since full reload follows
	}

	@Inject(
		method = "loadDataPacks",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/pack/repository/PackRepository;addSource(Lnet/minecraft/resource/pack/repository/RepositorySource;)V",
			ordinal = 0,
			shift = Shift.AFTER // after built-in Vanilla resources
		)
	)
	private void osl$resource_loader$registerModResources(CallbackInfo ci) {
		this.actualPackRepository.addSource(new BundledModResourcePacks());
	}

	@Inject(
		method = "loadDataPacks",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/resource/pack/repository/PackRepository;addSource(Lnet/minecraft/resource/pack/repository/RepositorySource;)V",
			ordinal = 1,
			shift = Shift.AFTER // after directory pack source
		)
	)
	private void osl$resource_loader$initResourcePackRepository(CallbackInfo ci) {
		this.actualPackRepository.init();
//		this.actualPackRepository.reload(); // handled by Vanilla
	}

	private NamespacedIdentifier identifyReloadListener(ResourceReloadListener listener) {
		if (TAG_MANAGER_EXISTS && listener == this.tags) {
			return DataReloaders.TAG_MANAGER;
		} else if (CRAFTING_MANAGER_IS_RELOAD_LISTENER && listener == this.craftingManager) {
			return DataReloaders.CRAFTING_MANAGER;
		} else if (listener == this.lootTables) {
			return DataReloaders.LOOT_TABLE_MANAGER;
		} else if (listener == this.functions) {
			return DataReloaders.FUNCTION_MANAGER;
		} else if (listener == this.advancements) {
			return DataReloaders.ADVANCEMENT_MANAGER;
		} else if (listener instanceof StructureManager) {
			return DataReloaders.STRUCTURE_MANAGER;
		} else {
			ResourceLoader.LOGGER.debug("Unknown resource reload listener {}/{}", listener.getClass().getSimpleName(), listener);
		}

		return null;
	}
}
