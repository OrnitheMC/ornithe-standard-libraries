package net.ornithemc.osl.branding.api;

import java.util.List;

/**
 * The operation that is applied to the branding string.
 */
public enum Operation {

	/**
	 * Add a new value to the beginning of the branding string.
	 */
	PREPEND {

		@Override
		public int apply(List<String> buffer, int original, String value) {
			buffer.add(0, value);
			return original + 1;
		}
	},
	/**
	 * Add a new value to the end of the branding string.
	 */
	APPEND {

		@Override
		public int apply(List<String> buffer, int original, String value) {
			buffer.add(value);
			return original;
		}
	},
	/**
	 * Replace the original part of the branding string with a new value.
	 */
	REPLACE {

		@Override
		public int apply(List<String> buffer, int original, String value) {
			buffer.set(original, value);
			return original;
		}
	},
	/**
	 * Replace the entire branding string with a new value.
	 */
	SET {

		@Override
		public int apply(List<String> buffer, int original, String value) {
			String o = buffer.get(original);
			buffer.clear();
			buffer.add(o);
			return 0;
		}
	};

	/**
	 * Applies this operation to the branding string.<br>
	 * The buffer contains each component of the branding modifier. Each component
	 * can add, remove, or modify the buffer, depending on the chosen operation.
	 * Once all components have been applied, the string is built from this buffer.
	 * 
	 * @param buffer   the buffer containing all components of the branding string
	 * @param original the index of the original part of the branding string
	 * @param value    the value to be applied to the branding string
	 * @return         the new index of the original part of the branding string
	 */
	public int apply(List<String> buffer, int original, String value) {
		return original;
	}
}
