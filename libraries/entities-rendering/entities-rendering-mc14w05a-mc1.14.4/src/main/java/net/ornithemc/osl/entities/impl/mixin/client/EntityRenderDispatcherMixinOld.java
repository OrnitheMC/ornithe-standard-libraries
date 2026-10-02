package net.ornithemc.osl.entities.impl.mixin.client;

import java.util.Map;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

import net.ornithemc.osl.entities.api.client.EntityRenderingEvents;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixinOld {

	@Shadow @Final
	private Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderers;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void osl$entities$registerEntityRenderers(CallbackInfo ci) {
		EntityRenderingEvents.REGISTER_ENTITY_RENDERERS.invoker().accept(this::register);
	}

	@Unique
	private <T extends Entity> void register(Class<T> type, Function<EntityRenderDispatcher, EntityRenderer<? super T>> factory) {
		this.renderers.put(type, factory.apply((EntityRenderDispatcher) (Object) this));
	}
}
