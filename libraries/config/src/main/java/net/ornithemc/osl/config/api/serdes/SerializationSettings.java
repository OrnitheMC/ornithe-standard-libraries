package net.ornithemc.osl.config.api.serdes;

public final class SerializationSettings {

	public final boolean skipConfigMetadata;
	public final boolean skipOptionGroupMetadata;
	public final boolean skipOptionMetadata;
	public final boolean skipDefaultOptions;

	private SerializationSettings(boolean skipConfigMetadata, boolean skipOptionGroupMetadata, boolean skipOptionMetadata,
	                              boolean skipDefaultOptions) {
		this.skipConfigMetadata = skipConfigMetadata;
		this.skipOptionGroupMetadata = skipOptionGroupMetadata;
		this.skipOptionMetadata = skipOptionMetadata;
		this.skipDefaultOptions = skipDefaultOptions;
	}

	public static SerializationSettings forFile() {
		return new Builder().build();
	}

	public static SerializationSettings forNetwork() {
		return new Builder().skipMetadata().skipDefaultOptions().build();
	}

	public static final class Builder {

		private boolean skipConfigMetadata;
		private boolean skipOptionGroupMetadata;
		private boolean skipOptionMetadata;
		private boolean skipDefaultOptions;

		public Builder skipConfigMetadata() {
			skipConfigMetadata = true;
			return this;
		}

		public Builder skipOptionGroupMetadata() {
			skipOptionGroupMetadata = true;
			return this;
		}

		public Builder skipOptionMetadata() {
			skipOptionMetadata = true;
			return this;
		}

		public Builder skipMetadata() {
			return skipConfigMetadata().skipOptionGroupMetadata().skipOptionMetadata();
		}

		public Builder skipDefaultOptions() {
			skipDefaultOptions = true;
			return this;
		}

		public SerializationSettings build() {
			return new SerializationSettings(skipConfigMetadata, skipOptionGroupMetadata, skipOptionMetadata, skipDefaultOptions);
		}
	}
}
