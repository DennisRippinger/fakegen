package de.drippinger.fakegen.types;

import lombok.Data;

/**
 * @author Dennis Rippinger
 */
@Data
public class RecursiveType {

    private String name;

    private RecursiveType recursiveType;
}
