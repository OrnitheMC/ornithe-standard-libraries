package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.pack.CustomResourcePack;

@Mixin(CustomResourcePack.class)
public interface CustomResourcePackInvoker {

	@Invoker("getMetadataSection")
	public static <T extends ResourceMetadataSection> T invokeGetMetadataSection(ResourceMetadataSerializerRegistry metadataSerializers, InputStream is, String name) {
		throw new UnsupportedOperationException();
	}
}
