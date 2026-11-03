package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.World;
import net.minecraft.world.WorldData;

import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(World.class)
public class WorldMixin implements CrashReportPopulator {

	@Shadow
	private WorldData data;

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Unique
	protected CrashReportSection currentCrashReportSection;

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
	}

	@Inject(
		method = "populateCrashReport",
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$crashreports$createSection(CrashReport report, CallbackInfoReturnable<CrashReport> cir) {
		this.currentCrashReportSection = report.addSection("Affected level");
	}

	@WrapOperation(
		method = "populateCrashReport",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/crash/CrashReport;add(Ljava/lang/String;Ljava/util/concurrent/Callable;)V"
		)
	)
	private void osl$crashreports$redirectDetailsToSection(CrashReport report, String key, Callable<String> detail, Operation<Void> op) {
		this.currentCrashReportSection.addDetail(key, detail::call);
	}

	@Inject(
		method = "populateCrashReport",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$populateCrashReport(CrashReport report, CallbackInfoReturnable<CrashReport> cir) {
		CrashReports.notifyListeners(report, this.currentCrashReportSection, this.crashListeners);
	}
}
