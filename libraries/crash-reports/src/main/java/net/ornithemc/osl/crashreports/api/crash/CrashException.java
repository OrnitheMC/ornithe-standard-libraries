package net.ornithemc.osl.crashreports.api.crash;

@SuppressWarnings("serial")
public class CrashException extends RuntimeException {

	private final CrashReport report;

	public CrashException(CrashReport report) {
		this.report = report;
	}

	@Override
	public String getMessage() {
		return this.report.getTitle();
	}

	@Override
	public Throwable getCause() {
		return this.report.getError();
	}

	public CrashReport getReport() {
		return this.report;
	}
}
