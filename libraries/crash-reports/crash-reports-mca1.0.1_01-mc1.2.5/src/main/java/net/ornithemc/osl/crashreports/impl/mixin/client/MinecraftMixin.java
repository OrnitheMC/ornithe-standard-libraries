package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.World;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.crashreports.api.crash.CrashException;
import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(Minecraft.class)
public class MinecraftMixin implements CrashReportPopulator {

	@Shadow
	private World world;

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
	}

	@WrapOperation(
		method = "run",
		at = @At(
			value = "NEW",
			target = "net/minecraft/util/crash/CrashReport"
		)
	)
	private CrashReport osl$crashreports$initCrashReport(String title, Throwable error, Operation<CrashReport> op) {
		CrashReport report = this.extractCrashReport(error);

		if (report == null) {
			return op.call(title, error);
		} else {
			return report;
		}
	}

	@Conditional(minecraftVersion = @Version("<=b1.5_02"))
	@WrapOperation(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;m_49334482(Lnet/minecraft/util/crash/CrashReport;)V"
		)
	)
	private void osl$crashreports$populateCrashReport1(Minecraft self, CrashReport report, Operation<Void> op) {
		op.call(self, this.populateCrashReport(report));
	}

	@Conditional(minecraftVersion = @Version(">b1.5_02"))
	@WrapOperation(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;gameCrashed(Lnet/minecraft/util/crash/CrashReport;)V"
		)
	)
	private void osl$crashreports$populateCrashReport2(Minecraft self, CrashReport report, Operation<Void> op) {
		op.call(self, this.populateCrashReport(report));
	}
	
	@Unique
	private CrashReport extractCrashReport(Throwable error) {
		if (error instanceof CrashException) {
			CrashException exception = (CrashException) error;

			if (exception.getReport() instanceof CrashReport) {
				return (CrashReport) exception.getReport();
			}
		}

		return null;
	}

	@Unique
	private CrashReport populateCrashReport(CrashReport report) {
		CrashReportSection systemReport = report.getSystemReport();

		systemReport.addDetail("LWJGL", () -> Sys.getVersion());
		systemReport.addDetail("OpenGL", () -> GL11.glGetString(GL11.GL_RENDERER) + " GL version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR));

		if (this.world != null) {
			this.world.populateCrashReport(report);
		}

		CrashReports.notifyListeners(report, systemReport, this.crashListeners);

		return report;
	}

	@WrapOperation(
		method = "shutdown",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/MinecraftApplet;clearMemory()V"
		)
	)
	private void osl$crashreports$cancelAppletDestroy(MinecraftApplet applet, Operation<Void> op) {
		// cancel this call as it prevents the crash report panel to open properly
	}
}
