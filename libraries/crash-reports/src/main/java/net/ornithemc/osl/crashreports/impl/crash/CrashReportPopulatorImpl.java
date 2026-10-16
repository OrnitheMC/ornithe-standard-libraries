package net.ornithemc.osl.crashreports.impl.crash;

import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;

public interface CrashReportPopulatorImpl extends CrashReportPopulator {

	@Override
	default void addCrashListener(CrashListener listener) {
		throw new AbstractMethodError();
	}
}
