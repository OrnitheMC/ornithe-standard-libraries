package net.ornithemc.osl.crashreports.impl.crash;

import net.ornithemc.osl.crashreports.api.crash.CrashReportDetail;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;

public interface ModifiableCrashReportSection extends CrashReportSection {

	void removeDetail(String key);

	default void replaceDetail(String key, CrashReportDetail<String> detail) {
		this.replaceDetail(key, key, detail);
	}

	void replaceDetail(String oldKey, String newKey, CrashReportDetail<String> detail);

}
