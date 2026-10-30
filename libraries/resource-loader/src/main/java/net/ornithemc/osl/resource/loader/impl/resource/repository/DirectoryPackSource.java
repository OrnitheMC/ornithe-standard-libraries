package net.ornithemc.osl.resource.loader.impl.resource.repository;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePack;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;

public class DirectoryPackSource implements ResourcePackRepository.Source {

	public static String packId(Path file) {
		return "file/" + file.getFileName();
	}

	private final Path directory;
	private ResourcePackDetector detector;

	public DirectoryPackSource(Path dir) {
		this(dir, new ResourcePackDetector());
	}

	public DirectoryPackSource(Path dir, ResourcePackDetector detector) {
		this.directory = dir;
		this.detector = detector;
	}

	@Override
	public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
		try {
			Files.createDirectories(this.directory);

			discoverResourcePacks(this.directory, this.detector, (file, pack) -> {
				ResourcePackSummary summary = ResourcePackSummary.create(
					pack,
					packId(file),
					false,
					false,
					PackPosition.TOP
				);

				if (summary != null) {
					consumer.accept(summary);
				}
			});
		} catch (IOException e) {
			ResourceLoader.LOGGER.warn("Unable to load packs from {}", this.directory.getFileName());
		}
	}

	private static void discoverResourcePacks(Path directory, ResourcePackDetector detector, BiConsumer<Path, ResourcePack> consumer) throws IOException {
		try (DirectoryStream<Path> dir = Files.newDirectoryStream(directory)) {
			for (Path file : dir) {
				try {
					ResourcePack pack = detector.detectResourcePack(file);

					if (pack != null) {
						consumer.accept(file, pack);
					}
				} catch (IOException e) {
					ResourceLoader.LOGGER.warn("Unable to read properties from {}, ignoring", file.getFileName());
				}
			}
		}
	}
}
