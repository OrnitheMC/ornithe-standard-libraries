package net.ornithemc.osl.branding.api;

public enum BrandingModifier {

	PREPEND() {

		@Override
		public String apply(String original, String modified, String value) {
			return value + modified;
		}
	},
	APPEND() {

		@Override
		public String apply(String original, String modified, String value) {
			return modified + value;
		}
	},
	REPLACE() {

		@Override
		public String apply(String original, String modified, String value) {
			return value;
		}
	};

	public String apply(String original, String modified, String value) {
		return modified;
	}

}
