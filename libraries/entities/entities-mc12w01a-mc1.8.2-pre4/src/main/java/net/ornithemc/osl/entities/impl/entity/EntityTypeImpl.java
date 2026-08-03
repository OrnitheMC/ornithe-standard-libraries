package net.ornithemc.osl.entities.impl.entity;

import java.lang.reflect.Constructor;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import net.ornithemc.osl.entities.api.EntityTypeRegistry;
import net.ornithemc.osl.entities.api.entity.EntityType;

public final class EntityTypeImpl<T extends Entity> implements EntityType<T> {

	private final Class<? extends T> type;
	private final Function<? super World, ? extends T> factory;

	private String key;

	private EntityTypeImpl(Class<? extends T> type, Function<? super World, ? extends T> factory) {
		this.type = type;
		this.factory = factory;
	}

	@Override
	public Class<? extends T> getType() {
		return this.type;
	}

	@Override
	public String getTranslationKey() {
		if (this.key == null) {
			this.key = "entity." + EntityTypeRegistry.getLegacyId(this.type);
		}

		return this.key;
	}

	@Override
	public T create(World world) {
		return this.factory.apply(world);
	}

	public static final class Builder<T extends Entity> implements EntityType.Builder<T> {

		public static <T extends Entity> EntityType.Builder<T> of(Class<? extends T> type) {
			Constructor<? extends T> constructor = null;

			try {
				constructor = type.getConstructor(World.class);
			} catch (NoSuchMethodException e) {
			}

			Constructor<? extends T> method = constructor;
			boolean instantiable = (constructor != null);

			Function<? super World, ? extends T> factory = world -> {
				if (instantiable) {
					try {
						return method.newInstance(world);
					} catch (Throwable t) {
						throw new IllegalStateException("error creating entity of type " + type, t);
					}
				} else {
					throw new UnsupportedOperationException();
				}
			};

			return new Builder<>(type, factory);
		}

		public static <T extends Entity> EntityType.Builder<T> of(Class<? extends T> type, Function<? super World, ? extends T> factory) {
			return new Builder<>(type, factory);
		}

		private final Class<? extends T> type;
		private final Function<? super World, ? extends T> factory;

		private Builder(Class<? extends T> type, Function<? super World, ? extends T> factory) {
			this.type = type;
			this.factory = factory;
		}

		@Override
		public EntityType<T> build() {
			return new EntityTypeImpl<>(this.type, this.factory);
		}
	}
}
