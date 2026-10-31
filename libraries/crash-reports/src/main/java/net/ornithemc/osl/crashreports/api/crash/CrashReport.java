package net.ornithemc.osl.crashreports.api.crash;

import java.nio.file.Path;

import net.ornithemc.osl.crashreports.impl.crash.CrashReportImpl;

public interface CrashReport {

	String getTitle();

	Throwable getError();

	CrashReportSection getSystemReport();

	CrashReportSection addSection(String name);

	CrashReportSection addSection(String name, int stackTraceOffset);

	String getErrorMessage();

	String buildFriendlyString();

	String buildFriendlyStringWithoutHeader();

	Path getSaveFile();

	boolean saveToFile(Path file);

	static CrashReport of(String title, Throwable exception) {
		return CrashReportImpl.of(title, exception);
	}
}
