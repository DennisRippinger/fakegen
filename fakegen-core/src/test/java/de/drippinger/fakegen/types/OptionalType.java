package de.drippinger.fakegen.types;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dennis Rippinger
 */
@Data
public class OptionalType {

    private Optional<String> optionalString;

    private Optional<List<String>> optionalStringList;

    private Optional<Map<String, String>> optionalMap;

}
