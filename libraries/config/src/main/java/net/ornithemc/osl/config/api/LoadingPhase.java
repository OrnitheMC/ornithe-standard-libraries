package net.ornithemc.osl.config.api;

/**
 * A loading phase defines a point in the scope's lifecycle
 * at which configs are loaded.
 */
public enum LoadingPhase {

	/**
	 * This loading phase is called at the scope's start-up,
	 * before it is initialized.
	 */
	START,
	/**
	 * This loading phase is called after the scope is fully
	 * initialized.
	 */
	READY

}
