package net.ornithemc.osl.core.api.json;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

import org.quiltmc.json5.JsonReader;
import org.quiltmc.json5.JsonToken;
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

	private JsonWriter writer;
	private JsonReader reader;

	public JsonFile(Path path) {
		this.path = path;
	}

	/**
	 * Creates a json writer and starts writing to this json file.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		if (canOpen()) {
			writer = JsonWriter.json(path);
			writer.beginObject(); // begin document
		}
	}

	/**
	 * Creates a json reader and starts reading from this json file.
	 * 
	 * @throws IOException
	 */
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

	/**
	 * {@link org.quiltmc.json5.JsonWriter#name(String)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeName(String name) throws IOException {
		writer().name(name);
		return this;
	}

	/**
	 * Wraps write operations between calls to
	 * {@link org.quiltmc.json5.JsonWriter#beginObject()}
	 * and
	 * {@link org.quiltmc.json5.JsonWriter#endObject()}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeObject(JsonFileConsumer object) throws IOException {
		writer().beginObject();
		object.accept(this);
		writer().endObject();
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeObject(object)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeObject(String name, JsonFileConsumer object) throws IOException {
		return writeName(name).writeObject(object);
	}

	/**
	 * Wraps write operations between calls to
	 * {@link org.quiltmc.json5.JsonWriter#beginArray()}
	 * and
	 * {@link org.quiltmc.json5.JsonWriter#endArray()}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeArray(JsonFileConsumer... elements) throws IOException {
		writer().beginArray();
		for (JsonFileConsumer element : elements) {
			element.accept(this);
		}
		writer().endArray();
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeArray(elements)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeArray(String name, JsonFileConsumer... elements) throws IOException {
		return writeName(name).writeArray(elements);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#value(String)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeString(String value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeString(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeString(String name, String value) throws IOException {
		return writeName(name).writeString(value);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#value(boolean)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeBoolean(boolean value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeBoolean(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeBoolean(String name, boolean value) throws IOException {
		return writeName(name).writeBoolean(value);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#value(Boolean)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeBoolean(Boolean value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeBoolean(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeBoolean(String name, Boolean value) throws IOException {
		return writeName(name).writeBoolean(value);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#value(Number)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeNumber(Number value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeNumber(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeNumber(String name, Number value) throws IOException {
		return writeName(name).writeNumber(value);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#value(double)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeDouble(double value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeDouble(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeDouble(String name, double value) throws IOException {
		return writeName(name).writeDouble(value);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#value(long)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeLong(long value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeLong(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeLong(String name, long value) throws IOException {
		return writeName(name).writeLong(value);
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#nullValue()}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeNull() throws IOException {
		writer().nullValue();
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeNull()}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeNull(String name) throws IOException {
		return writeName(name).writeNull();
	}

	/**
	 * {@link org.quiltmc.json5.JsonWriter#jsonValue(String)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeJson(String value) throws IOException {
		writer().value(value);
		return this;
	}

	/**
	 * shorthand for {@code json.writeName(name).writeJson(value)}
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile writeJson(String name, String value) throws IOException {
		return writeName(name).writeJson(value);
	}

	private JsonWriter writer() throws IOException {
		if (writer == null) {
			throw new IOException("not writing!");
		}

		return writer;
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#hasNext()}
	 * 
	 * @throws IOException
	 */
	public boolean hasNext() throws IOException {
		return reader().hasNext();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#peek()}
	 * 
	 * @throws IOException
	 */
	public JsonToken peek() throws IOException {
		return reader().peek();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextName()}
	 * 
	 * @throws IOException
	 */
	public String readName() throws IOException {
		return reader().nextName();
	}

	/**
	 * Reads the next property's name and throws an
	 * exception if it is not equal to the given name.
	 * 
	 * @return this json file
	 * @throws IOException
	 */
	public JsonFile readName(String name) throws IOException {
		String nextName = readName();

		if (!name.equals(nextName)) {
			throw new IOException("expected name \'" + name + "\' but found \'" + nextName);
		}

		return this;
	}

	/**
	 * Wraps read operations between calls to
	 * {@link org.quiltmc.json5.JsonReader#beginObject()}
	 * and
	 * {@link org.quiltmc.json5.JsonReader#endObject()}
	 * 
	 * @throws IOException
	 */
	public void readObject(JsonFileConsumer object) throws IOException {
		reader().beginObject();
		object.accept(this);
		reader().endObject();
	}

	/**
	 * shorthand for {@code json.readName(name).readObject(object)}
	 * 
	 * @throws IOException
	 */
	public void readObject(String name, JsonFileConsumer object) throws IOException {
		readName(name).readObject(object);
	}

	/**
	 * Wraps read operations between calls to
	 * {@link org.quiltmc.json5.JsonReader#beginArray()}
	 * and
	 * {@link org.quiltmc.json5.JsonReader#endArray()}
	 * 
	 * @throws IOException
	 */
	public void readArray(JsonFileConsumer... elements) throws IOException {
		reader().beginArray();
		for (JsonFileConsumer element : elements) {
			element.accept(this);
		}
		reader().endArray();
	}

	/**
	 * shorthand for {@code json.readName(name).readArray(elements)}
	 * 
	 * @throws IOException
	 */
	public void readArray(String name, JsonFileConsumer... elements) throws IOException {
		readName(name).readArray(elements);
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextString()}
	 * 
	 * @throws IOException
	 */
	public String readString() throws IOException {
		return reader().nextString();
	}

	/**
	 * shorthand for {@code json.readName(name).readString()}
	 * 
	 * @throws IOException
	 */
	public String readString(String name) throws IOException {
		return readName(name).readString();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextBoolean()}
	 * 
	 * @throws IOException
	 */
	public boolean readBoolean() throws IOException {
		return reader().nextBoolean();
	}

	/**
	 * shorthand for {@code json.readName(name).readBoolean()}
	 * 
	 * @throws IOException
	 */
	public boolean readBoolean(String name) throws IOException {
		return readName(name).readBoolean();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextNumber()}
	 * 
	 * @throws IOException
	 */
	public Number readNumber() throws IOException {
		return reader().nextNumber();
	}

	/**
	 * shorthand for {@code json.readName(name).readNumber()}
	 * 
	 * @throws IOException
	 */
	public Number readNumber(String name) throws IOException {
		return readName(name).readNumber();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextDouble()}
	 * 
	 * @throws IOException
	 */
	public double readDouble() throws IOException {
		return reader().nextDouble();
	}

	/**
	 * shorthand for {@code json.readName(name).readDouble()}
	 * 
	 * @throws IOException
	 */
	public double readDouble(String name) throws IOException {
		return readName(name).readDouble();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextLong()}
	 * 
	 * @throws IOException
	 */
	public long readLong() throws IOException {
		return reader().nextLong();
	}

	/**
	 * shorthand for {@code json.readName(name).readLong()}
	 * 
	 * @throws IOException
	 */
	public long readLong(String name) throws IOException {
		return readName(name).readLong();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextInt()}
	 * 
	 * @throws IOException
	 */
	public int readInt() throws IOException {
		return reader().nextInt();
	}

	/**
	 * shorthand for {@code json.readName(name).readInt()}
	 * 
	 * @throws IOException
	 */
	public int readInt(String name) throws IOException {
		return readName(name).readInt();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#nextNull()}
	 * 
	 * @throws IOException
	 */
	public void readNull() throws IOException {
		reader().nextNull();
	}

	/**
	 * shorthand for {@code json.readName(name).readNull()}
	 * 
	 * @throws IOException
	 */
	public void readNull(String name) throws IOException {
		readName(name).readNull();
	}

	/**
	 * {@link org.quiltmc.json5.JsonReader#skipValue()}
	 * 
	 * @throws IOException
	 */
	public void skipValue() throws IOException {
		reader().skipValue();
	}

	private JsonReader reader() throws IOException {
		if (reader == null) {
			throw new IOException("not reading!");
		}

		return reader;
	}

	@FunctionalInterface
	public interface JsonFileConsumer {

		void accept(JsonFile json) throws IOException;

	}
}
