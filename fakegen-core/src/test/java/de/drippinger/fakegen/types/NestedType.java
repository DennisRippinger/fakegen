package de.drippinger.fakegen.types;

import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
public class NestedType {

    private String name;

    private SimpleType simpleType;
}
