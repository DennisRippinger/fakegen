package de.drippinger.fakegen.stub;

import de.drippinger.fakegen.TestDataFiller;
import de.drippinger.fakegen.TestDataFillerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author Dennis Rippinger
 */
@ExtendWith(TestDataFillerExtension.class)
public class DataFillerExtensionSucessStub {

    private TestDataFiller testDataFiller = new TestDataFiller();

    @Test
    public void test_should_succeed() {
        // NOP
    }

}
