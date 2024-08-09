package net.ornithemc.osl.config.api;

/**
 * The config scope defines the context in which configs should apply. Configs
 * are loaded when the scope is initialized, and unloaded when it shuts down.
 * A config only has defined behavior from within its scope, and should not be
 * accessed when its scope is not active.
 */
public enum ConfigScope {

	/**
	 * The global scope is used for configs that apply globally across the game.
	 * This scope is used for both the client and dedicated server environments.
	 * Global configs are loaded upon game start up, and unloaded upon game
	 * shutdown
	 */
	GLOBAL,
	/**
	 * The world scope is used for configs that apply to a specific world save.
	 * World configs are loaded upon world initialization, and are unloaded when
	 * the world is closed. For the dedicated server environment, this coincides
	 * with game start up and shutdown, while for the client environment, this
	 * coincides with opening and closing a world in single-player.
	 */
	WORLD

}
