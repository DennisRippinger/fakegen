package de.drippinger.fakegen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
@AllArgsConstructor
public class DomainTuple<T> {

    private String fieldName;

    private Class<T> type;

}
