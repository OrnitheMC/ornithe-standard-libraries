package net.ornithemc.osl.crashreports.impl.mixin.server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.api.server.MinecraftServerExtension;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerExtension {

	private static final boolean OVERWORLD_DIMENSION_ONLY = MinecraftVersion.resolve().compareTo("b1.5_02") <= 0;

	@Shadow
	private PlayerManager playerManager;
	@Conditional(minecraftVersion = @Version("<=b1.5_02"))
	@Shadow
	private ServerWorld f_58488472;
	@Conditional(minecraftVersion = @Version(">b1.5_02"))
	@Shadow
	private ServerWorld[] worlds;

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
	}

	@Conditional(minecraftVersion = @Version("<b1.3"))
	@Inject(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Exception;printStackTrace()V",
			shift = Shift.AFTER
		)
	)
	private void osl$crashreports$saveCrashReport1(CallbackInfo ci, @Local Exception error) {
		this.saveCrashReport(error);
	}

	@Conditional(minecraftVersion = @Version(">=b1.3"))
	@Inject(
		method = "run",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Throwable;printStackTrace()V",
			shift = Shift.AFTER
		)
	)
	private void osl$crashreports$saveCrashReport2(CallbackInfo ci, @Local Throwable error) {
		this.saveCrashReport(error);
	}

	@Unique
	private void saveCrashReport(Throwable error) {
		CrashReport report = this.populateCrashReport(CrashReport.of("Exception in server tick loop", error));
		Path file = Paths.get("crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");

		if (report.saveToFile(file)) {
			CrashReports.LOGGER.error("This crash report has been saved to: {}", file.toAbsolutePath());
		} else {
			CrashReports.LOGGER.error("We were unable to save this crash report to disk.");
		}
	}

	@Override
	public CrashReport populateCrashReport(CrashReport report) {
		CrashReportSection systemReport = report.getSystemReport();

		if (this.playerManager != null) {
			systemReport.addDetail("Player count", this.playerManager.getCount() + " / " + this.playerManager.getMaxCount() + "; " + this.playerManager.players);
		}

		if (OVERWORLD_DIMENSION_ONLY) {
			if (this.f_58488472 != null) {
				this.f_58488472.populateCrashReport(report);
			}
		} else {
			if (this.worlds != null) {
				for (ServerWorld world : this.worlds) {
					world.populateCrashReport(report);
				}
			}
		}

		CrashReports.notifyListeners(report, systemReport, this.crashListeners);

		return report;
	}
}
