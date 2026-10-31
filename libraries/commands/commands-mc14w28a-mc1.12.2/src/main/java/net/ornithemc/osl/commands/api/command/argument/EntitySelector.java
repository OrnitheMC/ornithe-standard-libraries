package net.ornithemc.osl.commands.api.command.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.server.command.CommandSourceStack;
import net.ornithemc.osl.text.api.Formatting;
import net.ornithemc.osl.text.api.TextComponent;
import net.ornithemc.osl.text.api.TextComponents;

public class EntitySelector {

	private final int max;
	private final boolean allowNonPlayers;
	private final boolean restrictOtherWorlds;
	private final Predicate<Entity> predicate;
	private final NumberRange.Floats range;
	private final Function<Vec3d, Vec3d> position;
	private final Box bounds;
	private final BiConsumer<Vec3d, List<? extends Entity>> order;
	private final boolean allowSelf;
	private final String name;
	private final UUID uuid;
	private final Class<? extends Entity> type;
	private final boolean selector;

	public EntitySelector(int max, boolean allowNonPlayers, boolean restrictOtherWorlds, Predicate<Entity> predicate, NumberRange.Floats range, Function<Vec3d, Vec3d> position, Box bounds, BiConsumer<Vec3d, List<? extends Entity>> order, boolean allowSelf, String name, UUID uuid, Class<? extends Entity> type, boolean selector) {
		this.max = max;
		this.allowNonPlayers = allowNonPlayers;
		this.restrictOtherWorlds = restrictOtherWorlds;
		this.predicate = predicate;
		this.range = range;
		this.position = position;
		this.bounds = bounds;
		this.order = order;
		this.allowSelf = allowSelf;
		this.name = name;
		this.uuid = uuid;
		this.type = type;
		this.selector = selector;
	}

	public int getMax() {
		return this.max;
	}

	public boolean hasNonPlayers() {
		return this.allowNonPlayers;
	}

	public boolean hasSelf() {
		return this.allowSelf;
	}

	public boolean isRestrictOtherWorlds() {
		return this.restrictOtherWorlds;
	}

	private void checkPermissions(CommandSourceStack source) throws CommandSyntaxException {
		if (this.selector && !source.hasPermissions(2)) {
			throw EntityArgument.SELECTOR_NOT_ALLOWED_EXCEPTION.create();
		}
	}

	public Entity findSingleEntity(CommandSourceStack source) throws CommandSyntaxException {
		this.checkPermissions(source);

		List<? extends Entity> entities = this.findEntities(source);

		if (entities.isEmpty()) {
			throw EntityArgument.ENTITY_NOT_FOUND_EXCEPTION.create();
		}
		if (entities.size() > 1) {
			throw EntityArgument.TOO_MANY_ENTITIES_EXCEPTION.create();
		}

		return entities.get(0);
	}

	public List<? extends Entity> findEntities(CommandSourceStack source) throws CommandSyntaxException {
		this.checkPermissions(source);

		if (!this.allowNonPlayers) {
			return this.findPlayers(source);
		}

		if (this.name != null) {
			ServerPlayerEntity player = source.getServer().getPlayerManager().get(this.name);
			return player == null ? Collections.emptyList() : Collections.singletonList(player);
		}

		if (this.uuid != null) {
			for (ServerWorld world : source.getServer().worlds) {
				Entity entity = world.getEntity(this.uuid);

				if (entity != null) {
					return Collections.singletonList(entity);
				}
			}

			return Collections.emptyList();
		}

		Vec3d pos = this.position.apply(source.getPos());
		Predicate<Entity> predicate = this.getPredicate(pos);

		if (this.allowSelf) {
			return source.getEntity() != null && predicate.test(source.getEntity()) ? Collections.singletonList(source.getEntity()) : Collections.emptyList();
		} else {
			List<Entity> entities = new ArrayList<>();

			if (this.isRestrictOtherWorlds()) {
				this.collectEntities(entities, source.getWorld(), pos, predicate);
			} else {
				for (ServerWorld world : source.getServer().worlds) {
					this.collectEntities(entities, world, pos, predicate);
				}
			}

			return this.sortAndLimit(pos, entities);
		}
	}

	private void collectEntities(List<Entity> entities, ServerWorld world, Vec3d pos, Predicate<Entity> filter) {
		if (this.bounds != null) {
			entities.addAll(world.getEntitiesOfType(this.type, this.bounds.move(pos), filter::test));
		} else {
			entities.addAll(world.getEntities(this.type, filter::test));
		}
	}

	public ServerPlayerEntity findSinglePlayer(CommandSourceStack source) throws CommandSyntaxException {
		this.checkPermissions(source);

		List<ServerPlayerEntity> players = this.findPlayers(source);

		if (players.size() != 1) {
			throw EntityArgument.PLAYER_NOT_FOUND_EXCEPTION.create();
		}

		return players.get(0);
	}

	public List<ServerPlayerEntity> findPlayers(CommandSourceStack source) throws CommandSyntaxException {
		this.checkPermissions(source);

		if (this.name != null) {
			ServerPlayerEntity player = source.getServer().getPlayerManager().get(this.name);
			return player == null ? Collections.emptyList() : Collections.singletonList(player);
		}

		if (this.uuid != null) {
			ServerPlayerEntity player = source.getServer().getPlayerManager().get(this.uuid);
			return player == null ? Collections.emptyList() : Collections.singletonList(player);
		}

		Vec3d pos = this.position.apply(source.getPos());
		Predicate<Entity> predicate = this.getPredicate(pos);

		if (this.allowSelf) {
			if (source.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();

				if (predicate.test(player)) {
					return Collections.singletonList(player);
				}
			}

			return Collections.emptyList();
		} else {
			List<ServerPlayerEntity> players;

			if (this.isRestrictOtherWorlds()) {
				players = source.getWorld().getPlayers(ServerPlayerEntity.class, predicate::test);
			} else {
				players = new ArrayList<>();

				for (ServerPlayerEntity player : source.getServer().getPlayerManager().getAll()) {
					if (predicate.test(player)) {
						players.add(player);
					}
				}
			}

			return this.sortAndLimit(pos, players);
		}
	}

	private Predicate<Entity> getPredicate(Vec3d pos) {
		Predicate<Entity> predicate = this.predicate;

		if (this.bounds != null) {
			Box bounds = this.bounds.move(pos);
			predicate = predicate.and(entity -> bounds.intersects(entity.getShape()));
		}

		if (!this.range.isAny()) {
			predicate = predicate.and(entity -> this.range.matchesSquare(entity.squaredDistanceTo(pos.x, pos.y, pos.z)));
		}

		return predicate;
	}

	private <T extends Entity> List<T> sortAndLimit(Vec3d pos, List<T> entities) {
		if (entities.size() > 1) {
			this.order.accept(pos, entities);
		}

		return entities.subList(0, Math.min(this.max, entities.size()));
	}

	public static TextComponent joinNames(List<? extends Entity> entities) {
		if (entities.isEmpty()) {
			return TextComponents.literal("");
		}
		if (entities.size() == 1) {
			return TextComponents.resolve(entities.get(0).getDisplayName());
		}

		TextComponent text = TextComponents.literal("");
		boolean addSeparator = false;

		for (int i = 0; i < entities.size(); i++) {
			if (addSeparator) {
				text.append(TextComponents.literal(", ").format(Formatting.GRAY));
			}

			text.append(TextComponents.resolve(entities.get(i).getDisplayName()));
			addSeparator = true;
		}

		return text;
	}
}
