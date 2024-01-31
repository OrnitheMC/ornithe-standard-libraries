package net.ornithemc.osl.commands.api.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.api.server.CommandSourceStack;

public class EntitySelector {

	private final int max;
	private final boolean allowNonPlayers;
	private final boolean restrictOtherWorlds;
	private final Predicate<Entity> predicate;
	private final Float minDistance;
	private final Float maxDistance;
	private final Function<Vec3d, Vec3d> position;
	private final Box bounds;
	private final BiConsumer<Vec3d, List<? extends Entity>> order;
	private final boolean allowSelf;
	private final String name;
	private final UUID uuid;
	private final Class<? extends Entity> type;
	private final boolean selector;

	public EntitySelector(int max, boolean allowNonPlayers, boolean allowFromOtherWorlds, Predicate<Entity> predicate, Float minDistance, Float maxDistance, Function<Vec3d, Vec3d> position, Box bounds, BiConsumer<Vec3d, List<? extends Entity>> order, boolean allowSelf, String name, UUID uuid, Class<? extends Entity> type, boolean selector) {
		this.max = max;
		this.allowNonPlayers = allowNonPlayers;
		this.restrictOtherWorlds = allowFromOtherWorlds;
		this.predicate = predicate;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
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

	private void checkPermissions(AbstractCommandSourceStack<?> source) throws CommandSyntaxException {
		if (this.selector && !source.hasPermissions(2)) {
			throw EntityArgument.SELECTOR_NOT_ALLOWED_EXCEPTION.create();
		}
	}

	public Entity findSingleEntity(AbstractCommandSourceStack<?> source) throws CommandSyntaxException {
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

	public List<? extends Entity> findEntities(AbstractCommandSourceStack<?> source) throws CommandSyntaxException {
		this.checkPermissions(source);
		if (!this.allowNonPlayers) {
			return this.findPlayers(source);
		}
		if (this.name != null) {
			if (source instanceof CommandSourceStack) {
				CommandSourceStack serverSource = (CommandSourceStack)source;
				PlayerEntity player = serverSource.getServer().getPlayerManager().get(this.name);
				if (player != null) {
					return Arrays.asList(player);
				}
			}
			if (source instanceof ClientCommandSourceStack) {
				ClientCommandSourceStack clientSource = (ClientCommandSourceStack)source;
				for (PlayerEntity player : clientSource.getWorld().players) {
					if (this.name.equals(player.getName())) {
						return Arrays.asList(player);
					}
				}
			}
			return Collections.emptyList();
		}
		if (this.uuid != null) {
			if (source instanceof CommandSourceStack) {
				CommandSourceStack serverSource = (CommandSourceStack)source;
				for (ServerWorld world : serverSource.getServer().worlds) {
					Entity entity = world.getEntity(this.uuid);
					if (entity != null) {
						return Arrays.asList(entity);
					}
				}
			}
			if (source instanceof ClientCommandSourceStack) {
				ClientCommandSourceStack clientSource = (ClientCommandSourceStack)source;
				for (Entity entity : clientSource.getWorld().getEntities()) {
					if (this.uuid.equals(entity.getUuid())) {
						return Arrays.asList(entity);
					}
				}
			}
			return Collections.emptyList();
		}
		Vec3d vec3d = this.position.apply(source.getPos());
		Predicate<Entity> predicate = this.getPredicate(vec3d);
		if (this.allowSelf) {
			if (source.getEntity() != null && predicate.test(source.getEntity())) {
				return Arrays.asList(source.getEntity());
			}
			return Collections.emptyList();
		}
		ArrayList<Entity> list = new ArrayList<>();
		if (this.isRestrictOtherWorlds() || !(source instanceof CommandSourceStack)) {
			this.collectEntities(list, source.getWorld(), vec3d, predicate);
		} else {
			for (ServerWorld world : ((CommandSourceStack)source).getServer().worlds) {
				this.collectEntities(list, world, vec3d, predicate);
			}
		}
		return this.sortAndLimit(vec3d, list);
	}

	private void collectEntities(List<Entity> entities, World world, Vec3d pos, Predicate<Entity> filter) {
		if (this.bounds != null) {
			entities.addAll(world.getEntities(this.type, this.bounds.move(pos), filter::test));
		} else {
			entities.addAll(world.getEntities(this.type, filter::test));
		}
	}

	public PlayerEntity findSinglePlayer(AbstractCommandSourceStack<?> source) throws CommandSyntaxException {
		this.checkPermissions(source);
		List<PlayerEntity> list = this.findPlayers(source);
		if (list.size() != 1) {
			throw EntityArgument.PLAYER_NOT_FOUND_EXCEPTION.create();
		}
		return list.get(0);
	}

	public List<PlayerEntity> findPlayers(AbstractCommandSourceStack<?> source) throws CommandSyntaxException {
		this.checkPermissions(source);
		if (this.name != null) {
			if (source instanceof CommandSourceStack) {
				CommandSourceStack serverSource = (CommandSourceStack)source;
				PlayerEntity player = serverSource.getServer().getPlayerManager().get(this.name);
				if (player != null) {
					return Arrays.asList(player);
				}
			}
			if (source instanceof ClientCommandSourceStack) {
				ClientCommandSourceStack clientSource = (ClientCommandSourceStack)source;
				for (PlayerEntity player : clientSource.getWorld().players) {
					if (this.name.equals(player.getName())) {
						return Arrays.asList(player);
					}
				}
			}
			return Collections.emptyList();
		}
		if (this.uuid != null) {
			if (source instanceof CommandSourceStack) {
				CommandSourceStack serverSource = (CommandSourceStack)source;
				PlayerEntity player = serverSource.getServer().getPlayerManager().get(this.uuid);
				if (player != null) {
					return Arrays.asList(player);
				}
			}
			if (source instanceof ClientCommandSourceStack) {
				ClientCommandSourceStack clientSource = (ClientCommandSourceStack)source;
				for (PlayerEntity player : clientSource.getWorld().players) {
					if (this.uuid.equals(player.getUuid())) {
						return Arrays.asList(player);
					}
				}
			}
			return Collections.emptyList();
		}
		Vec3d pos = this.position.apply(source.getPos());
		Predicate<Entity> predicate = this.getPredicate(pos);
		if (this.allowSelf) {
			if (source.getEntity() instanceof PlayerEntity && predicate.test(source.getEntity())) {
				return Arrays.asList((PlayerEntity)source.getEntity());
			}
			return Collections.emptyList();
		}
		List<PlayerEntity> players;
		if (this.isRestrictOtherWorlds()) {
			players = source.getWorld().getPlayers(PlayerEntity.class, predicate::test);
		} else {
			players = new ArrayList<>();
			if (source instanceof CommandSourceStack) {
				CommandSourceStack serverSource = (CommandSourceStack)source;
				for (PlayerEntity player : serverSource.getServer().getPlayerManager().getAll()) {
					if (predicate.test(player)) {
						players.add(player);
					}
				}
			}
			if (source instanceof ClientCommandSourceStack) {
				ClientCommandSourceStack clientSource = (ClientCommandSourceStack)source;
				for (PlayerEntity player : clientSource.getWorld().players) {
					if (predicate.test(player)) {
						players.add(player);
					}
				}
			}
		}
		return this.sortAndLimit(pos, players);
	}

	private Predicate<Entity> getPredicate(Vec3d pos) {
		Predicate<Entity> predicate = this.predicate;
		if (this.bounds != null) {
			Box bounds = this.bounds.move(pos);
			predicate = predicate.and(entity -> bounds.intersects(entity.getShape()));
		}
		if (this.minDistance != null || this.maxDistance != null) {
			predicate = predicate.and(entity -> {
				double distance = pos.distanceTo(entity.getSourcePos());
				return (this.minDistance == null || distance >= this.minDistance) && (this.maxDistance == null || distance <= this.maxDistance);
			});
		}
		return predicate;
	}

	private <T extends Entity> List<T> sortAndLimit(Vec3d pos, List<T> entities) {
		if (entities.size() > 1) {
			this.order.accept(pos, entities);
		}
		return entities.subList(0, Math.min(this.max, entities.size()));
	}

	public static Text joinNames(List<? extends Entity> entities) {
		if (entities.isEmpty()) {
			return new LiteralText("");
		}
		if (entities.size() == 1) {
			return entities.get(0).getDisplayName();
		}
		LiteralText text = new LiteralText("");
		boolean addSeparator = false;
		for (int i = 0; i < entities.size(); i++) {
			if (addSeparator) {
				text.append(new LiteralText(", ").setStyle(new Style().setColor(Formatting.GRAY)));
			}
			text.append(entities.get(i).getDisplayName());
			addSeparator = true;
		}
		return text;
	}
}
