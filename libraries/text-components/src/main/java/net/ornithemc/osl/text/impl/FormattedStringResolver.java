package net.ornithemc.osl.text.impl;

import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.Style;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class FormattedStringResolver implements TextResolver<String> {

	@Override
	public TextComponent resolve(String fs) {
		TextComponent t = TextComponents.literal("");
		Style s = Style.EMPTY;

		int from = 0;
		int to = 0;

		boolean lastWasFormattingPrefix = false;
		boolean lastWasFormatting = false;

		for (; to < fs.length(); to++) {
			char chr = fs.charAt(to);

			boolean thisIsFormattingPrefix = (chr == Formatting.PREFIX);
			boolean thisIsStringEnd = (to == fs.length() - 1);

			if (lastWasFormattingPrefix) {
				Formatting f = Formatting.byCode(chr);

				if (f == Formatting.RESET) {
					s = Style.EMPTY;
				} else {
					s = s.withFormatting(f);
				}

				// make sure current char is not captured
				if (thisIsStringEnd) {
					from = to;
				}

				lastWasFormattingPrefix = false;
				lastWasFormatting = true;
			} else if (thisIsFormattingPrefix) {
				lastWasFormattingPrefix = true;
				lastWasFormatting = false;
			} else {
				if (lastWasFormatting) {
					from = to;
				}
				// make sure current char is captured
				if (thisIsStringEnd) {
					to++;
				}

				lastWasFormattingPrefix = false;
				lastWasFormatting = false;
			}

			if (thisIsFormattingPrefix || thisIsStringEnd) {
				if (from != to) {
					t.append(TextComponents.literal(fs.substring(from, to)).format(s));
				}

				from = to;
			}
		}

		return t;
	}
}
