package de.drippinger.fakegen;

import lombok.experimental.UtilityClass;

import static java.util.Collections.emptySet;

/**
 * Provides default configurations for {@link TestDataFiller}.
 *
 * @author Dennis Rippinger
 */
@UtilityClass
public class Configs {

    /**
     * Default Configuration for the regular case.
     */
    public static final FakegenConfig DEFAULT = FakegenConfig.builder()
            .usePrivateConstructor(false)
            .useSetter(false)
            .ignoreFields(emptySet())
            .build();

    /**
     * Configuration for Builder Pattern. Excludes the field initBits for Immutables,
     * it tracks the usage of required fields within it.
     */
    public static final FakegenConfig BUILDER = FakegenConfig.builder()
            .usePrivateConstructor(true)
            .useSetter(true)
            .ignoreField("initBits")
            .build();

}
