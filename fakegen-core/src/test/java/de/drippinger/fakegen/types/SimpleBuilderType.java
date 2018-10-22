package de.drippinger.fakegen.types;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Dennis Rippinger
 */
@Data
@Builder
public class SimpleBuilderType {

    private String name;

    private LocalDate birthday;

    private SimpleEnum simpleEnum;

}
