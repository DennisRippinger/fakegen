package de.drippinger.fakegen.domain;

import de.drippinger.fakegen.TestDataFiller;

import java.util.Random;

/**
 * @author Dennis Rippinger
 */
public class SimpleDomainConfiguration extends DomainConfiguration {

    public SimpleDomainConfiguration(Random random) {
        super(random);
    }

    @Override
    public void init(Random random, TestDataFiller testDataFiller) {
        // NOP
    }

}
