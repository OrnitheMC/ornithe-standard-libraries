package net.ornithemc.osl.crashreports.impl;

import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import net.ornithemc.osl.crashreports.api.crash.CrashException;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;

public final class CrashExceptionsImpl {

	private static final boolean USE_VANILLA_CRASH_REPORT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;

	public static RuntimeException from(String title, Throwable error) {
		return from(title, error, report -> { });
	}

	public static RuntimeException from(String title, Throwable error, Consumer<CrashReport> builder) {
		CrashReport report;
		if (error instanceof CrashException) {
			report = ((CrashException) error).getReport();
		} else if (!USE_VANILLA_CRASH_REPORT) {
			report = CrashReport.of(title, error);
		} else {
			report = new net.minecraft.util.crash.CrashReport(title, error);
		}
		builder.accept(report);

		return new CrashException(report);
	}
}
