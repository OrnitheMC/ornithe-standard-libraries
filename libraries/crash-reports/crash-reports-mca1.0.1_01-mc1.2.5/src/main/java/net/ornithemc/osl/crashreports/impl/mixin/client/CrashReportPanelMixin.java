package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.awt.TextArea;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.CrashReportPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.crash.CrashReport;

@Mixin(CrashReportPanel.class)
public class CrashReportPanelMixin {

	private static final String GENERATED = "Generated ";
	private static final String OPENGL = "OpenGL: ";
	private static final String PROBLEM = "Minecraft has stopped running because it encountered a problem.";

	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "NEW",
			target = "java/awt/TextArea"
		)
	)
	private TextArea osl$crashreports$displayCrashReport(String text, int row, int columns, int scrollbars, Operation<TextArea> op, @Local CrashReport report) {
		int generatedIndex = text.indexOf(GENERATED);
		int newlineAfterGeneratedIndex = text.indexOf('\n', generatedIndex + GENERATED.length());
		int openGlIndex = text.indexOf(OPENGL, newlineAfterGeneratedIndex);
		int newlineAfterOpenGlIndex = text.indexOf('\n', openGlIndex + OPENGL.length());

		text = text.substring(0, newlineAfterGeneratedIndex + 2)
			+ report.buildFriendlyStringWithoutHeader()
			+ text.substring(newlineAfterOpenGlIndex + 1);

		int problemIndex = text.indexOf(PROBLEM);

		if (problemIndex > 0) {
			Path saveFile = report.getSaveFile();

			if (saveFile == null) {
				report.saveToFile(
					Minecraft.getWorkingDirectory().toPath()
						.resolve("crash-reports")
						.resolve("crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt")
				);

				saveFile = report.getSaveFile();
			}

			String savedReportMessage;

			if (saveFile == null) {
				savedReportMessage = "We were unable to save this report to a file.";
			} else {
				savedReportMessage = "This error has been saved to "
						+ saveFile.toAbsolutePath()
					    + " for your convenience. Please include a copy of this file if you report this crash to anyone.";
			}

			text = text.substring(0, problemIndex + PROBLEM.length() - 1)
				+ "; "
				+ report.getTitle()
				+ "\n"
				+ savedReportMessage
				+ text.substring(problemIndex + PROBLEM.length());
		}

		return op.call(text, row, columns, scrollbars);
	}
}
