package net.ornithemc.osl.crashreports.api;

import java.util.function.Consumer;

import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.impl.CrashExceptionsImpl;

/**
 * Utility methods for constructing crash exceptions.
 */
public final class CrashExceptions {

	/**
	 * Constructs a crash exception with a crash report that is empty
	 * exception for system details.
	 * 
	 * @param title the title of the crash report.
	 * @param error the error that caused the crash.
	 * @return a new crash exception
	 */
	public static RuntimeException from(String title, Throwable error) {
		return CrashExceptionsImpl.from(title, error);
	}

	/**
	 * Constructs a crash exception with a crash report that is populated
	 * by the given builder.
	 * 
	 * @param title   the title of the crash report.
	 * @param error   the error that caused the crash.
	 * @param builder the builder that populates the crash report.
	 * @return a new crash exception.
	 */
	public static RuntimeException from(String title, Throwable error, Consumer<CrashReport> builder) {
		return CrashExceptionsImpl.from(title, error, builder);
	}
}
