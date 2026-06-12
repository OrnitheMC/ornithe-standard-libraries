# Localization API

The Localization API provides a consistent access point for localized text, and adds support for localization in versions that do not natively do so.

## Localizing Text

The `L10n` class provides utilities for localizing text.

```java
# localizing a translation key
String localizedText = L10n.get("example.translationKey");

# localizing a translation key with formatting arguments
String localizedTextWithArgs = L10n.get("example.translationKey", "Some Arg", 1);

# localizing a translation key with a default value in case no localization exists
String localizedTextOrDefault = L10n.getOrDefault("example.translationKey", "Example Text");

# checking whether a localization for a translation key exists
boolean localizedTextExists = L10n.has("example.translationKey");
```

## Providing Translations

Translation files can be added to your mod's resources and will be loaded automatically. While Minecraft natively only supports `.lang` files in versions 18w01a and below, and only `.json` files in 18w02a and above, the Localization API ensures both `.lang` and `.json` files are supported in any Minecraft version.

The Localization API also adds support for all-lowercase translation file names in 1.10.2 and below (e.g. `en_us.lang` instead of `en_US.lang`).

## Localization in Minecraft Alpha

Minecraft started localizing text elements in Minecraft Beta. The Localization API adds support for localization in Minecraft Alpha. Do note that no in-game text is modified in any version of the game, making this mostly a feature for modders to take advantage of.
