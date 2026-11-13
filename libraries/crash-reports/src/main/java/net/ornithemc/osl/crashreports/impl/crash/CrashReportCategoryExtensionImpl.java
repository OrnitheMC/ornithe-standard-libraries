package net.ornithemc.osl.crashreports.impl.crash;

import net.ornithemc.osl.crashreports.api.crash.CrashReportDetail;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;

public interface CrashReportCategoryExtensionImpl extends CrashReportSection {

	@Override
	default void addDetail(String key, CrashReportDetail<String> detail) {
		throw new AbstractMethodError();
	}

	@Override
	default void addDetail(String key, Object detail) {
		throw new AbstractMethodError();
	}

	@Override
	default void addDetailError(String key, Throwable error) {
		throw new AbstractMethodError();
	}
}
