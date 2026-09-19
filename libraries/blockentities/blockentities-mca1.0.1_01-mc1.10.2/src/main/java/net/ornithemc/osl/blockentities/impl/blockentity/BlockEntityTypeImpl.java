package net.ornithemc.osl.blockentities.impl.blockentity;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import net.minecraft.block.entity.BlockEntity;

import net.ornithemc.osl.blockentities.api.blockentity.BlockEntityType;

public final class BlockEntityTypeImpl<T extends BlockEntity> implements BlockEntityType<T> {

	private final Class<? extends T> type;
	private final Supplier<? extends T> factory;

	private BlockEntityTypeImpl(Class<? extends T> type, Supplier<? extends T> factory) {
		this.type = type;
		this.factory = factory;
	}

	@Override
	public Class<? extends T> getType() {
		return this.type;
	}

	@Override
	public T create() {
		return this.factory.get();
	}

	public static final class Builder<T extends BlockEntity> implements BlockEntityType.Builder<T> {

		public static <T extends BlockEntity> BlockEntityType.Builder<T> of(Class<? extends T> type) {
			Constructor<? extends T> constructor;

			try {
				constructor = type.getConstructor();
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Invalid class " + type + ": no constructor taking no arguments");
			}

			Supplier<? extends T> factory = () -> {
				try {
					return constructor.newInstance();
				} catch (Throwable t) {
					throw new IllegalStateException("error creating block entity of type " + type, t);
				}
			};

			return new Builder<>(type, factory);
		}

		public static <T extends BlockEntity> BlockEntityType.Builder<T> of(Class<? extends T> type, Supplier<? extends T> factory) {
			return new Builder<>(type, factory);
		}

		private final Class<? extends T> type;
		private final Supplier<? extends T> factory;

		private Builder(Class<? extends T> type, Supplier<? extends T> factory) {
			this.type = type;
			this.factory = factory;
		}

		@Override
		public BlockEntityType<T> build() {
			return new BlockEntityTypeImpl<>(this.type, this.factory);
		}
	}
}
