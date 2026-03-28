package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.IOException;

import net.minecraft.client.gui.screen.ResourcePacksScreen;
import net.minecraft.client.gui.screen.resourcepack.ResourcePackEntry;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.render.texture.TextureUtil;
import net.minecraft.client.resource.pack.ResourcePack;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

class ResourcePackSummaryEntry extends ResourcePackEntry {

	private final ResourcePackSummary summary;
	private final ResourcePack pack;

	private final Identifier iconLocation;

	ResourcePackSummaryEntry(ResourcePacksScreen parent, ResourcePackSummary summary) {
		super(parent);

		this.summary = summary;
		this.pack = Adapters.resourcePack(summary.open());

		DynamicTexture icon;

		try {
			icon = new DynamicTexture(this.pack.getIcon());
		} catch (IOException e) {
			icon = TextureUtil.MISSING_TEXTURE;
		}

		this.iconLocation = this.minecraft.getTextureManager().register("texturepackicon", icon);
	}

	@Override
	protected String getDescription() {
		return this.summary.getDescription().buildFormattedString();
	}

	@Override
	protected void bindIcon() {
		this.minecraft.getTextureManager().bind(this.iconLocation);
	}

	@Override
	protected String getName() {
		return this.summary.getTitle().buildFormattedString();
	}

	@Override
	protected boolean canMove() {
		return !this.summary.isRequired() && !this.summary.isFixedPosition();
	}

	@Override
	protected boolean canMoveRight() {
		return !this.summary.isRequired();
	}

	@Override
	protected boolean canMoveLeft() {
		return !this.summary.isRequired();
	}

	@Override
	protected boolean canMoveUp() {
		return !this.summary.isRequired() && !this.summary.isFixedPosition();
	}

	@Override
	protected boolean canMoveDown() {
		return !this.summary.isRequired() && !this.summary.isFixedPosition();
	}
}
