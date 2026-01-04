package net.ornithemc.osl.config.api.serdes;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

public class NbtType<T extends NbtElement> {

	private static final NbtType<?>[] BY_TYPE = new NbtType[12];

	public static NbtType<?> get(int type) {
		return BY_TYPE[type];
	}

	public static final NbtType<NbtEnd>       NULL       = new NbtType<>(0 , "end");
	public static final NbtType<NbtByte>      BYTE       = new NbtType<>(1 , "byte");
	public static final NbtType<NbtShort>     SHORT      = new NbtType<>(2 , "short");
	public static final NbtType<NbtInt>       INT        = new NbtType<>(3 , "int");
	public static final NbtType<NbtLong>      LONG       = new NbtType<>(4 , "long");
	public static final NbtType<NbtFloat>     FLOAT      = new NbtType<>(5 , "float");
	public static final NbtType<NbtDouble>    DOUBLE     = new NbtType<>(6 , "double");
	public static final NbtType<NbtByteArray> BYTE_ARRAY = new NbtType<>(7 , "byte array");
	public static final NbtType<NbtString>    STRING     = new NbtType<>(8 , "string");
	public static final NbtType<NbtList>      LIST       = new NbtType<>(9 , "list");
	public static final NbtType<NbtCompound>  COMPOUND   = new NbtType<>(10, "compound");

	private final int type;
	private final String name;

	private NbtType(int type, String name) {
		BY_TYPE[type] = this;

		this.type = type;
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public T cast(NbtElement nbt) {
		if (nbt.getType() != type) {
			throw new ClassCastException("expected nbt type \'" + name + "\' but found \'" + BY_TYPE[nbt.getType()].name + "\'");
		}

		return (T)nbt;
	}
}
