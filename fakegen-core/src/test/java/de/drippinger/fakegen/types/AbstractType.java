package de.drippinger.fakegen.types;

import java.time.LocalDateTime;

/**
 * @author Dennis Rippinger
 */
public abstract class AbstractType {

    public abstract String getName();

    public abstract LocalDateTime getTime();

}
