package net.ornithemc.osl.crashreports.api.crash;

public interface CrashReportSection {

	void addDetail(String key, CrashReportDetail<String> detail);

	void addDetail(String key, Object detail);

	void addDetailError(String key, Throwable error);

}
