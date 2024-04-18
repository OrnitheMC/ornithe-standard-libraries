package net.ornithemc.osl.config.api.serdes;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

public class NbtType<T extends NbtElement> {

	public static final NbtType<NbtEnd>       NULL       = new NbtType<>(0);
	public static final NbtType<NbtByte>      BYTE       = new NbtType<>(1);
	public static final NbtType<NbtShort>     SHORT      = new NbtType<>(2);
	public static final NbtType<NbtInt>       INT        = new NbtType<>(3);
	public static final NbtType<NbtLong>      LONG       = new NbtType<>(4);
	public static final NbtType<NbtFloat>     FLOAT      = new NbtType<>(5);
	public static final NbtType<NbtDouble>    DOUBLE     = new NbtType<>(6);
	public static final NbtType<NbtByteArray> BYTE_ARRAY = new NbtType<>(7);
	public static final NbtType<NbtString>    STRING     = new NbtType<>(8);
	public static final NbtType<NbtList>      LIST       = new NbtType<>(9);
	public static final NbtType<NbtCompound>  COMPOUND   = new NbtType<>(10);
	public static final NbtType<NbtIntArray>  INT_ARRAY  = new NbtType<>(11);
	public static final NbtType<NbtLongArray> LONG_ARRAY = new NbtType<>(12);

	private final int type;

	private NbtType(int type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public T cast(NbtElement nbt) {
		if (nbt.getType() != type) {
			throw new ClassCastException("expected nbt type \'" + NbtElement.getName(type) + "\' but found \'" + NbtElement.getName(nbt.getType()) + "\'");
		}

		return (T)nbt;
	}
}
