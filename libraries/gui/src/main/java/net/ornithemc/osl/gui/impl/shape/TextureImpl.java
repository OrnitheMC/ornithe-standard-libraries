package net.ornithemc.osl.gui.impl.shape;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.gui.api.color.Color;
import net.ornithemc.osl.gui.api.shape.texture.Texture;
import net.ornithemc.osl.gui.api.shape.texture.Subsection;
import org.jetbrains.annotations.Nullable;

public class TextureImpl implements Texture {
	private final NamespacedIdentifier identifier;
	private final int width;
	private final int height;
	private final @Nullable Subsection subsection;
	private final @Nullable Color filter;

	public TextureImpl(NamespacedIdentifier identifier, int width, int height, @Nullable Subsection subsection, @Nullable Color filter) {
		this.identifier = identifier;
		this.width = width;
		this.height = height;
		this.subsection = subsection;
		this.filter = filter;
	}

	@Override
	public NamespacedIdentifier identifier() {
		return this.identifier;
	}

	@Override
	public int width() {
		return this.width;
	}

	@Override
	public int height() {
		return this.height;
	}

	@Nullable
	@Override
	public Subsection subsection() {
		return this.subsection;
	}

	@Nullable
	@Override
	public Color filter() {
		return this.filter;
	}

	@Override
	public Texture withSubsection(Subsection subsection) {
		return new TextureImpl(this.identifier, this.width, this.height, subsection, this.filter);
	}

	@Override
	public Texture withFilter(Color filter) {
		return new TextureImpl(this.identifier, this.width, this.height, this.subsection, filter);
	}
}
