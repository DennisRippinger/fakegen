package de.drippinger.fakegen;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Set;

/**
 * Configuration class to further refine the usage of {@link TestDataFiller}.
 *
 * @author Dennis Rippinger
 */
@Value
@Builder
@Wither
public class FakegenConfig {

    /**
     * Should use Private Constructors.
     */
    @Builder.Default
    private final Boolean usePrivateConstructor = false;

    /**
     * Use Setter instead of field injection.
     */
    @Builder.Default
    private final Boolean useSetter = false;

    /**
     * Fields to ignore as simple name.
     */
    @Singular
    private final Set<String> ignoreFields;

}
