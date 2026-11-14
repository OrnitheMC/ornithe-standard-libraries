package net.ornithemc.osl.resource.loader.api.resource.reload;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

/**
 * A simple resource reloader that parses all resources in a given directory
 * into JSON objects.
 */
public abstract class SimpleJsonResourceReloader implements SimpleResourceReloader<Map<NamespacedIdentifier, JsonObject>> {

	private static final String FILE_EXTENSION = ".json";

	private final Gson gson;
	private final String directory;

	protected SimpleJsonResourceReloader(Gson gson, String directory) {
		this.gson = gson;
		this.directory = directory;
	}

	@Override
	public Map<NamespacedIdentifier, JsonObject> reloadResources(ResourceManager resourceManager) {
		Map<NamespacedIdentifier, JsonObject> resources = new HashMap<>();

		int prefixLength = this.directory.length() + 1;
		int suffixLength = FILE_EXTENSION.length();

		resourceManager.findResources(this.directory, location -> location.identifier().endsWith(FILE_EXTENSION)).forEach((location, resource) -> {
			NamespacedIdentifier identifier = location.subidentifier(
				prefixLength,
				location.identifier().length() - suffixLength
			);

			try (BufferedReader br = resource.openAsReader()) {
				JsonObject json = this.gson.fromJson(br, JsonObject.class);

				if (json == null) {
					ResourceLoader.LOGGER.error("Couldn't load data file {} from {} as it's null or empty", identifier, location);
				} else {
					json = resources.put(identifier, json);

					if (json != null) {
						throw new IllegalStateException("Duplicate data file with ID " + identifier);
					}
				}
			} catch (JsonParseException | IllegalArgumentException e) {
				ResourceLoader.LOGGER.error("Couldn't parse data file {}", identifier, e);
			} catch (IOException e) {
				ResourceLoader.LOGGER.error("Couldn't read data file {} from {}", identifier, location, e);
			}
		});

		return resources;
	}
}
