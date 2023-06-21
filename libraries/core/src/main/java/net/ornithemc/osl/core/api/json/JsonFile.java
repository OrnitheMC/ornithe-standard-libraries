package net.ornithemc.osl.core.api.json;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

import org.quiltmc.json5.JsonReader;
import org.quiltmc.json5.JsonWriter;

/**
 * A wrapper for quilt-json5's
 * {@linkplain org.quiltmc.json5.JsonWriter JsonWriter}
 * and
 * {@linkplain org.quiltmc.json5.JsonReader JsonReader}
 * classes.
 * 
 * @author Space Walker
 */
public class JsonFile implements Closeable {

	private final Path path;

	// TODO: wrap write/read methods instead of making the writer and reader public
	public JsonWriter writer;
	public JsonReader reader;

	public JsonFile(Path path) {
		this.path = path;
	}

	public void write() throws IOException {
		if (canOpen()) {
			writer = JsonWriter.json(path);
			writer.beginObject(); // begin document
		}
	}

	public void read() throws IOException {
		if (canOpen()) {
			reader = JsonReader.json(path);
			reader.beginObject(); // begin document
		}
	}

	private boolean canOpen() throws IOException {
		if (writer != null) {
			throw new IOException("already writing!");
		} else if (reader != null) {
			throw new IOException("already reading!");
		}

		return true;
	}

	@Override
	public void close() throws IOException {
		if (writer != null) {
			writer.endObject(); // end document
			writer.close();
			writer = null;
		} else if (reader != null) {
			reader.endObject(); // end document
			reader.close();
			reader = null;
		}
	}
}
