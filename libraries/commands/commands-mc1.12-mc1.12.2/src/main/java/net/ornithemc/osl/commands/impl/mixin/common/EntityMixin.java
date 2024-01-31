package net.ornithemc.osl.commands.impl.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.ornithemc.osl.commands.api.AbstractCommandSourceStack;
import net.ornithemc.osl.commands.api.BrigadierCommandSource;
import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.api.server.CommandSourceStack;

@Mixin(Entity.class)
public abstract class EntityMixin implements BrigadierCommandSource {

	@Shadow private World world;
	@Shadow private double x;
	@Shadow private double y;
	@Shadow private double z;
	@Shadow private float pitch;
	@Shadow private float yaw;

	@Override
	public AbstractCommandSourceStack<?> createCommandSourceStack() {
		if (world.isClient) {
			return new ClientCommandSourceStack(this, new Vec3d(this.x, this.y, this.z), new Vec2f(this.pitch, this.yaw), (ClientWorld)this.world, 0, this.getName(), this.getDisplayName(), (Entity)(Object)this);
		} else {
			return new CommandSourceStack(this, new Vec3d(this.x, this.y, this.z), new Vec2f(this.pitch, this.yaw), (ServerWorld)this.world, 0, this.getName(), this.getDisplayName(), this.world.getServer(), (Entity)(Object)this);
		}
	}

	@Override
	public boolean sendCommandSuccess() {
		return true;
	}

	@Override
	public boolean sendCommandFailure() {
		return true;
	}

	@Override
	public boolean sendCommandSuccessToOps() {
		return true;
	}
}
