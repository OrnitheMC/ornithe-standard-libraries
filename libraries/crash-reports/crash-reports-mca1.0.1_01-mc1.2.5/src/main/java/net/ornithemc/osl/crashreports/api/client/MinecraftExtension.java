package net.ornithemc.osl.crashreports.api.client;

import net.minecraft.util.crash.CrashReport;

import net.ornithemc.osl.crashreports.api.crash.CrashReportPopulator;

public interface MinecraftExtension extends CrashReportPopulator {

	CrashReport populateCrashReport(CrashReport report);

}
