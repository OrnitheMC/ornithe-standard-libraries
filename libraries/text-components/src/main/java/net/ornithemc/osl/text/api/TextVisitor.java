package net.ornithemc.osl.text.api;

import java.util.Optional;

public interface TextVisitor<T> {

	Optional<T> accept(String text);

}
