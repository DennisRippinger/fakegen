package de.drippinger.fakegen.stub;

import de.drippinger.fakegen.TestDataFillerRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Dennis Rippinger
 */
public class DataFillerRuleStub {

    @Rule
    public TestDataFillerRule testData = new TestDataFillerRule();

    @Test
    public void failing_test_should_print_seed() {
        throw new RuntimeException();
    }

}
