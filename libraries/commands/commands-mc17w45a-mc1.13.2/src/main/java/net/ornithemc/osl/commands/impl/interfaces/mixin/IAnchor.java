package net.ornithemc.osl.commands.impl.interfaces.mixin;

import net.minecraft.util.math.Vec3d;

import net.ornithemc.osl.commands.api.client.ClientCommandSourceStack;

public interface IAnchor {

	Vec3d osl$commands$apply(ClientCommandSourceStack source);

}
