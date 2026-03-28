package net.ornithemc.osl.resource.loader.impl.mixin.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.client.resource.pack.CustomResourcePack;

@Mixin(CustomResourcePack.class)
public interface CustomResourcePackAccess {

	@Accessor("file")
	File accessFile();

	@Invoker("openResource")
	InputStream invokeOpenResource(String path) throws IOException;

	@Invoker("hasResource")
	boolean invokeHasResource(String path);

	@Invoker("getMetadataSection")
	public static <T extends ResourceMetadataSection> T invokeGetMetadataSection(ResourceMetadataSerializerRegistry metadataSerializers, InputStream is, String name) {
		throw new UnsupportedOperationException();
	}
}
