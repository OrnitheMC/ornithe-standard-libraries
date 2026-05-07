package net.ornithemc.osl.resource.loader.api.resource.repository;

import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;

/**
 * The pack source for resource packs provided by the Minecraft server.
 */
public interface ServerPackSource extends ResourcePackRepository.Source {

	/**
	 * @return the built-in resource pack for the default behavior of Minecraft;
	 */
	ResourcePack getDefaultResourcePack();

}
