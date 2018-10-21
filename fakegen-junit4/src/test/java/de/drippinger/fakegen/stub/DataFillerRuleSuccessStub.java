package de.drippinger.fakegen.stub;

import de.drippinger.fakegen.TestDataFillerRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Dennis Rippinger
 */
public class DataFillerRuleSuccessStub {

    @Rule
    public TestDataFillerRule testData = new TestDataFillerRule();

    @Test
    public void successfulTestShouldNotPrintSeed() {
        // NOP
    }

}
