package net.ornithemc.osl.resource.loader.impl.adapter;

import java.io.InputStream;

import net.minecraft.client.gui.screen.ResourcePacksScreen;
import net.minecraft.client.gui.screen.resourcepack.ResourcePackEntry;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.render.texture.TextureUtil;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

class ResourcePackSummaryEntry extends ResourcePackEntry {

	private final ResourcePackSummary summary;
	private final Identifier iconLocation;

	ResourcePackSummaryEntry(ResourcePacksScreen parent, ResourcePackSummary summary) {
		super(parent);

		DynamicTexture icon;

		try (InputStream is = summary.open().getResource(ResourcePack.ICON_FILE)) {
			icon = new DynamicTexture(TextureUtil.readImage(is));
		} catch (Throwable t) {
			icon = TextureUtil.MISSING_TEXTURE;
		}

		this.summary = summary;
		this.iconLocation = this.minecraft.getTextureManager().register("texturepackicon", icon);
	}

	@Override
	protected int getFormat() {
		return this.summary.getCompatibility().asFormat();
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

	@Override
	public boolean m_42427629() {
		return this.summary.isFixedPosition() && this.summary.getDefaultPosition() == PackPosition.TOP;
	}
}
