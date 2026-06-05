package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.client.resource.pack.TexturePack;

import net.ornithemc.osl.resource.loader.api.resource.ResourcePath;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;

// one heck of a hack to inject OSL's resource manager
// as uninvasively as possible: turn it into a texture
// pack and select it in the texture pack manager
// TODO: switch to extends AbstractTexturePack once Ploceus is fixed
// it does not properly remap .excs files for method overrides
public class ResourceManagerAdapter implements TexturePack {

	private final ResourceManager resourceManager;

	public ResourceManagerAdapter(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public void unload(TextureManager manager) {
	}

	@Override
	public void bindIcon(TextureManager manager) {
	}

	@Override
	public InputStream getResource(String path) throws IOException {
		return this.resourceManager.getResource(ResourcePath.relative(path));
	}

	@Override
	public String getKey() {
		return "osl:resource_manager";
	}

	@Override
	public String getName() {
		return "OSL Resource Manager";
	}

	@Override
	public String getDescriptionLine1() {
		return "Adapter for OSL's Resource Manager";
	}

	@Override
	public String getDescriptionLine2() {
		return null;
	}

	@Override
	public int getResolution() {
		return 16;
	}

	@Override
	public boolean isCompatible() {
		return true;
	}
}
