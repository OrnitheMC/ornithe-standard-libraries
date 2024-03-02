package net.ornithemc.osl.branding.api;

import java.util.function.Consumer;

import net.ornithemc.osl.core.api.events.Event;

public class BrandingPatchEvents {

	public static final Event<Consumer<BrandingModifierRegistry>> REGISTER_MODIFIER = Event.consumer();

}
