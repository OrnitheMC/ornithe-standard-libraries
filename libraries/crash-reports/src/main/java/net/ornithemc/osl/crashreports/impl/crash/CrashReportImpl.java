package net.ornithemc.osl.crashreports.impl.crash;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ornithemc.osl.crashreports.api.crash.CrashException;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;
import net.ornithemc.osl.crashreports.impl.CrashReports;

public class CrashReportImpl implements CrashReport {

	public static CrashReport of(String title, Throwable exception) {
		if (exception instanceof CrashException) {
			return ((CrashException) exception).getReport();
		} else {
			return new CrashReportImpl(title, exception);
		}
	}

	private final String title;
	private final Throwable exception;
	private final CrashReportSectionImpl systemReport;
	private final List<CrashReportSectionImpl> sections;

	private boolean trackingStackTrace;
	private StackTraceElement[] stackTrace;
	private Path file;

	public CrashReportImpl(String title, Throwable exception) {
		this.title = title;
		this.exception = exception;
		this.systemReport = new CrashReportSectionImpl("System Details");
		this.sections = new ArrayList<>();

		this.trackingStackTrace = true;

		CrashReports.addSystemDetails(this.systemReport);
	}

	@Override
	public String getTitle() {
		return this.title;
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
		Throwable exception = this.exception;

		if (exception.getMessage() == null) {
			if (exception instanceof NullPointerException) {
				exception = copyProperties(exception, new NullPointerException(this.title));
			} else if (exception instanceof StackOverflowError) {
				exception = copyProperties(exception, new StackOverflowError(this.title));
			} else if (exception instanceof OutOfMemoryError) {
				exception = copyProperties(exception, new OutOfMemoryError(this.title));
			}
		}

		StringWriter writer = new StringWriter();

		try (PrintWriter printer = new PrintWriter(writer)) {
			exception.printStackTrace(new PrintWriter(writer));
			return writer.toString();
		}
	}

	private static Throwable copyProperties(Throwable original, Throwable copy) {
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
		sb.append(this.title);
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
