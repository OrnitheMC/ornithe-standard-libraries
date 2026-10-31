# Crash Reports API

The Crash Reports API adds general mod information to crash reports and adds a convenient version-independent way for mods to throw crash exceptions and add details to crash reports.

## Throwing Crash Exceptions

The `CrashExceptions` class provides a utility method for constructing crash exceptions with a given error and title. You may also pass in a crash report builder in which you can add details to the crash report.

```java
try {
	cookieMonster.eatCookie(cookie);
} catch (Throwable error) {
	throw CrashExceptions.from("Cookie monster encountered an error while eating a cookie", error, report -> {
		CrashReportSection section = report.addSection("Cookie Monster");

		section.addDetail("Name", () -> cookieMonster.getName());
		section.addDetail("Cookie Recipe", () -> cookie.getRecipe());
	})
}

```

## Populating Crash Reports

It is possible to add general information to the system report and level details sections of a crash report. This is done by registering a crash listener to one of several classes that implement the `CrashReportPopulator` interface (`Minecraft`, `MinecraftServer`, `World`, and `WorldData`). In a crash listener you can add details to the given crash report section or add a new section to the crash report.

```java
MinecraftClientEvents.START.register(minecraft -> {
	World world = minecraft.world;

	minecraft.addCrashListener((report, section) -> {
		CrashReportSection section = report.addSection("Cookie Management");
	
		section.addDetail("Cookies made", "Not enough!");
		section.addDetail("Cookies eaten", "All of them!");
	});
	world.addCrashListener((report, section) -> {
		section.addDetail("Cookie monsters", () -> world.cookieMonsters.size());
	});
});
```
