package net.ornithemc.osl.datagen.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class DataGenHelperImpl {
    /**
     * When enabled the startup will be hijacked to run the data generators and then quit.
     */
    public static final boolean ENABLED = System.getProperty("fabric-api.datagen") != null;

    /**
     * Sets the output directory for the generated data.
     */
    private static final String OUTPUT_DIR = System.getProperty("fabric-api.datagen.output-dir");

    /**
     * When enabled providers can enable extra validation, such as ensuring all registry entries have data generated for them.
     */
    public static final boolean STRICT_VALIDATION = System.getProperty("fabric-api.datagen.strict-validation") != null;

    /**
     * Filter to a specific mod ID with this property, useful if dependencies also have data generators.
     */
    public static final String MOD_ID_FILTER = System.getProperty("fabric-api.datagen.modid");

    public static final String ENTRYPOINT_KEY = "datagen";

    public static Path getOutputDir() {
        return Paths.get(Objects.requireNonNull(OUTPUT_DIR, "No output dir provided with the 'fabric-api.datagen.output-dir' property"));
    }
}
