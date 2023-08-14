package net.ornithemc.osl.commands.api.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;

import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.BrigadierCommandSource;
import net.ornithemc.osl.commands.api.argument.AnchorArgument;

public class ClientCommandSourceStack extends AbstractCommandSourceStack<ClientCommandSourceStack> {

	private final ClientWorld world;
	private final Minecraft minecraft;

	public ClientCommandSourceStack(BrigadierCommandSource source, Vec3d pos, Vec2f rotation, ClientWorld world, int permissions, String name, Text displayName, Entity entity) {
		this(source, pos, rotation, world, permissions, name, displayName, entity, false, (ctx, success, result) -> { }, AnchorArgument.Anchor.FEET);
	}

	public ClientCommandSourceStack(BrigadierCommandSource source, Vec3d pos, Vec2f rotation, ClientWorld world, int permissions, String name, Text displayName, Entity entity, boolean silent, ResultConsumer<ClientCommandSourceStack> callback, AnchorArgument.Anchor anchor) {
		super(source, pos, rotation, permissions, name, displayName, entity, silent, callback, anchor);

		this.world = world;
		this.minecraft = Minecraft.getInstance();
	}

	@Override
	public ClientCommandSourceStack withEntity(Entity entity) {
		if (this.entity == entity) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, entity.getName(), entity.getDisplayName(), entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withPos(Vec3d pos) {
		if (this.pos.equals(pos)) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withRotation(Vec2f rotation) {
		if (this.rotation.equals(rotation)) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, rotation, this.world, this.permissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withCallback(ResultConsumer<ClientCommandSourceStack> callback) {
		if (this.callback.equals(callback)) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.entity, this.silent, callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withCallback(ResultConsumer<ClientCommandSourceStack> callback, BinaryOperator<ResultConsumer<ClientCommandSourceStack>> chooser) {
		return this.withCallback(chooser.apply(this.callback, callback));
	}

	@Override
	public ClientCommandSourceStack silent() {
		if (this.silent) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.entity, true, this.callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withPermissions(int permissions) {
		if (permissions == this.permissions) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.world, permissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withMaxPermissions(int maxPermissions) {
		if (maxPermissions <= this.permissions) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.world, maxPermissions, this.name, this.displayName, this.entity, this.silent, this.callback, this.anchor);
	}

	@Override
	public ClientCommandSourceStack withAnchor(AnchorArgument.Anchor anchor) {
		if (anchor == this.anchor) {
			return this;
		}

		return new ClientCommandSourceStack(this.source, this.pos, this.rotation, this.world, this.permissions, this.name, this.displayName, this.entity, this.silent, this.callback, anchor);
	}

	@Override
	public ClientCommandSourceStack withFacing(Entity entity, AnchorArgument.Anchor anchor) throws CommandSyntaxException {
		return this.withFacing(anchor.apply(entity));
	}

	@Override
	public ClientCommandSourceStack withFacing(Vec3d target) throws CommandSyntaxException {
		Vec3d anchorPos = this.anchor.apply(this);

		double dx = target.x - anchorPos.x;
		double dy = target.y - anchorPos.y;
		double dz = target.z - anchorPos.z;
		double squaredDistance = MathHelper.sqrt(dx * dx + dz * dz);

		float rotX = MathHelper.wrapDegrees((float)(-(MathHelper.fastAtan2(dy, squaredDistance) * 57.2957763671875)));
		float rotY = MathHelper.wrapDegrees((float)(MathHelper.fastAtan2(dz, dx) * 57.2957763671875) - 90.0f);

		return this.withRotation(new Vec2f(rotX, rotY));
	}

	@Override
	public ClientWorld getWorld() {
		return this.world;
	}

	public Minecraft getMinecraft() {
		return this.minecraft;
	}

	public void sendSuccess(Text message) {
		if (this.source.sendCommandSuccess() && !this.silent) {
			this.source.sendMessage(message);
		}
	}

	@Override
	public Collection<String> getPlayerNames() {
		List<String> names = new ArrayList<>();
		for (PlayerEntity player : this.world.players) {
			names.add(player.getName());
		}
		return names;
	}
}
