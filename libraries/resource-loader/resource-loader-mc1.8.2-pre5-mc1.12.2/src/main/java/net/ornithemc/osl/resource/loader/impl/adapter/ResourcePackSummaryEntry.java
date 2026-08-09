package net.ornithemc.osl.resource.loader.impl.adapter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.gui.screen.ResourcePacksScreen;
import net.minecraft.client.gui.screen.resourcepack.ResourcePackEntry;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.render.texture.TextureUtil;
import net.minecraft.resource.Identifier;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

class ResourcePackSummaryEntry extends ResourcePackEntry {

	private static final boolean FALL_BACK_TO_UNKNOWN_PACK_ICON = MinecraftVersion.resolve().compareTo("16w36a") >= 0;
	private static final Identifier UNKNOWN_PACK_ICON_LOCATION = new Identifier("textures/misc/unknown_pack.png");

	private final ResourcePackSummary summary;
	private final Identifier iconLocation;

	ResourcePackSummaryEntry(ResourcePacksScreen parent, ResourcePackSummary summary) {
		super(parent);

		BufferedImage icon = null;

		try (InputStream is = summary.open().getResource(ResourcePack.ICON_FILE)) {
			icon = TextureUtil.readImage(is);
		} catch (IOException e) {
			if (FALL_BACK_TO_UNKNOWN_PACK_ICON) {
				try (InputStream is = this.minecraft.getResourceManager().getResource(UNKNOWN_PACK_ICON_LOCATION).asStream()) {
					icon = TextureUtil.readImage(is);
				} catch (Throwable t) {
					e.addSuppressed(t);
				}
			}

			if (icon == null) {
				try {
					icon = this.minecraft.getResourcePacks().defaultPack.getIcon();
				} catch (Throwable t) {
					e.addSuppressed(t);
				}
			}

			if (icon == null) {
				throw new IllegalStateException("Unable to load pack icon for " + summary.getId(), e);
			}
		}

		this.summary = summary;
		this.iconLocation = this.minecraft.getTextureManager().register("texturepackicon", new DynamicTexture(icon));
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
