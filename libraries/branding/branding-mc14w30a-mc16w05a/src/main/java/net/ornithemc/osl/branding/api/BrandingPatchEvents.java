package net.ornithemc.osl.branding.api;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

public class BrandingPatchEvents {

	public static final Event<Consumer<BiConsumer<BrandingModifier, String>>> REGISTER_MODIFIER = Event.consumer();

}
