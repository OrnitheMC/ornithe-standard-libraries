package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.BlockRenderDispatcher;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.manager.ReloadableResourceManager;
import net.minecraft.client.resource.manager.ResourceReloadListener;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.model.ModelManager;
import net.minecraft.client.resource.pack.BuiltInResourcePack;
import net.minecraft.client.resource.pack.ResourcePack;
import net.minecraft.client.sound.system.SoundEngine;
import net.minecraft.client.sound.system.SoundManager;
import net.minecraft.client.world.color.FoliageColorReloader;
import net.minecraft.client.world.color.GrassColorReloader;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.crashreports.api.crash.CrashReportDetail;
import net.ornithemc.osl.crashreports.impl.crash.ModifiableCrashReportSection;
import net.ornithemc.osl.resource.loader.api.client.resource.reload.AssetsReloaders;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourcePackLists;
import net.ornithemc.osl.resource.loader.impl.resource.manager.SimpleReloadableResourceManager;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	private static final boolean MULTIPLE_RESOURCE_PACKS_SUPPORTED = MinecraftVersion.resolve().compareTo("13w36a") >= 0;
	private static final boolean SOUND_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("13w42a") >= 0;
	private static final boolean TEXT_RENDERERS_ARE_RELOAD_LISTENERS = MinecraftVersion.resolve().compareTo("1.6.2") >= 0;
	private static final boolean MODEL_MANAGER_EXISTS = MinecraftVersion.resolve().compareTo("14w10a") >= 0;
	private static final boolean ITEM_RENDERER_IS_RELOAD_LISTENER = MinecraftVersion.resolve().compareTo("14w25a") >= 0;
	private static final boolean GAME_RENDERER_IS_RELOAD_LISTENER = MinecraftVersion.resolve().compareTo("13w38a") >= 0;
	private static final boolean BLOCK_RENDERER_IS_RELOAD_LISTENER = MinecraftVersion.resolve().compareTo("14w10c") >= 0;
	private static final boolean WORLD_RENDERER_IS_RELOAD_LISTENER = MinecraftVersion.resolve().compareTo("14w25b") >= 0;

	@Shadow @Final
	private ResourceMetadataSerializerRegistry resourceMetadataSerializerRegistry;
	@Shadow @Final
	private List<ResourcePack> defaultResourcePacks;
	@Shadow @Final
	private BuiltInResourcePack defaultResourcePack;
	@Shadow
	private ReloadableResourceManager resourceManager;

	@Shadow
	private LanguageManager languageManager;
	@Shadow
	private TextureManager textureManager;
	@Conditional(minecraftVersion = @Version("<13w42a"))
	@Shadow
	private SoundEngine f_52467463;
	@Conditional(minecraftVersion = @Version(">=13w42a"))
	@Shadow
	private SoundManager soundManager;
	@Conditional(minecraftVersion = @Version(">=1.6.2"))
	@Shadow
	private TextRenderer textRenderer;
	@Conditional(minecraftVersion = @Version(">=1.6.2"))
	@Shadow
	private TextRenderer enchantingPhraseRenderer;
	@Conditional(minecraftVersion = @Version(">=14w10a"))
	@Shadow
	private ModelManager modelManager;
	@Conditional(minecraftVersion = @Version(">=14w25a"))
	@Shadow
	private ItemRenderer itemRenderer;
	@Conditional(minecraftVersion = @Version(">=13w38a"))
	@Shadow
	private GameRenderer gameRenderer;
	@Conditional(minecraftVersion = @Version(">=14w10c"))
	@Shadow
	private BlockRenderDispatcher blockRenderer;
	@Conditional(minecraftVersion = @Version(">=14w25b"))
	@Shadow
	private WorldRenderer worldRenderer;

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private SimpleReloadableResourceManager actualResourceManager;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$modifyCrashReports(CallbackInfo ci) {
		Minecraft minecraft = (Minecraft) (Object) this;

		minecraft.addCrashListener((report, section) -> {
			ModifiableCrashReportSection modifiableSection = (ModifiableCrashReportSection) section;
			CrashReportDetail<String> detail = () -> {
				return this.packRepository.getSelectedPacks().stream()
						.map(summary -> {
							String s = summary.getId();
							if (!summary.getCompatibility().isCompatible()) {
								s += " (incompatible)";
							}

							return s;
						})
						.collect(Collectors.joining(", "));
			};

			if (MULTIPLE_RESOURCE_PACKS_SUPPORTED) {
				modifiableSection.replaceDetail("Resource Packs", detail);
			} else {
				modifiableSection.replaceDetail("Resource Pack", "Resource Packs", detail);
			}
		});
	}

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "net/minecraft/client/resource/pack/ResourcePacks"
		)
	)
	private void osl$resource_loader$initPackRepository(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();
	}

	@Inject(
		method = "init",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/Minecraft;resourceManager:Lnet/minecraft/client/resource/manager/ReloadableResourceManager;",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$setResourceManager(CallbackInfo ci) {
		this.actualResourceManager = SimpleReloadableResourceManager.client();
		this.resourceManager = new ResourceManagerAdapter(this.resourceMetadataSerializerRegistry, this.actualResourceManager, this::identifyReloadListener);

		this.actualResourceManager.reset();
//		this.actualResourceManager.addAll(this.packRepository.openSelectedPacks()); // handled by Vanilla
	}

	@Inject(
		method = "init",
		at = @At(
			value = "NEW",
			target = "net/minecraft/client/gui/GameGui" // after all Vanilla reload listeners
		)
	)
	private void osl$resource_loader$initResourceManager(CallbackInfo ci) {
		this.actualResourceManager.init();
		// Vanilla reloaders are triggered when registered
		// OSL reloaders are queued and reloaded at once
		this.actualResourceManager.partialReload();
	}

	@Inject(
		method = "reloadResources",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/manager/ReloadableResourceManager;reload(Ljava/util/List;)V"
		)
	)
	private void osl$resource_loader$fixSelection(CallbackInfo ci, @Local ArrayList<ResourcePack> selection) {
		// ResourcePacks.getSelectedPacks only contains directory or zip packs
		// The default pack and server pack are added in the target method, so
		// we must add any other required packs manually as well

		ResourcePackLists.fixSelection(selection, false);
	}

	private NamespacedIdentifier identifyReloadListener(ResourceReloadListener listener) {
		if (listener == this.languageManager) {
			return AssetsReloaders.LANGUAGE_MANAGER;
		} else if (listener == this.textureManager) {
			return AssetsReloaders.TEXTURE_MANAGER;
		} else if (!SOUND_MANAGER_EXISTS && listener == this.f_52467463) {
			return AssetsReloaders.SOUND_MANAGER;
		} else if (SOUND_MANAGER_EXISTS && listener == this.soundManager) {
			return AssetsReloaders.SOUND_MANAGER;
		} else if (TEXT_RENDERERS_ARE_RELOAD_LISTENERS && listener == this.textRenderer) {
			return AssetsReloaders.FONT_MANAGER;
		} else if (TEXT_RENDERERS_ARE_RELOAD_LISTENERS && listener == this.enchantingPhraseRenderer) {
			return AssetsReloaders.ENCHANTING_PHRASE_FONT_MANAGER;
		} else if (listener instanceof GrassColorReloader) {
			return AssetsReloaders.GRASS_COLORS;
		} else if (listener instanceof FoliageColorReloader) {
			return AssetsReloaders.FOLIAGE_COLORS;
		} else if (MODEL_MANAGER_EXISTS && listener == this.modelManager) {
			return AssetsReloaders.MODEL_MANAGER;
		} else if (ITEM_RENDERER_IS_RELOAD_LISTENER && listener == this.itemRenderer) {
			return AssetsReloaders.ITEM_RENDERER;
		} else if (GAME_RENDERER_IS_RELOAD_LISTENER && listener == this.gameRenderer) {
			return AssetsReloaders.GAME_RENDERER;
		} else if (BLOCK_RENDERER_IS_RELOAD_LISTENER && listener == this.blockRenderer) {
			return AssetsReloaders.BLOCK_RENDERER;
		} else if (WORLD_RENDERER_IS_RELOAD_LISTENER && listener == this.worldRenderer) {
			return AssetsReloaders.WORLD_RENDERER;
		} else {
			ResourceLoader.LOGGER.debug("Unknown resource reload listener {}/{}", listener.getClass().getSimpleName(), listener);
		}

		return null;
	}
}
