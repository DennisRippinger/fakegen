/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package de.drippinger.fakegen.junit5;

import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.LauncherDiscoveryRequest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

/**
 * Abstract base class for tests involving the {@link JupiterTestEngine}.
 *
 * @since 5.0
 */
public abstract class AbstractJupiterTestEngineTests {

    private final JupiterTestEngine engine = new JupiterTestEngine();

    protected ExecutionEventRecorder executeTestsForClass(Class<?> testClass) {
        return executeTests(selectClass(testClass));
    }

    protected ExecutionEventRecorder executeTests(DiscoverySelector... selectors) {
        return executeTests(request().selectors(selectors).build());
    }

    protected ExecutionEventRecorder executeTests(LauncherDiscoveryRequest request) {
        TestDescriptor testDescriptor = discoverTests(request);
        ExecutionEventRecorder eventRecorder = new ExecutionEventRecorder();
        engine.execute(new ExecutionRequest(testDescriptor, eventRecorder, request.getConfigurationParameters()));
        return eventRecorder;
    }

    protected TestDescriptor discoverTests(DiscoverySelector... selectors) {
        return discoverTests(request().selectors(selectors).build());
    }

    protected TestDescriptor discoverTests(LauncherDiscoveryRequest request) {
        return engine.discover(request, UniqueId.forEngine(engine.getId()));
    }

    protected UniqueId discoverUniqueId(Class<?> clazz, String methodName) {
        TestDescriptor engineDescriptor = discoverTests(selectMethod(clazz, methodName));
        Set<? extends TestDescriptor> descendants = engineDescriptor.getDescendants();
        // @formatter:off
		TestDescriptor testDescriptor = descendants.stream()
				.skip(descendants.size() - 1)
				.findFirst()
				.orElseGet(() -> fail("no descendants"));
		// @formatter:on
        return testDescriptor.getUniqueId();
    }

}
