package net.ornithemc.osl.commands.impl.mixin.client;

import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.command.argument.AnchorArgument.Anchor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;
import net.ornithemc.osl.commands.impl.interfaces.mixin.IAnchor;

@Mixin(Anchor.class)
public class AnchorMixin implements IAnchor {

	@Shadow @Final private BiFunction<Vec3d, Entity, Vec3d> transform;

	@Override
	public Vec3d osl$commands$apply(ClientCommandSourceStack source) {
		Entity entity = source.getEntity();
		if (entity == null) {
			return source.getPos();
		}
		return this.transform.apply(source.getPos(), entity);
	}
}
