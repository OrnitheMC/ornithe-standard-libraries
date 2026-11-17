package net.ornithemc.osl.resource.loader.impl.access;

import net.minecraft.resource.pack.Pack;

public interface ResourcePacksAccess extends VanillaPackSource {

	Pack osl$resource_loader$getServerPack();

}
