package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.io.IOException;
import java.util.function.Supplier;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackCompatibility;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackMetadata;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class SimpleResourcePackSummary implements ResourcePackSummary {

	public static ResourcePackSummary create(ResourcePack pack, boolean required, boolean fixedPosition, PackPosition defaultPosition) {
		return create(pack.getId(), required, fixedPosition, defaultPosition, () -> pack);
	}

	public static ResourcePackSummary create(String id, boolean required, boolean fixedPosition, PackPosition defaultPosition, Supplier<ResourcePack> opener) {
		try {
			ResourcePack pack = opener.get();
			ResourcePackMetadata metadata = pack.getMetadata(ResourcePackMetadata.SECTION);
			String name = pack.getName();

			if (metadata != null) {
				return new SimpleResourcePackSummary(metadata, id, name, required, fixedPosition, defaultPosition, opener);
			} else {
				ResourceLoader.LOGGER.warn("Could not find pack metadata for pack {}", id);
			}
		} catch (IOException e) {
			ResourceLoader.LOGGER.warn("Could not load pack metadata for pack {}: {}", id, e);
		}

		return null;
	};

	private final TextComponent title;
	private final TextComponent description;
	private final String id;
	private final boolean required;
	private final boolean fixedPosition;
	private final PackPosition defaultPosition;
	private final PackCompatibility compatibility;
	private final Supplier<ResourcePack> opener;

	public SimpleResourcePackSummary(ResourcePackMetadata metadata, String id, String name, boolean required, boolean fixedPosition, PackPosition defaultPosition, Supplier<ResourcePack> opener) {
		this(
			TextComponents.literal(name),
			metadata.description(),
			id,
			required,
			fixedPosition,
			defaultPosition,
			PackCompatibility.forFormat(metadata.format()),
			opener
		);
	}

	public SimpleResourcePackSummary(TextComponent title, TextComponent description, String id, boolean required, boolean fixedPosition, PackPosition defaultPosition, PackCompatibility compatibility, Supplier<ResourcePack> opener) {
		this.title = title;
		this.description = description;
		this.id = id;
		this.required = required;
		this.fixedPosition = fixedPosition;
		this.defaultPosition = defaultPosition;
		this.compatibility = compatibility;
		this.opener = opener;
	}

	@Override
	public TextComponent getTitle() {
		return this.title;
	}

	@Override
	public TextComponent getDescription() {
		return this.description;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isRequired() {
		return this.required;
	}

	@Override
	public boolean isFixedPosition() {
		return this.fixedPosition;
	}

	@Override
	public PackPosition getDefaultPosition() {
		return this.defaultPosition;
	}

	@Override
	public PackCompatibility getCompatibility() {
		return this.compatibility;
	}

	@Override
	public ResourcePack open() {
		return this.opener.get();
	}

	@Override
	public void close() {
	}
}
