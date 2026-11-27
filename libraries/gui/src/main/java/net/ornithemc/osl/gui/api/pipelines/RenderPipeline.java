package net.ornithemc.osl.gui.api.pipelines;

import net.ornithemc.osl.gui.api.FinchGraphics;
import net.ornithemc.osl.gui.api.Transforms;

public interface RenderPipeline {
	void transform(FinchGraphics gui, Transforms transforms);
}
