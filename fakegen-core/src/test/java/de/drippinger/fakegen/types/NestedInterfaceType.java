package de.drippinger.fakegen.types;

import java.time.LocalDateTime;

/**
 * @author Dennis Rippinger
 */
public interface NestedInterfaceType {

    String getName();

    LocalDateTime getTime();

    InterfaceType getNestedInterface();

}
