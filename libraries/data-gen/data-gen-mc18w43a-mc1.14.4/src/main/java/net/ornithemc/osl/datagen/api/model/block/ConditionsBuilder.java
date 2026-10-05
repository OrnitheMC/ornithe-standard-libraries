package net.ornithemc.osl.datagen.api.model.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents block model definition selector conditions.
 */
public interface ConditionsBuilder {
    JsonObject build();

    /**
     * Creates an inverted property predicate with the specified property and values.
     *
     * @param property the property
     * @param mainMatch the main match value
     * @param extraPossibleMatch the extra possible match values
     * @param <T> the type of the property
     * @return the property builder
     */
    @SafeVarargs
    static <T extends Comparable<T>> PropertyPredicate<T> propertyInverted(Property<T> property, @NotNull T mainMatch, T... extraPossibleMatch) {
        List<T> valueList = new ArrayList<>();
        valueList.add(mainMatch);
        valueList.addAll(Arrays.asList(extraPossibleMatch));
        return new PropertyPredicate<>(false, property, valueList);
    }

    /**
     * Creates a property predicate with the specified property and values.
     *
     * @param property the property
     * @param mainMatch the main match value
     * @param extraPossibleMatch the extra possible match values
     * @param <T> the type of the property
     * @return the property builder
     */
    @SafeVarargs
    static <T extends Comparable<T>> PropertyPredicate<T> property(Property<T> property, @NotNull T mainMatch, T... extraPossibleMatch) {
        List<T> valueList = new ArrayList<>();
        valueList.add(mainMatch);
        valueList.addAll(Arrays.asList(extraPossibleMatch));
        return new PropertyPredicate<>(false, property, valueList);
    }

    /**
     * Creates a simple conditions builder with the specified properties.
     *
     * @param properties the properties
     * @return the conditions builder
     */
    static ConditionsBuilder properties(PropertyPredicate<?>...properties) {
        if (properties.length == 0) {
            throw new IllegalArgumentException("Must provide at least one property");
        }

        return new PropertyConditionBuilder(properties);
    }

    /**
     * Creates a "or" conditions builder with the specified conditions.
     *
     * @param conditions the conditions
     * @return the conditions builder
     */
    static ConditionsBuilder or(ConditionsBuilder...conditions) {
        if (conditions.length == 0) {
            throw new IllegalArgumentException("Must provide at least one condition");
        }

        return new OrConditonBuilder(conditions);
    }

    /**
     * Creates a "and" conditions builder with the specified conditions.
     *
     * @param conditions the conditions
     * @return the conditions builder
     */
    static ConditionsBuilder and(ConditionsBuilder...conditions) {
        if (conditions.length == 0) {
            throw new IllegalArgumentException("Must provide at least one condition");
        }

        return new AndConditonBuilder(conditions);
    }

    class PropertyPredicate<T extends Comparable<T>> {
        private final boolean inverted;
        private final Property<T> property;
        private final List<T> values;

        private PropertyPredicate(boolean inverted, Property<T> property, List<T> values) {
            this.inverted = inverted;
            this.property = property;
            this.values = values;
        }

        private void write(JsonObject object) {
            object.addProperty(property.getName(), (inverted ? "!" : "") + values.stream().map(property::getName).collect(Collectors.joining("|")));
        }
    }

    class PropertyConditionBuilder implements ConditionsBuilder {
        private final PropertyPredicate<?>[] properties;

        private PropertyConditionBuilder(PropertyPredicate<?>[] properties) {
            this.properties = properties;
        }

        @Override
        public JsonObject build() {
            JsonObject object = new JsonObject();

            for (PropertyPredicate<?> propertyPredicate : properties) {
                propertyPredicate.write(object);
            }

            return object;
        }
    }

    class AndConditonBuilder implements ConditionsBuilder {
        private final ConditionsBuilder[] conditions;

        private AndConditonBuilder(ConditionsBuilder[] conditions) {
            this.conditions = conditions;
        }

        @Override
        public JsonObject build() {
            JsonArray array = new JsonArray();

            for (ConditionsBuilder condition : conditions) {
                array.add(condition.build());
            }

            JsonObject object = new JsonObject();
            object.add("AND", array);

            return object;
        }
    }

    class OrConditonBuilder implements ConditionsBuilder {
        private final ConditionsBuilder[] conditions;

        private OrConditonBuilder(ConditionsBuilder[] conditions) {
            this.conditions = conditions;
        }

        @Override
        public JsonObject build() {
            JsonArray array = new JsonArray();

            for (ConditionsBuilder condition : conditions) {
                array.add(condition.build());
            }

            JsonObject object = new JsonObject();
            object.add("OR", array);

            return object;
        }
    }
}
