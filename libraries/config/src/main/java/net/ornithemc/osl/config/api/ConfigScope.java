package net.ornithemc.osl.config.api;

public enum ConfigScope {

	GLOBAL("config"),
	WORLD ("config");

	private final String path;

	private ConfigScope(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
