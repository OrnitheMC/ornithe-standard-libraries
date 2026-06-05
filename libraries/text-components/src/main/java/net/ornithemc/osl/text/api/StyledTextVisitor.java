package net.ornithemc.osl.text.api;

import java.util.Optional;

public interface StyledTextVisitor<T> {

	Optional<T> accept(Style style, String text);

}
