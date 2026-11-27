package net.ornithemc.osl.gui.api.pipelines;

public class RenderPipelines {
	public static RenderPipeline GUI_TEXTURED = (gui, transforms) -> {
		transforms.enableBlend();
	};
}
