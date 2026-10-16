package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.crash.CrashReportCategory;
import net.minecraft.world.WorldData;

import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(WorldData.class)
public class WorldDataMixin implements CrashReportPopulator {

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
	private void osl$crashreports$populateCrashReport(CrashReportCategory category, CallbackInfo ci) {
		CrashReports.notifyListeners(null, category, this.crashListeners);
	}
}
