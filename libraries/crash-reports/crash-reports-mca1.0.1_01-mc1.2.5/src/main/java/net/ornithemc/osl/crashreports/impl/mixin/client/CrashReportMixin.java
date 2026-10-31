package net.ornithemc.osl.crashreports.impl.mixin.client;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

	@Unique
	private CrashReportSectionImpl systemDetails;
	@Unique
	private List<CrashReportSectionImpl> sections;
	@Unique
	private boolean trackingStackTrace;
	@Unique
	private StackTraceElement[] stackTrace;
	@Unique
	private Path file;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$crashreports$init(CallbackInfo ci) {
		this.systemDetails = new CrashReportSectionImpl("System Details");
		this.sections = new ArrayList<>();

		this.trackingStackTrace = true;

		CrashReports.addSystemDetails(this.systemDetails);
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
		Throwable exception = this.exception;

		if (exception.getMessage() == null) {
			if (exception instanceof NullPointerException) {
				exception = replaceException(exception, new NullPointerException(this.description));
			} else if (exception instanceof StackOverflowError) {
				exception = replaceException(exception, new StackOverflowError(this.description));
			} else if (exception instanceof OutOfMemoryError) {
				exception = replaceException(exception, new OutOfMemoryError(this.description));
			}
		}

		StringWriter writer = new StringWriter();

		try (PrintWriter printer = new PrintWriter(writer)) {
			exception.printStackTrace(new PrintWriter(writer));
			return writer.toString();
		}
	}

	private static Throwable replaceException(Throwable original, Throwable copy) {
		try {
			Throwable cause = original.getCause();

			if (cause != null) {
				copy.initCause(cause);
			}

			Throwable[] suppresseds = original.getSuppressed();

			for (Throwable suppressed : suppresseds) {
				copy.addSuppressed(suppressed);
			}

			copy.setStackTrace(original.getStackTrace());

			return copy;
		} catch (Throwable t) {
			return original;
		}
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

		this.systemDetails.buildString(sb);
	}

	@Override
	public Path getSaveFile() {
		return this.file;
	}

	@Override
	public boolean saveToFile(Path file) {
		if (this.file != null) {
			return false;
		}

		if (file.getParent() != null) {
			file.getParent().toFile().mkdirs();
		}

		try (BufferedWriter bw = Files.newBufferedWriter(file)) {
			bw.write(this.buildFriendlyString());
			this.file = file;
			return true;
		} catch (Throwable t) {
			CrashReports.LOGGER.error("Could not save crash report to {}", file, t);
			return false;
		}
	}
}
