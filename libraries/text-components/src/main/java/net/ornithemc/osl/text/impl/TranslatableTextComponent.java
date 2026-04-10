package net.ornithemc.osl.text.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class TranslatableTextComponent extends BaseTextComponent {

	private static final Pattern ARG_FORMAT = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
	private static final Locale LOCALE = Locale.find();

	private final String key;
	private final Object[] args;

	private long resolvedTime;
	private List<TextComponent> resolved;

	public TranslatableTextComponent(String key, Object... args) {
		this.key = key;
		this.args = args;
	}

	public String getKey() {
		return this.key;
	}

	public Object[] getArgs() {
		return this.args;
	}

	@Override
	void buildString(StringBuilder sb, boolean formatted) {
		long localeUpdateTime = LOCALE.getLastUpdateTime();

		if (this.resolved == null || this.resolvedTime != localeUpdateTime) {
			this.resolve(localeUpdateTime);
		}

		this.buildString(sb, formatted, this.resolved);
	}

	private void resolve(long time) {
		this.resolvedTime = time;
		this.resolved = new ArrayList<>();

		String translation = LOCALE.get(this.key);
		Matcher matcher = ARG_FORMAT.matcher(translation);

		int nextArgIndex = 0;
		int nextCharIndex = 0;

		while (matcher.find(nextCharIndex)) {
			int matchStart = matcher.start();
			int matchEnd = matcher.end();

			if (matchStart > nextCharIndex) {
				this.resolved.add(TextComponents.literal(translation.substring(nextCharIndex, matchStart)));
			}

			String formatting = matcher.group(2);
			String partWithFormatting = translation.substring(matchStart, matchEnd);

			if ("%".equals(formatting) && "%%".equals(partWithFormatting)) {
				this.resolved.add(TextComponents.literal("%"));
			} else {
				if (!"s".equals(formatting)) {
					throw new IllegalStateException("Unsupported format: '" + partWithFormatting + "'");
				}

				String matchedArgIndex = matcher.group(1);
				int argIndex = matchedArgIndex != null ? Integer.parseInt(matchedArgIndex) - 1 : nextArgIndex++;

				if (argIndex < this.args.length) {
					this.resolved.add(this.resolveArg(argIndex));
				}
			}

			nextCharIndex = matchEnd;
		}

		if (nextCharIndex < translation.length()) {
			this.resolved.add(TextComponents.literal(translation.substring(nextCharIndex)));
		}
	}

	private TextComponent resolveArg(int index) {
		if (index >= this.args.length) {
			throw new IndexOutOfBoundsException("Index out of range: " + index);
		}

		Object arg = this.args[index];

		if (arg == null) {
			return TextComponents.literal("null");
		}

		return TextComponents.resolve(arg);
	}
}
