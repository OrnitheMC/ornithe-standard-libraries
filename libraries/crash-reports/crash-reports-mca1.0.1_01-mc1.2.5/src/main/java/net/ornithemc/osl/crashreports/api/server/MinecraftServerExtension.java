package net.ornithemc.osl.crashreports.api.server;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;

public interface MinecraftServerExtension extends CrashReportPopulator {

	CrashReport populateCrashReport(CrashReport report);

}
