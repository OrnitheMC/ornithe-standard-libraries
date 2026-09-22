package net.ornithemc.osl.datagen.api.model.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface ConditionsBuilder {
    JsonObject build();

    @SafeVarargs
    static <T extends Comparable<T>> PropertyBuilder<T> propertyNegated(Property<T> property, @NotNull T mainMatch, T... extraPossibleMatch) {
        List<T> valueList = new ArrayList<>();
        valueList.add(mainMatch);
        valueList.addAll(Arrays.asList(extraPossibleMatch));
        return new PropertyBuilder<>(false, property, valueList);
    }

    @SafeVarargs
    static <T extends Comparable<T>> PropertyBuilder<T> property(Property<T> property, @NotNull T mainMatch, T... extraPossibleMatch) {
        List<T> valueList = new ArrayList<>();
        valueList.add(mainMatch);
        valueList.addAll(Arrays.asList(extraPossibleMatch));
        return new PropertyBuilder<>(false, property, valueList);
    }

    static ConditionsBuilder properties(PropertyBuilder<?>...properties) {
        if (properties.length == 0) {
            throw new IllegalArgumentException("Must provide at least one property");
        }

        return new PropertyConditionBuilder(properties);
    }

    static ConditionsBuilder or(ConditionsBuilder...conditions) {
        if (conditions.length == 0) {
            throw new IllegalArgumentException("Must provide at least one condition");
        }

        return new OrConditonBuilder(conditions);
    }

    static ConditionsBuilder and(ConditionsBuilder...conditions) {
        if (conditions.length == 0) {
            throw new IllegalArgumentException("Must provide at least one condition");
        }

        return new AndConditonBuilder(conditions);
    }

    class PropertyBuilder<T extends Comparable<T>> {
        private final boolean negated;
        private final Property<T> property;
        private final List<T> values;

        private PropertyBuilder(boolean negated, Property<T> property, List<T> values) {
            this.negated = negated;
            this.property = property;
            this.values = values;
        }

        private void write(JsonObject object) {
            object.addProperty(property.getName(), (negated ? "!" : "") + values.stream().map(property::getName).collect(Collectors.joining("|")));
        }
    }

    class PropertyConditionBuilder implements ConditionsBuilder {
        private final PropertyBuilder<?>[] properties;

        private PropertyConditionBuilder(PropertyBuilder<?>[] properties) {
            this.properties = properties;
        }

        @Override
        public JsonObject build() {
            JsonObject object = new JsonObject();

            for (PropertyBuilder<?> propertyBuilder : properties) {
                propertyBuilder.write(object);
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
