package net.ornithemc.osl.resource.loader.api.resource.repository;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;

/**
 * The pack source for resource packs provided by the Minecraft client.
 */
public interface ClientPackSource extends ResourcePackRepository.Source {

	/**
	 * The resource pack ID for Vanilla's built-in assets.
	 */
	String DEFAULT_PACK_ID = "vanilla";
	/**
	 * The resource pack ID for assets provided by the server.
	 */
	String SERVER_PACK_ID = "server";

	/**
	 * @return the built-in resource pack for the default look of Minecraft;
	 */
	ResourcePack getDefaultResourcePack();

	/**
	 * @return the resource pack provided by the connected Minecraft server.
	 */
	ResourcePack getServerResourcePack();

}
