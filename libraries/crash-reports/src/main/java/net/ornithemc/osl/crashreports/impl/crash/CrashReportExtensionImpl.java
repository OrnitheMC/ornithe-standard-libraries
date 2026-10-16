package net.ornithemc.osl.crashreports.impl.crash;

import java.nio.file.Path;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;

public interface CrashReportExtensionImpl extends CrashReport {

	@Override
	default String getTitle() {
		throw new AbstractMethodError();
	}

	@Override
	default Throwable getError() {
		throw new AbstractMethodError();
	}

	@Override
	default CrashReportSection getSystemReport() {
		throw new AbstractMethodError();
	}

	@Override
	default CrashReportSection addSection(String name) {
		throw new AbstractMethodError();
	}

	@Override
	default CrashReportSection addSection(String name, int stackTraceOffset) {
		throw new AbstractMethodError();
	}

	@Override
	default String getErrorMessage() {
		throw new AbstractMethodError();
	}

	@Override
	default String buildFriendlyString() {
		throw new AbstractMethodError();
	}

	@Override
	default String buildFriendlyStringWithoutHeader() {
		throw new AbstractMethodError();
	}

	@Override
	default Path getSaveFile() {
		throw new AbstractMethodError();
	}

	@Override
	default boolean saveToFile(Path file) {
		throw new AbstractMethodError();
	}
}
