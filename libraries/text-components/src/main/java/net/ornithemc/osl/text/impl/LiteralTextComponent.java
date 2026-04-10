package net.ornithemc.osl.text.impl;

public class LiteralTextComponent extends BaseTextComponent {

	private final String value;

	public LiteralTextComponent(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	void buildString(StringBuilder sb, boolean formatted) {
		if (formatted) {
			this.style.apply(sb);
		}

		sb.append(this.value);
	}
}
