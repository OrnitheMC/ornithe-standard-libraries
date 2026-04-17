package net.ornithemc.osl.localization.impl.language;

import net.ornithemc.osl.localization.api.language.Language;

public class SimpleLanguage implements Language, Comparable<Language> {

	public static final Factory FACTORY = SimpleLanguage::new;

	private final String code;
	private final String region;
	private final String name;
	private final boolean bidirectional;

	public SimpleLanguage(String code, String region, String name, boolean bidirectional) {
		this.code = code;
		this.region = region;
		this.name = name;
		this.bidirectional = bidirectional;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (!(that instanceof Language)) {
			return false;
		}

		Language lang = (Language) that;
		return this.code.equals(lang.code());
	}

	@Override
	public int hashCode() {
		return this.code.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", this.name, this.region);
	}

	@Override
	public int compareTo(Language language) {
		return this.code.compareTo(language.code());
	}

	public String code() {
		return this.code;
	}

	public String name() {
		return this.name;
	}

	public String region() {
		return this.region;
	}

	public boolean bidirectional() {
		return this.bidirectional;
	}
}
