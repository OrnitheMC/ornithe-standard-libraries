package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.resource.Identifier;
import net.minecraft.world.gen.structure.template.StructureManager;
import net.minecraft.world.gen.structure.template.StructureTemplate;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.resource.loader.api.resource.Resource;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Conditional(minecraftVersion = @Version(">=15w31a"))
@Mixin(StructureManager.class)
public class StructureManagerMixin implements ResourceReloadListener {

	private static final String DIRECTORY = "structures";
	private static final String EXTENSION = ".nbt";

	@Shadow
	private Map<String, StructureTemplate> templates;

	@Unique
	private ResourceManager resourceManager;

	@Shadow
	private void loadStructure(String path, InputStream is) throws IOException { }

	@WrapOperation(
		method = "getStructure",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/structure/template/StructureManager;loadGeneratedStructure(Lnet/minecraft/resource/Identifier;)Z"
		)
	)
	private boolean osl$resource_loader$loadFromResources1(StructureManager structureManager, Identifier identifier, Operation<Boolean> op) {
		if (this.resourceManager == null) {
			return op.call(structureManager, identifier);
		} else {
			return this.loadFromResources(identifier);
		}
	}

	@WrapOperation(
		method = "loadFromNbt",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/gen/structure/template/StructureManager;loadGeneratedStructure(Lnet/minecraft/resource/Identifier;)Z"
		)
	)
	private boolean osl$resource_loader$loadFromResources2(StructureManager structureManager, Identifier identifier, Operation<Boolean> op) {
		if (this.resourceManager == null) {
			return op.call(structureManager, identifier);
		} else {
			return this.loadFromResources(identifier);
		}
	}

	@Override
	public void resourcesReloaded(ResourceManager resourceManager) {
		this.templates.clear();
		this.resourceManager = resourceManager;
	}

	@Unique
	private boolean loadFromResources(Identifier identifier) {
		NamespacedIdentifier location = identifier
			.prefixed(DIRECTORY + "/")
			.suffixed(EXTENSION);
		Optional<Resource> resource = this.resourceManager.getResource(location);

		if (!resource.isPresent()) {
			return false;
		}

		try (InputStream is = resource.get().open()) {
			this.loadStructure(identifier.getPath(), is);
			return true;
		} catch (Throwable t) {
			ResourceLoader.LOGGER.error("Couldn't load custom structure template {} from {}", identifier, location, t);
			return false;
		}
	}
}
