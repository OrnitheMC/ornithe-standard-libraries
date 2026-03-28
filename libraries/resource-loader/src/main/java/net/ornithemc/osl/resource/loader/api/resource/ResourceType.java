package net.ornithemc.osl.resource.loader.api.resource;

/**
 * Each resource manager deals with exactly one type of resources.
 * Resources of a given type have their own directory in a resource
 * pack.
 */
public enum ResourceType {

	/**
	 * Client assets are things like textures, sounds, and language files.
	 */
	CLIENT_ASSETS("assets"),
	/**
	 * Server data are things like loot tables, recipes, and command functions.
	 */
	SERVER_DATA("data");

	/**
	 * The directory in a resource pack for this type of resources.
	 */
	private final String directory;

	private ResourceType(String directory) {
		this.directory = directory;
	}

	/**
	 * @return the directory in a resource pack for this type of resources.
	 */
	public String directory() {
		return this.directory;
	}
}
