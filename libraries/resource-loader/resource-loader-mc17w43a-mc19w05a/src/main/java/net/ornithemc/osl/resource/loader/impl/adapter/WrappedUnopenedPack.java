package net.ornithemc.osl.resource.loader.impl.adapter;

import net.minecraft.client.resource.pack.UnopenedResourcePack;
import net.minecraft.resource.pack.repository.UnopenedPack;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackCompatibility;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

class WrappedUnopenedPack implements ResourcePackSummary {

	final UnopenedPack unopenedPack;

	WrappedUnopenedPack(UnopenedPack pack) {
		this.unopenedPack = pack;
	}

	@Override
	public TextComponent getTitle() {
		if (this.unopenedPack instanceof UnopenedResourcePack) {
			// TODO: convert Vanilla Text -> OSL TextComponent
			return TextComponents.literal(((UnopenedResourcePack) this.unopenedPack).getTitle().getFormattedString());
		}

		return null;
	}

	@Override
	public TextComponent getDescription() {
		if (this.unopenedPack instanceof UnopenedResourcePack) {
			// TODO: convert Vanilla Text -> OSL TextComponent
			return TextComponents.literal(((UnopenedResourcePack) this.unopenedPack).getDescription().getFormattedString());
		}

		return null;
	}

	@Override
	public String getId() {
		return this.unopenedPack.getId();
	}

	@Override
	public boolean isRequired() {
		return this.unopenedPack.isRequired();
	}

	@Override
	public boolean isFixedPosition() {
		return this.unopenedPack.isFixed();
	}

	@Override
	public PackPosition getDefaultPosition() {
		return Adapters.packPosition(this.unopenedPack.getPosition());
	}

	@Override
	public PackCompatibility getCompatibility() {
		return Adapters.packCompatibility(this.unopenedPack.getCompatibility());
	}

	@Override
	public ResourcePack open() {
		return new WrappedPack(this.unopenedPack.build());
	}

	@Override
	public void close() {
	}
}
