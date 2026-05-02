package net.ornithemc.osl.text.api;

import java.util.Locale;
import java.util.Objects;

public class HoverEvent {

	private final Action action;
	private final Object value;

	public HoverEvent(Action action, Object value) {
		this.action = action;
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof HoverEvent)) {
			return false;
		}
		HoverEvent event = (HoverEvent) o;
		return this.action == event.action && Objects.equals(this.value, event.value);
	}

	public Action getAction() {
		return this.action;
	}

	public <T> T getValue() {
		return (T) this.value;
	}

	public static HoverEvent showText(TextComponent text) {
		return new HoverEvent(Action.SHOW_TEXT, text);
	}

	public enum Action {

		SHOW_TEXT;

		public String getName() {
			return this.name().toLowerCase(Locale.ROOT);
		}

		public static Action byName(String name) {
			return Action.valueOf(name.toUpperCase());
		}
	}
}
