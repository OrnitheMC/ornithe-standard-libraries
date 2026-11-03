package net.ornithemc.osl.crashreports.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.core.impl.util.ModLoader;
import net.ornithemc.osl.crashreports.api.crash.CrashListener;
import net.ornithemc.osl.crashreports.api.crash.CrashReport;
import net.ornithemc.osl.crashreports.api.crash.CrashReportSection;

public final class CrashReports {

	public static final Logger LOGGER = LogManager.getLogger("OSL|Crash Reports");

	private static final String[] WITTY_COMMENTS = new String[]{
		"Who set us up the TNT?",
		"Everything's going to plan. No, really, that was supposed to happen.",
		"Uh... Did I do that?",
		"Oops.",
		"Why did you do that?",
		"I feel sad now :(",
		"My bad.",
		"I'm sorry, Dave.",
		"I let you down. Sorry :(",
		"On the bright side, I bought you a teddy bear!",
		"Daisy, daisy...",
		"Oh - I know what I did wrong!",
		"Hey, that tickles! Hehehe!",
		"I blame Dinnerbone.",
		"You should try our sister game, Minceraft!",
		"Don't be sad. I'll do better next time, I promise!",
		"Don't be sad, have a hug! <3",
		"I just don't know what went wrong :(",
		"Shall we play a game?",
		"Quite honestly, I wouldn't worry myself about that.",
		"I bet Cylons wouldn't have this problem.",
		"Sorry :(",
		"Surprise! Haha. Well, this is awkward.",
		"Would you like a cupcake?",
		"Hi. I'm Minecraft, and I'm a crashaholic.",
		"Ooh. Shiny.",
		"This doesn't make any sense!",
		"Why is it breaking :(",
		"Don't do that.",
		"Ouch. That hurt :(",
		"You're mean.",
		"This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]",
		"There are four lights!",
		"But it works on my machine."
	};

	public static void addSystemDetails(CrashReportSection systemReport) {
		systemReport.addDetail("Minecraft Version", () -> MinecraftVersion.resolve().friendlyGameVersion());
		systemReport.addDetail("Operating System", () -> System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
		systemReport.addDetail("Java Version", () -> System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
		systemReport.addDetail("Java VM Version", () -> System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
		systemReport.addDetail("Memory", () -> {
			Runtime runtime = Runtime.getRuntime();

			long maxMemory = runtime.maxMemory();
			long totalMemory = runtime.totalMemory();
			long freeMemory = runtime.freeMemory();

			long maxMemoryMb = maxMemory / 1024L / 1024L;
			long totalMemoryMb = totalMemory / 1024L / 1024L;
			long freeMemoryMb = freeMemory / 1024L / 1024L;

			return freeMemory + " bytes (" + freeMemoryMb + " MB) / " + totalMemory + " bytes (" + totalMemoryMb + " MB) up to " + maxMemory + " bytes (" + maxMemoryMb + " MB)";
		});
		systemReport.addDetail("JVM Flags", () -> {
			RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

			List<String> inputArgs = runtime.getInputArguments();
			List<String> jvmArgs = inputArgs.stream()
				.filter(arg -> arg.startsWith("-X"))
				.collect(Collectors.toList());
			
			return String.format("%d total; %s", jvmArgs.size(), String.join(" ", jvmArgs));
		});

		addModTree(systemReport);
	}

	public static void addModTree(CrashReportSection systemReport) {
		systemReport.addDetail(ModLoader.resolve().displayName() + " Mods", CrashReports::buildModTree);
	}

	private static String buildModTree() {
		List<ModContainer> topLevelMods = new ArrayList<>();

		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			if (!container.getContainingMod().isPresent()) {
				topLevelMods.add(container);
			}
		}

		StringBuilder sb = new StringBuilder();
		buildModTree(sb, 2, topLevelMods);
		return sb.toString();
	}

	private static void buildModTree(StringBuilder sb, int depth, List<ModContainer> mods) {
		mods.sort(Comparator.comparing(mod -> mod.getMetadata().getId()));

		for (ModContainer mod : mods) {
			sb.append('\n');
			for (int i = 0; i < depth; i++)
				sb.append('\t');
			sb.append(mod.getMetadata().getId());
			sb.append(": ");
			sb.append(mod.getMetadata().getName());
			sb.append(' ');
			sb.append(mod.getMetadata().getVersion().getFriendlyString());

			if (!mod.getContainedMods().isEmpty()) {
				buildModTree(sb, depth + 1, new ArrayList<>(mod.getContainedMods()));
			}
		}
	}

	public static String getWittyComment() {
		try {
			return WITTY_COMMENTS[(int) (System.nanoTime() % WITTY_COMMENTS.length)];
		} catch (Throwable t) {
			return "Witty comment unavailable :(";
		}
	}

	public static void notifyListeners(CrashReport report, List<CrashListener> listeners) {
		notifyListeners(report, report.getSystemReport(), listeners);
	}

	public static void notifyListeners(CrashReport report, CrashReportSection section, List<CrashListener> listeners) {
		for (CrashListener listener : listeners) {
			try {
				listener.populateCrashReport(report, section);
			} catch (Throwable t) {
				LOGGER.warn("Error while notifying crash listener", t);
			}
		}
	}
}
