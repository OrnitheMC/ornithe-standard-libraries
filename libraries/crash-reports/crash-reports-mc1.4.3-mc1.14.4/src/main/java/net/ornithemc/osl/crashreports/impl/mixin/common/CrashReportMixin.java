package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.io.File;
import java.nio.file.Path;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.impl.CrashReports;

@Mixin(net.minecraft.util.crash.CrashReport.class)
public class CrashReportMixin implements CrashReport {

	private static final boolean SAVING_FORWARDING_SUPPORTED = MinecraftVersion.resolve().compareTo("13w07a") < 0 || MinecraftVersion.resolve().compareTo("13w37b") > 0;

	@Shadow
	private String description;
	@Shadow
	private Throwable exception;
	@Shadow
	private net.minecraft.util.crash.CrashReportCategory systemDetails;
	@Shadow
	private File file;

	@Shadow
	private net.minecraft.util.crash.CrashReportCategory addCategory(String name) { return null; }
	@Shadow
	private net.minecraft.util.crash.CrashReportCategory addCategory(String name, int ignoredStackTraceCallCount) { return null; }
	@Shadow
	private String getExceptionMessage() { return null; }
	@Shadow
	private String build() { return null; }
	@Shadow
	private void addDetails(StringBuilder sb) { }
	@Conditional(minecraftVersion = @Version({ "<13w07a", ">13w37b" }))
	@Shadow
	private boolean writeToFile(File file) { return false; }

	@Inject(
		method = "fillSystemDetails",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$addModsToSystemDetails(CallbackInfo ci) {
		CrashReports.addModTree(this.systemDetails);
	}

	@Override
	public String getTitle() {
		return this.description;
	}

	@Override
	public Throwable getError() {
		return this.exception;
	}

	@Override
	public CrashReportSection getSystemReport() {
		return this.systemDetails;
	}

	@Override
	public CrashReportSection addSection(String name) {
		return this.addCategory(name, 2);
	}

	@Override
	public CrashReportSection addSection(String name, int stackTraceOffset) {
		return this.addCategory(name, stackTraceOffset + 1);
	}

	@Override
	public String getErrorMessage() {
		return this.getExceptionMessage();
	}

	@Override
	public String buildFriendlyString() {
		return this.build();
	}

	@Override
	public String buildFriendlyStringWithoutHeader() {
		StringBuilder sb = new StringBuilder();
		this.addDetails(sb);
		return sb.toString();
	}

	@Override
	public Path getSaveFile() {
		return this.file.toPath();
	}

	@Override
	public boolean saveToFile(Path file) {
		if (SAVING_FORWARDING_SUPPORTED) {
			return this.writeToFile(file.toFile());
		} else {
			// TODO: figure how to shadow a method with a non existent param type
			// in 13w07a-13w37b there's an extra net.minecraft.util.Logger parameter
			throw new UnsupportedOperationException();
		}
	}
}
