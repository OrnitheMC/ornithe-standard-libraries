package net.ornithemc.osl.crashreports.impl;

import java.util.function.Consumer;

import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

public final class CrashExceptionsImpl {

	public static RuntimeException from(String title, Throwable error) {
		return from(title, error, report -> { });
	}

	public static RuntimeException from(String title, Throwable error, Consumer<net.ornithemc.osl.crashreports.api.crash.CrashReport> builder) {
		CrashReport report = CrashReport.of(error, title);
		builder.accept(report);

		return new CrashException(report);
	}
}
