package net.ornithemc.osl.resource.loader.impl.mixin.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.Identifier;
import net.minecraft.server.FunctionManager;
import net.minecraft.server.command.function.CommandFunction;

import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.resource.loader.api.resource.manager.ResourceManager;
import net.ornithemc.osl.resource.loader.api.resource.reload.ResourceReloadListener;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

@Conditional(minecraftVersion = @Version(">=1.12-pre1"))
@Mixin(FunctionManager.class)
public class FunctionManagerMixin implements ResourceReloadListener {

	private static final String DIRECTORY = "functions";
	private static final String EXTENSION = MinecraftVersion.resolve().compareTo("1.12-pre3") < 0 ? ".txt" : ".mcfunction";

	@Shadow
	private Map<Identifier, CommandFunction> customFunctions;

	@Unique
	private ResourceManager resourceManager;

	@Shadow
	private void reload() { }

	@Redirect(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/FunctionManager;reload()V"
		)
	)
	private void osl$resource_loader$cancelReloadOnInit(FunctionManager functions) {
		// we trigger this reload through the resource manager so it can be profiled
	}

	@Inject(
		method = "reload",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$resource_loader$loadFromResources(CallbackInfo ci) {
		if (this.resourceManager != null) {
			this.loadFromResources(this.customFunctions);
		}
	}

	@Override
	public void resourcesReloaded(ResourceManager manager) {
		this.resourceManager = manager;
		this.reload();
	}

	@Unique
	private void loadFromResources(Map<Identifier, CommandFunction> functions) {
		this.resourceManager.findResources(DIRECTORY, location -> location.identifier().endsWith(EXTENSION)).forEach((location, resource) -> {
			Identifier identifier = new Identifier(
				location.namespace(),
				location.identifier().substring(DIRECTORY.length() + 1, location.identifier().length() - EXTENSION.length())
			);

			// prioritize functions from local data/ directory
			if (functions.containsKey(identifier)) {
				return;
			}

			try (BufferedReader br = resource.openAsReader()) {
				CommandFunction function = CommandFunction.of((FunctionManager) (Object) this, br.lines().collect(Collectors.toList()));

				if (function == null) {
					ResourceLoader.LOGGER.error("Couldn't load custom function {} from {} as it's empty or null", identifier, location);
				} else {
					functions.put(identifier, function);
				}
			} catch (IllegalArgumentException e) {
				ResourceLoader.LOGGER.error("Parsing error loading custom function {}: {}", identifier, e.getMessage());
			} catch (IOException e) {
				ResourceLoader.LOGGER.error("Couldn't read custom function {} from {}", identifier, location, e);
			}
		});
	}
}
