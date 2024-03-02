package net.ornithemc.osl.branding.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import net.ornithemc.osl.branding.api.Operation;

public class BrandingModifier {

	private final Map<String, Component> components;
	private final Set<Component> componentsInOrder;

	private int cachedHash;
	private String cachedPatch;

	public BrandingModifier() {
		this.components = new HashMap<>();
		this.componentsInOrder = new TreeSet<>();
	}

	public void register(String key, Operation operation, String value, int priority) {
		Component component = new Component(key, operation, value, priority);

		if (components.containsKey(key)) {
			throw new IllegalStateException("modifier component " + key + " already exists");
		} else {
			components.put(key, component);
			componentsInOrder.add(component);
		}
	}

	public String apply(String s) {
		int hash = Objects.hash(components, s);

		if (hash == cachedHash) {
			return cachedPatch;
		}

		new ArrayList<>().hashCode();
		List<String> buffer = new ArrayList<>();
		int original = 0;

		buffer.add(s);

		for (Component component : componentsInOrder) {
			try {
				original = component.operation.apply(buffer, original, component.value);
			} catch (Throwable t) {
				throw new RuntimeException("unable to apply modifier component provided by " + component.key, t);
			}
		}

		cachedHash = hash;
		cachedPatch = String.join("", buffer);

		return cachedPatch;
	}


	private class Component implements Comparable<Component> {

		public final String key;
		public final Operation operation;
		public final String value;
		public final int priority;

		public Component(String key, Operation operation, String value, int priority) {
			this.key = key;
			this.operation = operation;
			this.value = value;
			this.priority = priority;
		}

		@Override
		public int compareTo(Component o) {
			return Integer.compare(priority, o.priority);
		}
	}
}
