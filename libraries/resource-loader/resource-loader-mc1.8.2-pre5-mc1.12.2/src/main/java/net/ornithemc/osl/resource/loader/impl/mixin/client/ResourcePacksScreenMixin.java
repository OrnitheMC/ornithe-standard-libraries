package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ResourcePacksScreen;
import net.minecraft.client.gui.screen.resourcepack.ResourcePackEntry;

import net.ornithemc.osl.resource.loader.impl.adapter.ResourcePackLists;

@Mixin(ResourcePacksScreen.class)
public class ResourcePacksScreenMixin {

	@Shadow
	private List<ResourcePackEntry> appliedPacks;

	@Inject(
		method = "init",
		slice = @Slice(
			from = @At(
				value = "NEW",
				target = "net/minecraft/client/gui/screen/resourcepack/DefaultResourcePackEntry"
			)
		),
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
			ordinal = 0,
			shift = Shift.AFTER
		)
	)
	private void osl$resource_loader$fixSelection(CallbackInfo ci) {
		// ResourcePacks.getSelectedPacks only contains directory or zip packs
		// The default pack and server pack are added in the target method, so
		// we must add any other required packs manually as well

		ResourcePackLists.fixSelection(
			(ResourcePacksScreen) (Object) this,
			this.appliedPacks,
			true
		);
	}
}
