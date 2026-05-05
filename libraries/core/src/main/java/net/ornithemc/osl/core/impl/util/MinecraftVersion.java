package net.ornithemc.osl.core.impl.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.game.minecraft.McVersionLookup;

public final class MinecraftVersion {

	private static MinecraftVersion INSTANCE;

	public static MinecraftVersion resolve() {
		if (INSTANCE == null) {
			Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer("minecraft");

			if (!mod.isPresent()) {
				throw new IllegalStateException("Minecraft not loaded!");
			}

			ModContainer minecraft = mod.get();
			String gameVersion = FabricLoader.getInstance().getRawGameVersion();
			SemanticVersion semanticVersion = resolveSemanticVersion(minecraft.getMetadata().getVersion());

			INSTANCE = new MinecraftVersion(gameVersion, semanticVersion);
		}

		return INSTANCE;
	}

	private final String gameVersion;
	private final SemanticVersion semanticVersion;

	// semantic versions for other game versions
	private final Map<String, SemanticVersion> semanticVersions = new HashMap<>();

	private MinecraftVersion(String gameVersion, SemanticVersion semanticVersion) {
		this.gameVersion = gameVersion;
		this.semanticVersion = semanticVersion;
	}

	public String gameVersion() {
		return this.gameVersion;
	}

	public String semanticVersion() {
		return this.semanticVersion.toString();
	}

	public int compareTo(String gameVersion) {
		return this.semanticVersion.compareTo((Version) this.semanticVersions.computeIfAbsent(gameVersion, MinecraftVersion::resolveSemanticVersion));
	}

	private static SemanticVersion resolveSemanticVersion(Version gameVersion) {
		if (gameVersion instanceof SemanticVersion) {
			return (SemanticVersion) gameVersion;
		} else {
			return resolveSemanticVersion(gameVersion.toString());
		}
	}

	private static SemanticVersion resolveSemanticVersion(String gameVersion) {
		String releaseVersion = McVersionLookup.getRelease(gameVersion);
		String normalizedVersion = McVersionLookup.normalizeVersion(gameVersion, releaseVersion);

		try {
			return SemanticVersion.parse(normalizedVersion);
		} catch (VersionParsingException e) {
			throw new IllegalStateException("Unable to parse Minecraft version " + gameVersion, e);
		}
	}
}
