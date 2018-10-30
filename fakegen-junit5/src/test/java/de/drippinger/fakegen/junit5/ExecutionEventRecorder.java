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

import de.drippinger.fakegen.junit5.ExecutionEvent.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.engine.reporting.ReportEntry;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static de.drippinger.fakegen.junit5.ExecutionEvent.Type.*;
import static de.drippinger.fakegen.junit5.ExecutionEvent.*;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.toList;
import static org.junit.platform.commons.util.FunctionUtils.where;

/**
 * {@link EngineExecutionListener} that records all events and makes them available to tests.
 *
 * @see ExecutionEvent
 * @since 1.0
 */
public class ExecutionEventRecorder implements EngineExecutionListener {

    public static List<ExecutionEvent> execute(TestEngine testEngine, EngineDiscoveryRequest discoveryRequest) {
        ExecutionEventRecorder recorder = new ExecutionEventRecorder();
        execute(testEngine, discoveryRequest, recorder);
        return recorder.getExecutionEvents();
    }

    public static void execute(TestEngine testEngine, EngineDiscoveryRequest discoveryRequest,
                               EngineExecutionListener listener) {
        TestDescriptor engineTestDescriptor = testEngine.discover(discoveryRequest,
                UniqueId.forEngine(testEngine.getId()));
        testEngine.execute(
                new ExecutionRequest(engineTestDescriptor, listener, discoveryRequest.getConfigurationParameters()));
    }

    private final List<ExecutionEvent> executionEvents = new CopyOnWriteArrayList<>();

    @Override
    public void dynamicTestRegistered(TestDescriptor testDescriptor) {
        addEvent(ExecutionEvent.dynamicTestRegistered(testDescriptor));
    }

    @Override
    public void executionSkipped(TestDescriptor testDescriptor, String reason) {
        addEvent(ExecutionEvent.executionSkipped(testDescriptor, reason));
    }

    @Override
    public void executionStarted(TestDescriptor testDescriptor) {
        addEvent(ExecutionEvent.executionStarted(testDescriptor));
    }

    @Override
    public void executionFinished(TestDescriptor testDescriptor, TestExecutionResult result) {
        addEvent(ExecutionEvent.executionFinished(testDescriptor, result));
    }

    @Override
    public void reportingEntryPublished(TestDescriptor testDescriptor, ReportEntry entry) {
        addEvent(ExecutionEvent.reportingEntryPublished(testDescriptor, entry));
    }

    public List<ExecutionEvent> getExecutionEvents() {
        return executionEvents;
    }

    public Stream<ExecutionEvent> eventStream() {
        return getExecutionEvents().stream();
    }

    public long getReportingEntryPublishedCount() {
        return testEventsByType(REPORTING_ENTRY_PUBLISHED).count();
    }

    public long getDynamicTestRegisteredCount() {
        return eventsByTypeAndTestDescriptor(DYNAMIC_TEST_REGISTERED, descriptor -> true).count();
    }

    public long getTestSkippedCount() {
        return testEventsByType(SKIPPED).count();
    }

    public long getTestStartedCount() {
        return testEventsByType(STARTED).count();
    }

    public long getTestFinishedCount() {
        return testEventsByType(FINISHED).count();
    }

    public long getTestSuccessfulCount() {
        return getTestFinishedCount(Status.SUCCESSFUL);
    }

    public long getTestAbortedCount() {
        return getTestFinishedCount(Status.ABORTED);
    }

    public long getTestFailedCount() {
        return getTestFinishedCount(Status.FAILED);
    }

    public long getContainerSkippedCount() {
        return containerEventsByType(SKIPPED).count();
    }

    public long getContainerStartedCount() {
        return containerEventsByType(STARTED).count();
    }

    public long getContainerFinishedCount() {
        return containerEventsByType(FINISHED).count();
    }

    public long getContainerFailedCount() {
        return getContainerFinishedCount(Status.FAILED);
    }

    public List<ExecutionEvent> getSkippedTestEvents() {
        return testEventsByType(SKIPPED).collect(toList());
    }

    public List<ExecutionEvent> getSuccessfulTestFinishedEvents() {
        return testFinishedEvents(Status.SUCCESSFUL).collect(toList());
    }

    public List<ExecutionEvent> getFailedTestFinishedEvents() {
        return testFinishedEvents(Status.FAILED).collect(toList());
    }

    public List<ExecutionEvent> getFailedContainerFinishedEvents() {
        return containerFinishedEvents(Status.FAILED).collect(toList());
    }

    private long getTestFinishedCount(Status status) {
        return testFinishedEvents(status).count();
    }

    private long getContainerFinishedCount(Status status) {
        return containerFinishedEvents(status).count();
    }

    private Stream<ExecutionEvent> testFinishedEvents(Status status) {
        return testEventsByType(FINISHED).filter(
                byPayload(TestExecutionResult.class, where(TestExecutionResult::getStatus, isEqual(status))));
    }

    private Stream<ExecutionEvent> containerFinishedEvents(Status status) {
        return containerEventsByType(FINISHED).filter(
                byPayload(TestExecutionResult.class, where(TestExecutionResult::getStatus, isEqual(status))));
    }

    private Stream<ExecutionEvent> testEventsByType(Type type) {
        return eventsByTypeAndTestDescriptor(type, TestDescriptor::isTest);
    }

    private Stream<ExecutionEvent> containerEventsByType(Type type) {
        return eventsByTypeAndTestDescriptor(type, TestDescriptor::isContainer);
    }

    private Stream<ExecutionEvent> eventsByTypeAndTestDescriptor(Type type,
                                                                 Predicate<? super TestDescriptor> predicate) {
        return eventStream().filter(byType(type).and(byTestDescriptor(predicate)));
    }

    private void addEvent(ExecutionEvent event) {
        executionEvents.add(event);
    }

}
