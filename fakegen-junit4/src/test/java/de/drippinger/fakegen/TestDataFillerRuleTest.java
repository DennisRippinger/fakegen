package de.drippinger.fakegen;

import de.drippinger.fakegen.stub.DataFillerRuleFixedStub;
import de.drippinger.fakegen.stub.DataFillerRuleStub;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.JUnitCore;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dennis Rippinger
 */
public class TestDataFillerRuleTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void should_write_which_seed_was_used_for_failing_test() {
        JUnitCore.runClasses(DataFillerRuleStub.class);

        assertThat(systemOutRule.getLog().trim())
                .matches("Seed used in Test .* was \\d{2,}");
    }

    @Test
    public void should_write_fixed_seed_used_for_failing_test() {
        JUnitCore.runClasses(DataFillerRuleFixedStub.class);

        assertThat(systemOutRule.getLog().trim())
                .matches("Seed used in Test .* was 1");
    }

}
