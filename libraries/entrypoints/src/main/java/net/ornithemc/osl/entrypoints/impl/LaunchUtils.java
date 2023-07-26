package net.ornithemc.osl.entrypoints.impl;

import net.ornithemc.osl.entrypoints.api.RunArgs;

public class LaunchUtils {

	public static RunArgs wrapFabricRunArgs(RunArgs args) {
		return name -> {
			String arg = args.getParameter(name);

			if (arg == null) {
				arg = args.getParameter("fabric.arguments." + name);
			}

			return arg;
		};
	}
}
