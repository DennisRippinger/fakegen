package de.drippinger.fakegen.types;

import java.time.LocalDateTime;

/**
 * @author Dennis Rippinger
 */
public interface InterfaceType extends SuperInterface {

    String getName();

    LocalDateTime getTime();

    boolean isValid();

    static boolean isValid(String test) {
        return test != null;
    }

    default boolean isSomehowValid() {
        return isValid();
    }

}
