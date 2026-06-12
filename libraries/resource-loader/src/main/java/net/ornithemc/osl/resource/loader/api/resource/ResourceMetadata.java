package net.ornithemc.osl.resource.loader.api.resource;

import java.io.InputStream;

import com.google.gson.JsonObject;

import net.ornithemc.osl.core.api.util.function.IOSupplier;
import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata.Section.Serializer;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.ResourceMetadataSection;

/**
 * Metadata of a {@linkplain Resource}
 */
public interface ResourceMetadata {

	/**
	 * The file extension for resource metadata files.
	 */
	String FILE_EXTENSION = ".mcmeta";

	ResourceMetadata EMPTY = new ResourceMetadata() {

		@Override
		public <T> T getSection(String name, Serializer<T> serializer) {
			return null;
		}
	};
	IOSupplier<ResourceMetadata> EMPTY_SUPPLIER = () -> EMPTY;

	/**
	 * @return a supplier for the resource metadata parsed from the given {@linkplain InputStream}.
	 */
	static IOSupplier<ResourceMetadata> supplier(IOSupplier<InputStream> inputStream) {
		return () -> JsonResourceMetadata.fromInputStream(inputStream);
	}

	/**
	 * Parses the resource metadata section of the given name with the given serializer.
	 * 
	 * @return the resource metadata section of the given type.
	 */
	default <T> T getSection(Section<T> section) {
		return this.getSection(section.name(), section.serializer());
	}

	/**
	 * Parses the resource metadata section of the given name with the given serializer.
	 * 
	 * @return the resource metadata section of the given type.
	 */
	<T> T getSection(String name, Section.Serializer<T> serializer);

	/**
	 * A section type of resource metadata.
	 */
	interface Section<T> {

		/**
		 * @return the constructed section type with the given name and serializer.
		 */
		static <T> Section<T> of(String name, Serializer<T> serializer) {
			return new ResourceMetadataSection<>(name, serializer);
		}

		/**
		 * @return the name of this section.
		 */
		String name();

		/**
		 * @return the serializer of this section.
		 */
		Serializer<T> serializer();

		/**
		 * A JSON serializer for a resource metadata section.
		 */
		interface Serializer<T> {

			/**
			 * @return the resource metadata section parsed from the given JSON object.
			 */
			T deserialize(JsonObject json);

		}
	}
}
