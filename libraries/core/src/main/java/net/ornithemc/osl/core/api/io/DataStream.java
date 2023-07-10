package net.ornithemc.osl.core.api.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A wrapper for the
 * {@linkplain java.io.DataOutputStream DataOutputStream}
 * and
 * {@linkplain java.io.DataInputStream DataInputStream}
 * classes.
 * 
 * @author Space Walker
 */
public class DataStream implements DataOutput, DataInput {

	public static DataStream output(OutputStream output) {
		return wrapOutput(new DataOutputStream(output));
	}

	public static DataStream input(InputStream input) {
		return wrapInput(new DataInputStream(input));
	}

	public static DataStream wrapOutput(DataOutputStream output) {
		return new DataStream(output, null);
	}

	public static DataStream wrapInput(DataInputStream input) {
		return new DataStream(null, input);
	}

	private final DataOutputStream output;
	private final DataInputStream input;

	private DataStream(DataOutputStream output, DataInputStream input) {
		this.output = output;
		this.input = input;
	}

	@Override
	public void write(int b) throws IOException {
		output().write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		output().write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		output().write(b, off, len);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		output().writeBoolean(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		output().writeByte(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		output().writeShort(v);
	}

	@Override
	public void writeChar(int v) throws IOException {
		output().writeChar(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		output().writeInt(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		output().writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		output().writeFloat(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		output().writeDouble(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		output().writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		output().writeChars(s);
	}

	@Override
	public void writeUTF(String s) throws IOException {
		output().writeUTF(s);
	}

	private DataOutput output() throws IOException {
		if (output == null) {
			throw new IOException("not writing!");
		}

		return output;
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		input().readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		input().readFully(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return input().skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return input().readBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return input().readByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return input().readUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return input().readShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return input().readUnsignedShort();
	}

	@Override
	public char readChar() throws IOException {
		return input().readChar();
	}

	@Override
	public int readInt() throws IOException {
		return input().readInt();
	}

	@Override
	public long readLong() throws IOException {
		return input().readLong();
	}

	@Override
	public float readFloat() throws IOException {
		return input().readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return input().readDouble();
	}

	@Override
	public String readLine() throws IOException {
		return input().readLine();
	}

	@Override
	public String readUTF() throws IOException {
		return input().readUTF();
	}

	private DataInput input() throws IOException {
		if (input == null) {
			throw new IOException("not reading!");
		}

		return input;
	}
}
