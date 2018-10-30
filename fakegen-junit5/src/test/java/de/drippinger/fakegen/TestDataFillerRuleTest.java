package de.drippinger.fakegen;

import de.drippinger.fakegen.junit5.AbstractJupiterTestEngineTests;
import de.drippinger.fakegen.junit5.ExecutionEventRecorder;
import de.drippinger.fakegen.stub.DataFillerExtensionStub;
import de.drippinger.fakegen.stub.DataFillerExtensionSucessStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.LauncherDiscoveryRequest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

/**
 * @author Dennis Rippinger
 */
class TestDataFillerRuleTest extends AbstractJupiterTestEngineTests {


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should write which seed was used for failing test")
    void should_write_which_seed_was_used_for_failing_test() {
        LauncherDiscoveryRequest request = request().selectors(selectClass(DataFillerExtensionStub.class)).build();
        ExecutionEventRecorder eventRecorder = executeTests(request);

        assertThat(eventRecorder.getTestFailedCount()).isEqualTo(1);

        assertThat(outContent.toString().trim())
                .matches("Seed used in Test .* was [-]?\\d{2,}");
    }

    @Test
    @DisplayName("Should succeed")
    void should_succeed() {
        LauncherDiscoveryRequest request = request().selectors(selectClass(DataFillerExtensionSucessStub.class)).build();
        ExecutionEventRecorder eventRecorder = executeTests(request);

        assertThat(eventRecorder.getTestStartedCount()).isEqualTo(1);

        assertThat(outContent.toString().trim()).isEmpty();
    }
}
