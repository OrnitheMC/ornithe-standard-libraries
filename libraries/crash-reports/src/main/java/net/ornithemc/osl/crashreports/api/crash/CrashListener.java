package net.ornithemc.osl.crashreports.api.crash;

@FunctionalInterface
public interface CrashListener {

	void populateCrashReport(CrashReport report, CrashReportSection section);

}
