package net.ornithemc.osl.resource.loader.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;

public class ResourceLoaderMixinPlugin implements IMixinConfigPlugin {

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if ("net.ornithemc.osl.resource.loader.impl.mixin.common.AdvancementManagerMixin".equals(mixinClassName)
			|| "net.ornithemc.osl.resource.loader.impl.mixin.common.CraftingManagerMixin".equals(mixinClassName)
		) {
			return MinecraftVersion.resolve().compareTo("17w13a") >= 0;
		}
		if ("net.ornithemc.osl.resource.loader.impl.mixin.client.LegacyResourcePackAccess".equals(mixinClassName)) {
			return MinecraftVersion.resolve().compareTo("16w32a") >= 0;
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
