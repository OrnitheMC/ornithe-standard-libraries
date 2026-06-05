package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.resource.pack.TexturePack;

import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

// one heck of a hack to inject OSL's resource manager
// as uninvasively as possible: turn it into a texture
// pack and select it in the texture pack manager
public class ResourceManagerAdapter extends TexturePack {

	private final ResourceManager resourceManager;

	public ResourceManagerAdapter(ResourceManager resourceManager) {
		this.key = "osl:resource_manager";
		this.name = "OSL Resource Manager";
		this.descriptionLine1 = "Adapter for OSL's Resource Manager";
		this.descriptionLine1 = null;

		this.resourceManager = resourceManager;
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		return this.resourceManager.getResource(ResourcePath.relative(path));
	}
}
