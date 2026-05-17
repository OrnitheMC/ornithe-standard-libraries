package net.ornithemc.osl.core.impl.util;

import net.fabricmc.loader.api.FabricLoader;

public enum ModLoader {

	FABRIC("fabric", "fabricloader", "Fabric"),
	QUILT ("quilt" , "quilt_loader", "Quilt");

	public static ModLoader INSTANCE;

	public static ModLoader resolve() {
		if (INSTANCE == null) {
			// Quilt also provides fabricloader as a mod so quiltloader must be checked first
			if (FabricLoader.getInstance().isModLoaded(QUILT.modId)) {
				INSTANCE = QUILT;
			} else if (FabricLoader.getInstance().isModLoaded(FABRIC.modId)) {
				INSTANCE = FABRIC;
			} else {
				throw new RuntimeException("Neither Fabric Loader nor Quilt Loader are loaded?! How is this even running?!");
			}
		}

		return INSTANCE;
	}

	private final String id;
	private final String modId;
	private final String displayName;

	private ModLoader(String id, String modId, String displayName) {
		this.id = id;
		this.modId = modId;
		this.displayName = displayName;
	}

	public String id() {
		return this.id;
	}

	public String modId() {
		return this.modId;
	}

	public String displayName() {
		return this.displayName;
	}
}
