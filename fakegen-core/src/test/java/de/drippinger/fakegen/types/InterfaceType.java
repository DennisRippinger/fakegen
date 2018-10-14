package de.drippinger.fakegen.types;

import java.time.LocalDateTime;

/**
 * @author Dennis Rippinger
 */
public interface InterfaceType {

    String getName();

    LocalDateTime getTime();

    boolean isValid();

}
