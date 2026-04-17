package net.ornithemc.osl.localization.api.language;

public interface Language {

	String code();

	String name();

	String region();

	boolean bidirectional();

	interface Factory {

		Language create(String code, String name, String region, boolean bidirectional);

	}
}
