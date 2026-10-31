package net.ornithemc.osl.crashreports.impl.crash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;

import net.ornithemc.osl.crashreports.api.crash.CrashReportDetail;

public class CrashReportSectionImpl implements ModifiableCrashReportSection {

	private final String name;

	private final List<Entry> entries;
	private final Object2ReferenceMap<String, Entry> entriesByKey;

	private StackTraceElement[] stackTrace;

	public CrashReportSectionImpl(String name) {
		this.name = name;

		this.entries = new ArrayList<>();
		this.entriesByKey = new Object2ReferenceOpenHashMap<>();

		this.stackTrace = new StackTraceElement[0];
	}

	@Override
	public void addDetail(String key, CrashReportDetail<String> detail) {
		try {
			this.addDetail(key, detail.call());
		} catch (Throwable t) {
			this.addDetailError(key, t);
		}
	}

	@Override
	public void addDetail(String key, Object detail) {
		Entry entry = new Entry(key, detail);

		this.entries.add(entry);
		this.entriesByKey.put(key, entry);
	}

	@Override
	public void addDetailError(String key, Throwable error) {
		this.addDetail(key, error);
	}

	@Override
	public void removeDetail(String key) {
		Entry entry = this.entriesByKey.remove(key);

		if (entry != null) {
			this.entries.remove(entry);
		}
	}

	@Override
	public void replaceDetail(String oldKey, String newKey, CrashReportDetail<String> detail) {
		Entry entry = this.entriesByKey.remove(oldKey);

		if (entry != null) {
			int index = this.entries.indexOf(entry);

			this.addDetail(newKey, detail); // also updates the map
			Collections.swap(this.entries, index, this.entries.size() - 1);
			this.entries.remove(this.entries.size() - 1);
		}
	}

	public int fillStackTrace(int stackTraceOffset) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		this.stackTrace = new StackTraceElement[stackTrace.length - 3 - stackTraceOffset];
		System.arraycopy(stackTrace, 3 + stackTraceOffset, this.stackTrace, 0, this.stackTrace.length);
		return this.stackTrace.length;
	}

	public boolean validateStackTrace(StackTraceElement source, StackTraceElement next) {
		if (this.stackTrace.length != 0 && source != null) {
			StackTraceElement current = this.stackTrace[0];

			if (stackTraceElementsMatch(current, source)) {
				if ((next != null) != (this.stackTrace.length > 1)) {
					return false;
				}

				if (next != null && !this.stackTrace[1].equals(next)) {
					return false;
				}

				this.stackTrace[0] = source;

				return true;
			}
		}

		return false;
	}

	private static boolean stackTraceElementsMatch(StackTraceElement first, StackTraceElement second) {
		return first.isNativeMethod() == second.isNativeMethod()
			&& first.getClassName().equals(second.getClassName())
			&& first.getFileName().equals(second.getFileName())
			&& first.getMethodName().equals(second.getMethodName());
	}

	public void buildString(StringBuilder sb) {
		sb.append("-- ");
		sb.append(this.name);
		sb.append(" --");

		sb.append("\nDetails:");

		for (Entry entry : this.entries) {
			sb.append("\n\t");
			sb.append(entry.getKey());
			sb.append(": ");
			sb.append(entry.getValue());
		}

		if (this.stackTrace != null && this.stackTrace.length > 0) {
			sb.append("\nStacktrace:");

			for (StackTraceElement element : this.stackTrace) {
				sb.append("\n\tat ");
				sb.append(element.toString());
			}
		}
	}

	private static class Entry {

		private final String key;
		private final String value;

		public Entry(String key, Object value) {
			this.key = key;

			if (value == null) {
				this.value = "~~NULL~~";
			} else if (value instanceof Throwable) {
				Throwable exception = (Throwable) value;
				this.value = "~~ERROR~~ " + exception.getClass().getSimpleName() + ": " + exception.getMessage();
			} else {
				this.value = value.toString();
			}
		}

		public String getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}
	}
}
