package net.ornithemc.osl.resource.loader.impl.adapter;

import net.minecraft.client.resource.pack.UnopenedResourcePack;
import net.minecraft.resource.pack.repository.UnopenedPack;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackCompatibility;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

class WrappedUnopenedPack<T extends UnopenedPack> implements ResourcePackSummary {

	final T pack;

	WrappedUnopenedPack(T pack) {
		this.pack = pack;
	}

	@Override
	public TextComponent getTitle() {
		if (this.pack instanceof UnopenedResourcePack) {
			// TODO: convert Vanilla Text -> OSL TextComponent
			return TextComponents.literal(((UnopenedResourcePack) this.pack).getTitle().getFormattedString());
		}

		return null;
	}

	@Override
	public TextComponent getDescription() {
		if (this.pack instanceof UnopenedResourcePack) {
			// TODO: convert Vanilla Text -> OSL TextComponent
			return TextComponents.literal(((UnopenedResourcePack) this.pack).getDescription().getFormattedString());
		}

		return null;
	}

	@Override
	public String getId() {
		return this.pack.getId();
	}

	@Override
	public boolean isRequired() {
		return this.pack.isRequired();
	}

	@Override
	public boolean isFixedPosition() {
		return this.pack.isFixed();
	}

	@Override
	public PackPosition getDefaultPosition() {
		return Adapters.packPosition(this.pack.getPosition());
	}

	@Override
	public PackCompatibility getCompatibility() {
		return Adapters.packCompatibility(this.pack.getCompatibility());
	}

	@Override
	public ResourcePack open() {
		return Adapters.resourcePack(this.pack.build());
	}

	@Override
	public void close() {
	}
}
