package de.drippinger.fakegen.autovalue;

import com.google.auto.value.AutoValue;

/**
 * Simple Animal Class.
 *
 * @author Dennis Rippinger
 */
@AutoValue
public abstract class Animal {
    abstract String name();

    abstract int numberOfLegs();

    static Builder builder() {
        return new AutoValue_Animal.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setName(String value);

        abstract Builder setNumberOfLegs(int value);

        abstract Animal build();
    }
}
