package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.DirectoryTexturePack;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.TexturePacks;
import net.minecraft.client.resource.pack.ZippedTexturePack;

import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ClientPackSource;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ClientResourcePacks;
import net.ornithemc.osl.resource.loader.impl.access.TexturePacksAccess;
import net.ornithemc.osl.resource.loader.impl.adapter.ResourceManagerAdapter;
import net.ornithemc.osl.resource.loader.impl.adapter.WrappedTexturePack;
import net.ornithemc.osl.resource.loader.impl.resource.repository.BundledModResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.repository.DirectoryPackSource;
import net.ornithemc.osl.resource.loader.impl.resource.repository.SimpleResourcePackRepository;

@Mixin(TexturePacks.class)
public class TexturePacksMixin implements TexturePacksAccess, ResourcePackRepository.Source {

	@Shadow @Final
	private static TexturePack DEFAULT_PACK;

	@Shadow
	private Minecraft minecraft;
	@Shadow
	private List<TexturePack> availablePacks;
	@Shadow
	private Map<String, TexturePack> availablePacksByKey;
	@Shadow
	private TexturePack selected;
	@Shadow
	private boolean hasServerTextures;

	@Unique
	private SimpleResourcePackRepository packRepository;
	@Unique
	private TexturePack resourceManager;
	@Unique
	private Map<String, TexturePack> availablePacksById;

	@Unique
	private TexturePack actuallySelected;
	@Unique
	private boolean vanillaReloading;
	@Unique
	private boolean reloading;

	@Shadow
	private void reload() { }

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resource/pack/TexturePacks;reload()V"
		)
	)
	private void osl$resource_loader$initResourcePackRepository(CallbackInfo ci) {
		this.packRepository = SimpleResourcePackRepository.client();
		// wrap the ResourceManager into a texture pack, and set this as selected
		// this way we don't have to redirect every call to TexturePack::getResource!
		this.resourceManager = new ResourceManagerAdapter(ResourceManager.client());
		this.availablePacksById = new HashMap<>();

		this.packRepository.reset();
		this.packRepository.setCallbacks(null, this::selectionChanged);

		this.packRepository.addSource(new ClientResourcePacks((TexturePacks) (Object) this));
		this.packRepository.addSource(new BundledModResourcePacks());
		this.packRepository.addSource(this); // texturepacks/ directory source

		this.packRepository.init();
	}

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$initSelected(CallbackInfo ci) {
		TexturePack selected = DEFAULT_PACK;

		for (TexturePack pack : this.availablePacks) {
			if (pack.getName().equals(this.minecraft.options.skin)) {
				selected = pack;
			}
		}

		this.selectPack(selected);
	}

	@Inject(
		method = "select",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$select(TexturePack pack, CallbackInfoReturnable<Boolean> cir) {
		if (pack == this.actuallySelected) {
			cir.setReturnValue(false);
		}
	}

	@Inject(
		method = "select",
		at = @At(
			value = "RETURN",
			ordinal = 1 // only on texture pack change
		)
	)
	private void osl$resource_loader$selected(TexturePack pack, CallbackInfoReturnable<Boolean> cir) {
		this.selectPack(this.selected);
	}

	@Inject(
		method = "reload",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$resource_loader$startReload(CallbackInfo ci) {
		if (!this.vanillaReloading) {
			this.vanillaReloading = true;

			if (!this.reloading) {
				this.packRepository.reload();

				// cancel this call as the pack repository will trigger
				// another recursive call through this.loadResourcePacks
				ci.cancel();
			}
		}
	}

	@Inject(
		method = "reload",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$endReload(CallbackInfo ci) {
		this.vanillaReloading = false;

		if (this.actuallySelected != this.resourceManager) {
			this.actuallySelected = this.selected;
			this.selected = this.resourceManager;
		}
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		this.reloading = true;
		this.reload();
		this.reloading = false;

		this.availablePacksById.clear();

		for (TexturePack pack : this.availablePacks) {
			if (pack == DEFAULT_PACK) {
				continue;
			}

			ResourcePack resourcePack = new WrappedTexturePack(pack);
			ResourcePackSummary summary = ResourcePackSummary.create(
				resourcePack,
				this.packId(pack),
				false,
				false,
				PackPosition.TOP
			);

			this.availablePacksById.put(summary.getId(), pack);

			if (summary != null) {
				consumer.accept(summary);
			}
		}
	}

	@Override
	public TexturePack osl$resource_loader$getDefaultPack() {
		return DEFAULT_PACK;
	}

	@Override
	public TexturePack osl$resource_loader$getServerPack() {
		return this.hasServerPack() ? this.actuallySelected : null;
	}

	@Override
	public TexturePack osl$resource_loader$getActuallySelected() {
		return this.actuallySelected;
	}

	@Unique
	private boolean hasServerPack() {
		return this.hasServerTextures;
	}

	@Unique // TODO: call from the server pack download callback
	private void selectPack(TexturePack pack) {
		this.actuallySelected = pack;
		this.selected = this.resourceManager;

		if (pack == null || pack == DEFAULT_PACK || this.hasServerPack()) {
			this.packRepository.setSelectedPacks(Collections.emptyList());
		} else {
			this.packRepository.setSelectedPacks(Collections.singletonList(this.packId(pack)));
		}
	}

	@Unique
	private void selectionChanged() {
		this.actuallySelected = DEFAULT_PACK;

		for (ResourcePackSummary summary : this.packRepository.getSelectedPacks()) {
			TexturePack pack = this.availablePacksById.get(summary.getId());

			// vanilla and server packs are handled separately
			if (pack != null) {
				this.actuallySelected = pack;
			}
		}
	}

	@Unique
	private String packId(TexturePack pack) {
		if (pack == DEFAULT_PACK) {
			return ClientPackSource.DEFAULT_PACK_ID;
		} else if (pack == this.actuallySelected && this.hasServerPack()) {
			return ClientPackSource.SERVER_PACK_ID;
		} else if (pack instanceof ZippedTexturePack || pack instanceof DirectoryTexturePack) {
			return DirectoryPackSource.packId(((AbstractTexturePackAccess) pack).accessFile().toPath());
		} else {
			return "texturepack/" + pack.getName();
		}
	}
}
