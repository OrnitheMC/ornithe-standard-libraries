package net.ornithemc.osl.crashreports.api.world;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;

public interface WorldExtension extends CrashReportPopulator {

	CrashReportSection populateCrashReport(CrashReport report);

}
