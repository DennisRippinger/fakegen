package de.drippinger.fakegen.lombok;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Dennis Rippinger
 */
@Data
@Builder
public class SimpleType {

    private String name;

    private LocalDate birthday;

    private SimpleEnum simpleEnum;

}
