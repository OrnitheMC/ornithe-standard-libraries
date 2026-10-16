package net.ornithemc.osl.crashreports.impl.client;

import net.minecraft.util.crash.CrashReport;

import net.ornithemc.osl.crashreports.api.client.MinecraftExtension;
import net.ornithemc.osl.crashreports.impl.crash.CrashReportPopulatorImpl;

public interface MinecraftExtensionImpl extends MinecraftExtension, CrashReportPopulatorImpl {

	@Override
	default CrashReport populateCrashReport(CrashReport report) {
		throw new AbstractMethodError();
	}
}
