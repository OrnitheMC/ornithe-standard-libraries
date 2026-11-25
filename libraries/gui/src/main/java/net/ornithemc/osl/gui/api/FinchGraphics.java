package net.ornithemc.osl.gui.api;

import net.ornithemc.osl.gui.api.pipelines.RenderPipeline;
import net.ornithemc.osl.gui.api.shape.Text;
import net.ornithemc.osl.gui.api.shape.texture.Texture;

public interface FinchGraphics {
	void drawTexture(RenderPipeline pipeline, Texture texture, int x, int y);

	void drawText(RenderPipeline pipeline, Text text, int x, int y);

	Transforms beginTransform();
}
