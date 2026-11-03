package net.ornithemc.osl.blockstates.api.block.state;

import com.google.common.base.Optional;

public interface PropertyExtension {

	Optional<Comparable<?>> getValue(String name);

}
