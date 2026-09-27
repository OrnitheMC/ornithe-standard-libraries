package net.ornithemc.osl.resource.loader.impl.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.BlockRenderDispatcher;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.model.ModelManager;
import net.minecraft.client.resource.pack.UnopenedResourcePack;
import net.minecraft.client.search.SearchRegistry;
import net.minecraft.client.sound.system.SoundManager;
import net.minecraft.client.world.color.FoliageColorReloader;
import net.minecraft.client.world.color.GrassColorReloader;
import net.minecraft.resource.manager.ReloadableResourceManager;
import net.minecraft.resource.manager.ResourceReloadListener;
import net.minecraft.resource.pack.repository.PackRepository;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.resource.loader.api.client.resource.reload.AssetsReloaders;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourcePackRepositoryAdapter;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	private static final boolean FONT_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("1.13-pre7") >= 0;

	@Shadow @Final @Mutable
	private PackRepository<UnopenedResourcePack> packRepository;
	@Shadow
	private ReloadableResourceManager resourceManager;

	@Shadow
	private LanguageManager languageManager;
	@Shadow
	private TextureManager textureManager;
	@Shadow
	private SoundManager soundManager;
	@Conditional(minecraftVersion = @Version("<1.13-pre7"))
	@Shadow
	private TextRenderer textRenderer;
	@Conditional(minecraftVersion = @Version("<1.13-pre7"))
	@Shadow
	private TextRenderer f_77337277;
	@Shadow
	private ModelManager modelManager;
	@Shadow
	private ItemRenderer itemRenderer;
	@Shadow
	private GameRenderer gameRenderer;
	@Shadow
	private BlockRenderDispatcher blockRenderer;
	@Shadow
	private WorldRenderer worldRenderer;
	@Shadow
	private SearchRegistry searchRegistry;

	@Unique
	private SimpleResourcePackRepository actualPackRepository;
	@Unique
	private SimpleReloadableResourceManager actualResourceManager;

	@Conditional(minecraftVersion = @Version(">=1.13-pre7"))
	@Shadow
	private FontManager getFontManager() { return null; }

	@Inject(
		method = "<init>",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;packRepository:Lnet/minecraft/resource/pack/repository/PackRepository;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setPackRepository(CallbackInfo ci) {
		this.actualPackRepository = SimpleResourcePackRepository.client();
		this.packRepository = new ResourcePackRepositoryAdapter<>(this.packRepository.factory, this.actualPackRepository);

		this.actualPackRepository.reset();
		this.actualPackRepository.setCallbacks(null, null);
	}

	@Inject(
		method = "<init>",
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
		method = "<init>",
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

	@Inject(
		method = "init",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;resourceManager:Lnet/minecraft/resource/manager/ReloadableResourceManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setResourceManager(CallbackInfo ci) {
		this.actualResourceManager = SimpleReloadableResourceManager.client();
		this.resourceManager = new ResourceManagerAdapter(this.actualResourceManager, this::identifyReloadListener);

		this.actualResourceManager.reset();
//		this.actualResourceManager.addAll(this.actualPackRepository.openSelectedPacks()); // handled by Vanilla
	}

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "Lnet/minecraft/client/gui/GameGui;" // after all Vanilla reload listeners
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.actualResourceManager.init();
		// Vanilla reloaders are triggered when registered
		// OSL reloaders are queued and reloaded at once
		this.actualResourceManager.partialReload();
	}

	private NamespacedIdentifier identifyReloadListener(ResourceReloadListener listener) {
		if (listener == this.languageManager) {
			return AssetsReloaders.LANGUAGE_MANAGER;
		} else if (listener == this.textureManager) {
			return AssetsReloaders.TEXTURE_MANAGER;
		} else if (listener == this.soundManager) {
			return AssetsReloaders.SOUND_MANAGER;
		} else if (!FONT_MANAGER_EXISTS && listener == this.textRenderer) {
			return AssetsReloaders.FONT_MANAGER;
		} else if (!FONT_MANAGER_EXISTS && listener == this.f_77337277) {
			return AssetsReloaders.ENCHANTING_PHRASE_FONT_MANAGER;
		} else if (FONT_MANAGER_EXISTS && listener == this.getFontManager()) {
			return AssetsReloaders.FONT_MANAGER;
		} else if (listener instanceof GrassColorReloader) {
			return AssetsReloaders.GRASS_COLORS;
		} else if (listener instanceof FoliageColorReloader) {
			return AssetsReloaders.FOLIAGE_COLORS;
		} else if (listener == this.modelManager) {
			return AssetsReloaders.MODEL_MANAGER;
		} else if (listener == this.itemRenderer) {
			return AssetsReloaders.ITEM_RENDERER;
		} else if (listener == this.gameRenderer) {
			return AssetsReloaders.GAME_RENDERER;
		} else if (listener == this.blockRenderer) {
			return AssetsReloaders.BLOCK_RENDERER;
		} else if (listener == this.worldRenderer) {
			return AssetsReloaders.WORLD_RENDERER;
		} else if (listener == this.searchRegistry) {
			return AssetsReloaders.SEARCH_REGISTRY;
		} else {
			ResourceLoader.LOGGER.debug("Unknown resource reload listener {}/{}", listener.getClass().getSimpleName(), listener);
		}

		return null;
	}
}
