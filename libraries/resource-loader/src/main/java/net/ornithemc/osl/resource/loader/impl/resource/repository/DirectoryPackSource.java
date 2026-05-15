package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.nio.file.Path;
import java.util.function.Consumer;

import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

public class DirectoryPackSource implements ResourcePackRepository.Source {

	public static String packId(Path file) {
		return "file/" + file.getFileName();
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		//  TODO
	}
}
