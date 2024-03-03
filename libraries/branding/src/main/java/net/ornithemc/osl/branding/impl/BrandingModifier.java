package net.ornithemc.osl.branding.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.branding.api.Operation;

public class BrandingModifier {

	private static final Logger LOGGER = LogManager.getLogger("OSL|Branding");

	private final Map<String, Component> components;
	private final Set<Component> componentsInOrder;

	public BrandingModifier() {
		this.components = new HashMap<>();
		this.componentsInOrder = new TreeSet<>();
	}

	public void register(String key, Operation operation, String value, int priority, boolean required) {
		if (components.containsKey(key)) {
			if (required) {
				throw new IllegalStateException("required modifier component " + key + " already exists");
			} else {
				LOGGER.warn("unrequired modifier component " + key + " already exsists");
			}
		} else {
			Component conflict = checkConflicts(operation);

			if (conflict != null && conflict.required) {
				if (required) {
					throw new IllegalStateException("required modifier component " + key + " conflicts with " + conflict.key);
				} else {
					LOGGER.warn("unrequired modifier component " + key + " conflicts with " + conflict.key);
				}
			} else {
				if (conflict != null) {
					components.remove(conflict.key);
					componentsInOrder.remove(conflict);

					LOGGER.warn("unrequired modifier component " + conflict.key + " conflicts with " + key);
				}

				Component component = new Component(key, operation, value, priority, required);

				components.put(key, component);
				componentsInOrder.add(component);
			}
		}
	}

	public String apply(String s) {
		List<String> buffer = new ArrayList<>();

		buffer.add(s);
		int original = 0;

		for (Component component : componentsInOrder) {
			try {
				original = component.operation.apply(buffer, original, component.value);
			} catch (Throwable t) {
				throw new RuntimeException("unable to apply modifier component provided by " + component.key, t);
			}
		}

		return String.join("", buffer);
	}

	private Component checkConflicts(Operation operation) {
		if (operation == Operation.REPLACE || operation == Operation.SET) {
			for (Component component : componentsInOrder) {
				if (component.operation == Operation.REPLACE || component.operation == Operation.SET) {
					return component;
				}
			}
		}

		return null;
	}

	private class Component implements Comparable<Component> {

		public final int id;
		public final String key;
		public final Operation operation;
		public final String value;
		public final int priority;
		public final boolean required;

		public Component(String key, Operation operation, String value, int priority, boolean required) {
			this.id = components.size();
			this.key = key;
			this.operation = operation;
			this.value = value;
			this.priority = priority;
			this.required = required;
		}

		@Override
		public int compareTo(Component o) {
			int c = Integer.compare(priority, o.priority);
			return (c == 0) ? Integer.compare(id, o.id) : c;
		}
	}
}
