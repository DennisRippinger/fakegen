package de.drippinger.fakegen.types;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author Dennis Rippinger
 */
@Data
public class SimpleType {

    private String name;

    private LocalDate birthday;

    private SimpleEnum simpleEnum;

}
