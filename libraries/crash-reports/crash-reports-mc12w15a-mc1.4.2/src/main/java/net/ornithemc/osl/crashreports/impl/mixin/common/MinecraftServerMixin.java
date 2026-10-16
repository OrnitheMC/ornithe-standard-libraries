package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashReport;

import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements CrashReportPopulator {

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
	}

	@WrapOperation(
		method = "populateCrashReport",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/crash/CrashReport;add(Ljava/lang/String;Ljava/util/concurrent/Callable;)V"
		)
	)
	private void osl$crashreports$redirectDetailsToSystemReport(CrashReport report, String key, Callable<String> detail, Operation<Void> op) {
		report.getSystemReport().addDetail(key, detail::call);
	}

	@Inject(
		method = "populateCrashReport",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$populateCrashReport(CrashReport report, CallbackInfoReturnable<CrashReport> cir) {
		CrashReports.notifyListeners(report, this.crashListeners);
	}
}
