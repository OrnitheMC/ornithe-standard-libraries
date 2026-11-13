package net.ornithemc.osl.crashreports.impl.server;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.server.MinecraftServerExtension;
import net.ornithemc.osl.crashreports.impl.crash.CrashReportPopulatorImpl;

public interface MinecraftServerExtensionImpl extends MinecraftServerExtension, CrashReportPopulatorImpl {

	@Override
	default CrashReport populateCrashReport(CrashReport report) {
		throw new AbstractMethodError();
	}
}
