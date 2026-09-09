package net.ornithemc.osl.entities.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class EntitiesMixinPlugin implements IMixinConfigPlugin {

	public static final boolean SPAWN_EGGS_BY_ID = MinecraftVersion.resolve().compareTo("15w33a") < 0;

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.entities.impl.mixin.common.EntitiesMixin_15w32c".equals(mixinClassName)
			|| "net.ornithemc.osl.entities.impl.mixin.common.SpawnEggDataAccessOld".equals(mixinClassName)) {
			return SPAWN_EGGS_BY_ID;
		}
		if ("net.ornithemc.osl.entities.impl.mixin.common.EntitiesMixin_15w33a".equals(mixinClassName)
			|| "net.ornithemc.osl.entities.impl.mixin.common.SpawnEggDataAccessNew".equals(mixinClassName)) {
			return !SPAWN_EGGS_BY_ID;
		}

		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
