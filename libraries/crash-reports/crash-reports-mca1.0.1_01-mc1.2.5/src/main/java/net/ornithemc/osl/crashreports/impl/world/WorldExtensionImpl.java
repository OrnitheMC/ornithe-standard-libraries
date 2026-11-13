package net.ornithemc.osl.crashreports.impl.world;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.api.world.WorldExtension;
import net.ornithemc.osl.crashreports.impl.crash.CrashReportPopulatorImpl;

public interface WorldExtensionImpl extends WorldExtension, CrashReportPopulatorImpl {

	@Override
	default CrashReportSection populateCrashReport(CrashReport report) {
		throw new AbstractMethodError();
	}
}
