package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.resource.pack.AbstractTexturePack;

import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

// one heck of a hack to inject OSL's resource manager
// as uninvasively as possible: turn it into a texture
// pack and select it in the texture pack manager
public class ResourceManagerAdapter extends AbstractTexturePack {

	private final ResourceManager resourceManager;

	public ResourceManagerAdapter(ResourceManager resourceManager) {
		super(
			"osl:resource_manager",
			null,
			"OSL Resource Manager",
			null
		);

		this.resourceManager = resourceManager;
	}

	@Override
	public InputStream openResource(String path) throws IOException {
		return this.resourceManager.getResource(ResourcePath.relative(path));
	}

	@Override
	public boolean hasResource(String path) {
		return this.resourceManager.hasResource(ResourcePath.relative(path));
	}

	@Override
	public boolean isCompatible() {
		return true;
	}

	@Override
	protected void loadIcon() {
	}

	@Override
	protected void loadDescription() {
		this.descriptionLine1 = "Adapter for OSL's Resource Manager";
		this.descriptionLine2 = null;
	}
}
