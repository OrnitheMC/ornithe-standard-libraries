package net.ornithemc.osl.gui.api.shape.texture;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.gui.api.color.Color;
import net.ornithemc.osl.gui.api.color.Colors;
import net.ornithemc.osl.gui.impl.shape.TextureImpl;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Texture {
	static Texture of(NamespacedIdentifier identifier, int width, int height) {
		return new TextureImpl(identifier, width, height, null, null);
	}

	NamespacedIdentifier identifier();

	int width();

	int height();

	@Nullable Subsection subsection();

	@Nullable Color filter();

	Texture withSubsection(Subsection subsection);

	Texture withFilter(Color filter);

	default Texture withTransparency(double alpha) {
		return this.withFilter(Optional.ofNullable(this.filter()).orElse(Colors.WHITE).withAlpha(alpha));
	}
}
