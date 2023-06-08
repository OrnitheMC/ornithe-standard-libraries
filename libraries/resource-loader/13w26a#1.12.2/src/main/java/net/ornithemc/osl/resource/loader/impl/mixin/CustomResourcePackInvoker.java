package net.ornithemc.osl.resource.loader.impl.mixin;

import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.pack.CustomResourcePack;

@Mixin(CustomResourcePack.class)
public interface CustomResourcePackInvoker {

	@Invoker("getMetadataSection(Lnet/minecraft/client/resource/metadata/ResourceMetadataSerializerRegistry;Ljava/io/InputStream;Ljava/lang/String;)Lnet/minecraft/client/resource/metadata/ResourceMetadataSection;")
	public static <T extends ResourceMetadataSection> T invokeGetMetadataSection(ResourceMetadataSerializerRegistry metadataSerializers, InputStream is, String name) {
		throw new UnsupportedOperationException();
	}
}
