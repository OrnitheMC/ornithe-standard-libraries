# Branding Patch API

In 16w05b and above, the game parses the `versionType` option from the run args and displays this on the title screen and the debug overlay. The Branding Patch backports this feature for older versions and provides an API for mod developers to add their own components to this information.

## Events

The API provides an event in `BrandingPatchEvents` to register branding modifier components. 
This event is only invoked on the client side, upon game start-up. Callbacks to this event should be
registered in your mod entrypoint:

```java
@Override
public void clientInit() {
	BrandingPatchEvents.REGISTER_MODIFIER_COMPONENT.register(registry -> {
		registry.register(
			BrandingContext.ALL, // apply to both title screen and debug overlay
			"Cookie",            // identifier key of this component, recommended to use mod id
			Operation.APPEND,    // operation to apply, APPEND adds a string to the end
			"cookie",            // the operand, in this case the string to be added,
			0,                   // priority with which this component will be sorted against other components
			false                // whether this component is required - SET and REPLACE components must be unique within a modifier
		);
	});
}
```
