package de.drippinger.fakegen;

import de.drippinger.fakegen.filler.BasicObjectFiller;
import de.drippinger.fakegen.filler.ObjectFiller;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Dennis Rippinger
 */
public class TestDataFillerRule implements TestRule {

    private final TestDataFiller testDataFiller;

    public TestDataFillerRule() {
        this.testDataFiller = new TestDataFiller(new BasicObjectFiller());
    }

    public TestDataFillerRule(Long seed) {
        this.testDataFiller = new TestDataFiller(new BasicObjectFiller(seed));
    }

    public TestDataFillerRule(ObjectFiller objectFiller) {
        this.testDataFiller = new TestDataFiller(objectFiller);
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
