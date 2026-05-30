package net.ornithemc.osl.resource.loader.impl.adapter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.minecraft.client.render.texture.TextureUtil;
import net.minecraft.client.resource.metadata.ResourceMetadataSection;
import net.minecraft.client.resource.metadata.ResourceMetadataSerializerRegistry;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackFileNotFoundException;
import net.ornithemc.osl.resource.loader.impl.mixin.client.CustomResourcePackAccess;

class ResourcePackAdapter implements net.minecraft.client.resource.pack.ResourcePack {

	final ResourcePack pack;

	ResourcePackAdapter(ResourcePack pack) {
		this.pack = pack;
	}

	@Override
	public InputStream getResource(Identifier location) throws IOException {
		IOSupplier<InputStream> resource = this.pack.getResource(ResourceType.CLIENT_ASSETS, location);

		if (resource != null) {
			return resource.get();
		}

		throw new ResourcePackFileNotFoundException(this.pack, ResourcePath.nameOf(ResourceType.CLIENT_ASSETS, location));
	}

	@Override
	public boolean hasResource(Identifier location) {
		return this.pack.hasResource(ResourceType.CLIENT_ASSETS, location);
	}

	@Override
	public Set<String> getNamespaces() {
		return this.pack.getNamespaces(ResourceType.CLIENT_ASSETS);
	}

	@Override
	public <T extends ResourceMetadataSection> T getMetadataSection(ResourceMetadataSerializerRegistry metadataSerializers, String name) throws IOException {
		return CustomResourcePackAccess.invokeGetMetadataSection(metadataSerializers, this.pack.getResource(ResourcePack.METADATA_FILE), name);
	}

	@Override
	public BufferedImage getIcon() throws IOException {
		return TextureUtil.readImage(this.pack.getResource(ResourcePack.ICON_FILE));
	}

	@Override
	public String getName() {
		return this.pack.getName();
	}
}
