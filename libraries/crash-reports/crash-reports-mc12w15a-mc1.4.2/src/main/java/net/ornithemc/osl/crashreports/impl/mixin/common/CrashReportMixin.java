package net.ornithemc.osl.crashreports.impl.mixin.common;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.impl.CrashReports;
import net.ornithemc.osl.crashreports.impl.crash.CrashReportSectionImpl;

@Mixin(net.minecraft.util.crash.CrashReport.class)
public class CrashReportMixin implements CrashReport {

	@Shadow
	private String description;
	@Shadow
	private Throwable exception;
	@Shadow
	private Map<String, String> details;
	@Shadow
	private File file;

	@Unique
	private CrashReportSectionImpl systemReport;
	@Unique
	private List<CrashReportSectionImpl> sections;
	@Unique
	private boolean trackingStackTrace;
	@Unique
	private StackTraceElement[] stackTrace;

	@Shadow
	private String getExceptionMessage() { return null; }
	@Shadow
	private boolean save(File file) { return false; }

	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/crash/CrashReport;fillSystemDetails()V"
		)
	)
	private void osl$crashreports$init(CallbackInfo ci) {
		this.systemReport = new CrashReportSectionImpl("System Details");
		this.sections = new ArrayList<>();

		this.trackingStackTrace = true;
	}

	@WrapOperation(
		method = "fillSystemDetails",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/crash/CrashReport;add(Ljava/lang/String;Ljava/util/concurrent/Callable;)V"
		)
	)
	private void osl$crashreports$redirectDetailsToSystemReport(net.minecraft.util.crash.CrashReport report, String key, Callable<String> detail, Operation<Void> op) {
		this.systemReport.addDetail(key, detail::call);
	}

	@Inject(
		method = "fillSystemDetails",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$addModsToSystemDetails(CallbackInfo ci) {
		CrashReports.addModTree(this.systemReport);
	}

	@Inject(
		method = "add(Ljava/lang/String;Ljava/lang/Object;)V",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$duplicateVanillaDetailToSystemDetails(String key, Object detail, CallbackInfo ci) {
		this.systemReport.addDetail(key, detail);
	}

	@Inject(
		method = "addDetails",
		cancellable = true,
		at = @At(
			value = "HEAD"
		)
	)
	private void osl$crashreports$buildFriendlyString(StringBuilder sb, CallbackInfo ci) {
		this.buildString(sb);

		if (this.details.isEmpty()) {
			ci.cancel();
		} else {
			sb.append("\n\n\n\n\n\n\n\n\n\n");
			sb.append("Miscellaneous details");
			sb.append("\n\n");
		}
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
		return this.systemReport;
	}

	@Override
	public CrashReportSection addSection(String name) {
		return this.addSection(name, 1);
	}

	@Override
	public CrashReportSection addSection(String name, int stackTraceOffset) {
		CrashReportSectionImpl section = new CrashReportSectionImpl(name);
		this.sections.add(section);

		if (this.trackingStackTrace) {
			int length = section.fillStackTrace(stackTraceOffset);

			StackTraceElement[] fullTrace = this.exception.getStackTrace();
			StackTraceElement source = null;
			StackTraceElement next = null;

			int index = fullTrace.length - length;

			if (index < fullTrace.length) {
				source = fullTrace[index];

				if (index + 1 < fullTrace.length) {
					next = fullTrace[index + 1];
				}
			}

			this.trackingStackTrace = section.validateStackTrace(source, next);

			if (fullTrace.length >= length && index >= 0 && index < fullTrace.length) {
				this.stackTrace = new StackTraceElement[index];
				System.arraycopy(fullTrace, 0, this.stackTrace, 0, this.stackTrace.length);
			} else {
				this.trackingStackTrace = false;
			}
		}

		return section;
	}

	@Override
	public String getErrorMessage() {
		return this.getExceptionMessage();
	}

	@Override
	public String buildFriendlyString() {
		StringBuilder sb = new StringBuilder();

		sb.append("---- Minecraft Crash Report ----\n");
		sb.append("// ");
		sb.append(CrashReports.getWittyComment());
		sb.append("\n\n");
		sb.append("Time: ");
		sb.append(new SimpleDateFormat().format(new Date()));
		sb.append("\n");
		sb.append("Description: ");
		sb.append(this.description);
		sb.append("\n\n");
		sb.append(this.getErrorMessage());
		sb.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

		for (int i = 0; i < 87; i++) {
			sb.append("-");
		}

		sb.append("\n\n");

		this.buildString(sb);

		return sb.toString();
	}

	@Override
	public String buildFriendlyStringWithoutHeader() {
		StringBuilder sb = new StringBuilder();
		this.buildString(sb);
		return sb.toString();
	}

	@Unique
	private void buildString(StringBuilder sb) {
		if (this.stackTrace != null && this.stackTrace.length > 0) {
			sb.append("-- Head --\n");
			sb.append("Stacktrace:\n");

			for (StackTraceElement element : this.stackTrace) {
				sb.append("\t").append("at ").append(element.toString());
				sb.append("\n");
			}

			sb.append("\n");
		}

		for (CrashReportSectionImpl section : this.sections) {
			section.buildString(sb);
			sb.append("\n\n");
		}

		this.systemReport.buildString(sb);
	}

	@Override
	public Path getSaveFile() {
		return this.file.toPath();
	}

	@Override
	public boolean saveToFile(Path file) {
		return this.save(file.toFile());
	}
}
