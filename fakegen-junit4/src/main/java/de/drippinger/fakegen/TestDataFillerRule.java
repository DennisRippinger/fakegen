package de.drippinger.fakegen;

import de.drippinger.fakegen.domain.DomainConfiguration;
import lombok.experimental.Delegate;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This JUnit4 Rule offers the methods of {@link de.drippinger.fakegen.TestDataFiller} to create and fill
 * an instance of a class. If the test fails for some reason, it will print a message with the random seed
 * used to generate all random values into STOUT. This allows to replay a failed test with the same values.
 *
 * @author Dennis Rippinger
 */
public class TestDataFillerRule implements TestRule {

    @Delegate
    private final TestDataFiller testDataFiller;


    /**
     * Creates a new instance with a {@link de.drippinger.fakegen.domain.SimpleDomainConfiguration}
     * and a new Random seed.
     */
    public TestDataFillerRule() {
        this.testDataFiller = new TestDataFiller();
    }

    /**
     * Creates a new instance with a {@link de.drippinger.fakegen.domain.SimpleDomainConfiguration}
     * with a fixed seed.
     *
     * @param seed the fixed seed, non null.
     */
    public TestDataFillerRule(Long seed) {
        this.testDataFiller = new TestDataFiller(seed);
    }

    /**
     * Creates a new instance based on a custom domain class. It will create an instance and
     * provide the correct initialization of its random part.
     *
     * @param clazz the domain class definition, non null.
     */
    public TestDataFillerRule(Class<? extends DomainConfiguration> clazz) {
        testDataFiller = new TestDataFiller(clazz);
    }

    /**
     * Creates a new instance based on a custom domain class. It will create an instance and
     * provide the correct initialization of its random part by the given seed.
     *
     * @param clazz the domain class definition, non null.
     * @param seed  the fixed seed, non null.
     */
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
