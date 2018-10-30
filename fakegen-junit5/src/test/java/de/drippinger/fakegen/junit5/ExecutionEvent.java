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

import org.junit.platform.commons.util.ToStringBuilder;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Predicate;

import static de.drippinger.fakegen.junit5.ExecutionEvent.Type.*;
import static java.util.function.Predicate.isEqual;
import static org.junit.platform.commons.util.FunctionUtils.where;

/**
 * Represents an event collected by {@link ExecutionEventRecorder}.
 *
 * @since 1.0
 */
public class ExecutionEvent {

    public enum Type {
        DYNAMIC_TEST_REGISTERED, SKIPPED, STARTED, FINISHED, REPORTING_ENTRY_PUBLISHED
    }

    public static ExecutionEvent reportingEntryPublished(TestDescriptor testDescriptor, ReportEntry entry) {
        return new ExecutionEvent(REPORTING_ENTRY_PUBLISHED, testDescriptor, entry);
    }

    public static ExecutionEvent dynamicTestRegistered(TestDescriptor testDescriptor) {
        return new ExecutionEvent(DYNAMIC_TEST_REGISTERED, testDescriptor, null);
    }

    public static ExecutionEvent executionSkipped(TestDescriptor testDescriptor, String reason) {
        return new ExecutionEvent(SKIPPED, testDescriptor, reason);
    }

    public static ExecutionEvent executionStarted(TestDescriptor testDescriptor) {
        return new ExecutionEvent(STARTED, testDescriptor, null);
    }

    public static ExecutionEvent executionFinished(TestDescriptor testDescriptor, TestExecutionResult result) {
        return new ExecutionEvent(FINISHED, testDescriptor, result);
    }

    public static Predicate<ExecutionEvent> byType(ExecutionEvent.Type type) {
        return where(ExecutionEvent::getType, isEqual(type));
    }

    public static Predicate<ExecutionEvent> byTestDescriptor(Predicate<? super TestDescriptor> predicate) {
        return where(ExecutionEvent::getTestDescriptor, predicate);
    }

    public static <T> Predicate<ExecutionEvent> byPayload(Class<T> payloadClass, Predicate<? super T> predicate) {
        return event -> event.getPayload(payloadClass).filter(predicate).isPresent();
    }

    private final Instant timestamp;
    private final ExecutionEvent.Type type;
    private final TestDescriptor testDescriptor;
    private final Object payload;

    private ExecutionEvent(ExecutionEvent.Type type, TestDescriptor testDescriptor, Object payload) {
        this.timestamp = Instant.now();
        this.type = type;
        this.testDescriptor = testDescriptor;
        this.payload = payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public ExecutionEvent.Type getType() {
        return type;
    }

    public TestDescriptor getTestDescriptor() {
        return testDescriptor;
    }

    public <T> Optional<T> getPayload(Class<T> payloadClass) {
        return Optional.ofNullable(payload).filter(payloadClass::isInstance).map(payloadClass::cast);
    }

    @Override
    public String toString() {
        // @formatter:off
		return new ToStringBuilder(this)
				.append("type", type)
				.append("timestamp", timestamp)
				.append("testDescriptor", testDescriptor)
				.append("payload", payload)
				.toString();
		// @formatter:on
    }

}
