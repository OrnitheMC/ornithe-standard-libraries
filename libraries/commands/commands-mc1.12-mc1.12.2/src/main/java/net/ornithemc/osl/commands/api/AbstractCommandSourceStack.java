package net.ornithemc.osl.commands.api;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.Formatting;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ornithemc.osl.commands.api.argument.AnchorArgument;

public abstract class AbstractCommandSourceStack<S extends AbstractCommandSourceStack<S>> implements SuggestionProvider {

	public static final SimpleCommandExceptionType NOT_PLAYER_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("permissions.requires.player"));
	public static final SimpleCommandExceptionType NOT_ENTITY_EXCEPTION = new SimpleCommandExceptionType((Message)new TranslatableText("permissions.requires.entity"));

	protected final BrigadierCommandSource source;
	protected final Vec3d pos;
	protected final int permissions;
	protected final String name;
	protected final Text displayName;
	protected final boolean silent;
	protected final Entity entity;
	protected final ResultConsumer<S> callback;
	protected final AnchorArgument.Anchor anchor;
	protected final Vec2f rotation;

	protected AbstractCommandSourceStack(BrigadierCommandSource source, Vec3d pos, Vec2f rotation, int permissions, String name, Text displayName, Entity entity, boolean silent, ResultConsumer<S> callback, AnchorArgument.Anchor anchor) {
		this.source = source;
		this.pos = pos;
		this.silent = silent;
		this.entity = entity;
		this.permissions = permissions;
		this.name = name;
		this.displayName = displayName;
		this.callback = callback;
		this.anchor = anchor;
		this.rotation = rotation;
	}

	public abstract S withEntity(Entity entity);

	public abstract S withPos(Vec3d pos);

	public abstract S withRotation(Vec2f rotation);

	public abstract S withCallback(ResultConsumer<S> callback);

	public abstract S withCallback(ResultConsumer<S> callback, BinaryOperator<ResultConsumer<S>> chooser);

	public abstract S silent();

	public abstract S withPermissions(int permissions);

	public abstract S withMaxPermissions(int maxPermissions);

	public abstract S withAnchor(AnchorArgument.Anchor anchor);

	public abstract S withFacing(Entity entity, AnchorArgument.Anchor anchor) throws CommandSyntaxException;

	public abstract S withFacing(Vec3d target) throws CommandSyntaxException;

	public Text getDisplayName() {
		return this.displayName;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasPermissions(int permissions) {
		return this.permissions >= permissions;
	}

	public Vec3d getPos() {
		return this.pos;
	}

	public abstract World getWorld();

	public Entity getEntity() {
		return this.entity;
	}

	public Entity getEntityOrThrow() throws CommandSyntaxException {
		if (this.entity == null) {
			throw NOT_ENTITY_EXCEPTION.create();
		}

		return this.entity;
	}

	public ServerPlayerEntity getPlayerOrThrow() throws CommandSyntaxException {
		if (!(this.entity instanceof ServerPlayerEntity)) {
			throw NOT_PLAYER_EXCEPTION.create();
		}

		return (ServerPlayerEntity) this.entity;
	}

	public Vec2f getRotation() {
		return this.rotation;
	}

	public AnchorArgument.Anchor getAnchor() {
		return this.anchor;
	}

	public void sendFailure(Text message) {
		if (this.source.sendCommandFailure() && !this.silent) {
			this.source.sendMessage(new LiteralText("").append(message).setStyle(new Style().setColor(Formatting.RED)));
		}
	}

	public void onCommandComplete(CommandContext<S> ctx, boolean success, int result) {
		if (this.callback != null) {
			this.callback.onCommandComplete(ctx, success, result);
		}
	}

	@Override
	public CompletableFuture<Suggestions> suggest(CommandContext<SuggestionProvider> ctx, SuggestionsBuilder builder) {
		return null;
	}

	@Override
	public Collection<SuggestionProvider.Coordinate> getCoordinates(boolean absolute) {
		return Collections.singleton(SuggestionProvider.Coordinate.DEFAULT_GLOBAL);
	}

	@Override
	public Collection<String> getTeams() {
		return getWorld().getScoreboard().getTeamNames();
	}
}
