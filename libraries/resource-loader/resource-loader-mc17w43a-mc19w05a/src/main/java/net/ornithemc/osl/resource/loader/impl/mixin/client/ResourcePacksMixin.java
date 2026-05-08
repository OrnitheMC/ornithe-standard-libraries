package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.pack.UnopenedResourcePack;
import net.minecraft.resource.pack.BuiltInPack;
import net.minecraft.resource.pack.Pack;
import net.minecraft.resource.pack.metadata.PackMetadataSection;

import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.access.ResourcePacksAccess;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

@Mixin(net.minecraft.client.resource.pack.ResourcePacks.class)
public class ResourcePacksMixin implements ResourcePacksAccess {

	@Shadow
	private BuiltInPack defaultPack;
	@Shadow
	private UnopenedResourcePack serverPack;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$detetectPackFormat(CallbackInfo ci) {
		try {
			PackMetadataSection metadata = defaultPack.getMetadataSection(PackMetadataSection.SERIALIZER);
			int format = metadata.getFormat();

			ResourcePacks.setSupportedFormat(format);
		} catch (IOException e) {
			ResourceLoader.LOGGER.info("unable to parse pack format from default resource pack", e);
		}
	}

	@Override
	public Pack osl$resource_loader$getServerPack() {
		return this.serverPack == null ? null : this.serverPack.build();
	}
}
