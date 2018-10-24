package de.drippinger.fakegen;

import de.drippinger.fakegen.domain.DomainConfiguration;
import lombok.experimental.Delegate;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Dennis Rippinger
 */
public class TestDataFillerRule implements TestRule {

    @Delegate
    private final TestDataFiller testDataFiller;

    public TestDataFillerRule() {
        this.testDataFiller = new TestDataFiller();
    }

    public TestDataFillerRule(Long seed) {
        this.testDataFiller = new TestDataFiller(seed);
    }

    public TestDataFillerRule(Class<? extends DomainConfiguration> clazz) {
        testDataFiller = new TestDataFiller(clazz);
    }

    public TestDataFillerRule(Class<? extends DomainConfiguration> clazz, Long seed) {
        testDataFiller = new TestDataFiller(clazz, seed);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Exception e) {
                    String message = String
                            .format("Seed used in Test '%s' was %s",
                                    description.getTestClass().getName(),
                                    testDataFiller.getSeed()
                            );

                    System.out.println(message);

                    throw e;
                }
            }
        };
    }


}
