package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.crash.CrashReport;

import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(Minecraft.class)
public class MinecraftMixin implements CrashReportPopulator {

	@Unique
	private final List<CrashListener> crashListeners = new ArrayList<>();

	@Override
	public void addCrashListener(CrashListener listener) {
		this.crashListeners.add(listener);
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
