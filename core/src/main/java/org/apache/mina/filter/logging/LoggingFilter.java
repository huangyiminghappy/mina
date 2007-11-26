/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.filter.logging;

import java.util.Map;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoEventType;
import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoFilterAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.IoSessionLogger;
import org.apache.mina.common.WriteRequest;
import org.apache.mina.util.CopyOnWriteMap;

/**
 * Logs all MINA protocol events using the {@link IoSessionLogger}.  Each event can be
 * tuned to use a different level based on the user's specific requirements.  Methods
 * are in place that allow the user to use either the get or set method for each event
 * and pass in the {@link IoEventType} and the {@link LogLevel}.
 *
 * By default, all events are logged to the {@link LogLevel#INFO} level except
 * {@link IoFilterAdapter#exceptionCaught(IoFilter.NextFilter, IoSession, Throwable)},
 * which is logged to {@link LogLevel#WARN}.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class LoggingFilter extends IoFilterAdapter {

    private final Map<IoEventType, LogLevel> logSettings = new CopyOnWriteMap<IoEventType, LogLevel>();
    private final String name;

    /**
     * Default Constructor.
     */
    public LoggingFilter() {
        this(LoggingFilter.class.getName());
    }

    public LoggingFilter(Class<?> clazz) {
        this(clazz.getName());
    }

    public LoggingFilter(String name) {
        this.name = name;

        // Exceptions will be logged to WARN as default.
        setLogLevel(IoEventType.EXCEPTION_CAUGHT, LogLevel.WARN);
        setLogLevel(IoEventType.MESSAGE_RECEIVED, LogLevel.INFO);
        setLogLevel(IoEventType.MESSAGE_SENT, LogLevel.INFO);
        setLogLevel(IoEventType.SESSION_CLOSED, LogLevel.INFO);
        setLogLevel(IoEventType.SESSION_CREATED, LogLevel.INFO);
        setLogLevel(IoEventType.SESSION_IDLE, LogLevel.INFO);
        setLogLevel(IoEventType.SESSION_OPENED, LogLevel.INFO);
    }

    public String getName() {
        return name;
    }

    @Override
    public void exceptionCaught(NextFilter nextFilter, IoSession session,
            Throwable cause) throws Exception {
        getLogLevel(IoEventType.EXCEPTION_CAUGHT).log(
                session, name, "EXCEPTION: ", cause);
        nextFilter.exceptionCaught(session, cause);
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session,
            Object message) throws Exception {
        getLogLevel(IoEventType.MESSAGE_RECEIVED).log(
                session, name, "RECEIVED: " + message);
        nextFilter.messageReceived(session, message);
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session,
            WriteRequest writeRequest) throws Exception {
        getLogLevel(IoEventType.MESSAGE_SENT).log(
                session, name, "SENT: " + writeRequest.getMessage());
        nextFilter.messageSent(session, writeRequest);
    }

    @Override
    public void sessionClosed(NextFilter nextFilter, IoSession session)
            throws Exception {
        getLogLevel(IoEventType.SESSION_CLOSED).log(session, name, "CLOSED");
        nextFilter.sessionClosed(session);
    }

    @Override
    public void sessionCreated(NextFilter nextFilter, IoSession session)
            throws Exception {
        getLogLevel(IoEventType.SESSION_CREATED).log(session, name, "CREATED");
        nextFilter.sessionCreated(session);
    }

    @Override
    public void sessionIdle(NextFilter nextFilter, IoSession session,
            IdleStatus status) throws Exception {
        getLogLevel(IoEventType.SESSION_IDLE).log(session, name, "IDLE: " + status);
        nextFilter.sessionIdle(session, status);
    }

    @Override
    public void sessionOpened(NextFilter nextFilter, IoSession session)
            throws Exception {
        getLogLevel(IoEventType.SESSION_OPENED).log(session, name, "OPENED");
        nextFilter.sessionOpened(session);
    }

    /**
     * Sets the {@link LogLevel} to be used when exceptions are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when exceptions are logged.
     */
    public void setExceptionCaughtLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.EXCEPTION_CAUGHT, logLevel);
    }

    /**
     * Sets the {@link LogLevel} to be used when message received events are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when message received events are logged.
     */
    public void setMessageReceivedLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.MESSAGE_RECEIVED, logLevel);
    }

    /**
     * Sets the {@link LogLevel} to be used when message sent events are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when message sent events are logged.
     */
    public void setMessageSentLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.MESSAGE_SENT, logLevel);
    }

    /**
     * Sets the {@link LogLevel} to be used when session closed events are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when session closed events are logged.
     */
    public void setSessionClosedLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.SESSION_CLOSED, logLevel);
    }

    /**
     * Sets the {@link LogLevel} to be used when session created events are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when session created events are logged.
     */
    public void setSessionCreatedLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.SESSION_CREATED, logLevel);
    }

    /**
     * Sets the {@link LogLevel} to be used when session idle events are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when session idle events are logged.
     */
    public void setSessionIdleLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.SESSION_IDLE, logLevel);
    }

    /**
     * Sets the {@link LogLevel} to be used when session opened events are logged.
     *
     * @param logLevel
     * 	The {@link LogLevel} to be used when session opened events are logged.
     */
    public void setSessionOpenedLogLevel(LogLevel logLevel) {
        setLogLevel(IoEventType.SESSION_OPENED, logLevel);
    }

    /**
     * This method sets the log level for the supplied {@link LogLevel}
     * event.
     *
     * @param eventType the type of the event that is to be updated with
     *                  the new {@link LogLevel}
     * @param logLevel  the new {@link LogLevel} to be used to log the
     *                  specified event
     */
    public void setLogLevel(IoEventType eventType, LogLevel logLevel) {
        if (eventType == null) {
            throw new NullPointerException("eventType");
        }
        if (logLevel == null) {
            throw new NullPointerException("logLevel");
        }

        logSettings.put(eventType, logLevel);
    }

    /**
     * Returns the log level for the supplied event type.
     *
     * @param eventType the type of the event
     */
    public LogLevel getLogLevel(IoEventType eventType) {
        if (eventType == null) {
            throw new NullPointerException("eventType");
        }

        return logSettings.get(eventType);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * exception caught events.
     *
     * @return
     * 	The {@link LogLevel} used when logging exception caught events
     */
    public LogLevel getExceptionCaughtLogLevel() {
        return getLogLevel(IoEventType.EXCEPTION_CAUGHT);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * message received events.
     *
     * @return
     * 	The {@link LogLevel} used when logging message received events
     */
    public LogLevel getMessageReceivedLogLevel() {
        return getLogLevel(IoEventType.MESSAGE_RECEIVED);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * message sent events.
     *
     * @return
     * 	The {@link LogLevel} used when logging message sent events
     */
    public LogLevel getMessageSentLogLevel() {
        return getLogLevel(IoEventType.MESSAGE_SENT);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * session closed events.
     *
     * @return
     * 	The {@link LogLevel} used when logging session closed events
     */
    public LogLevel getSessionClosedLogLevel() {
        return getLogLevel(IoEventType.SESSION_CLOSED);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * session created events.
     *
     * @return
     * 	The {@link LogLevel} used when logging session created events
     */
    public LogLevel getSessionCreatedLogLevel() {
        return getLogLevel(IoEventType.SESSION_CREATED);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * session idle events.
     *
     * @return
     * 	The {@link LogLevel} used when logging session idle events
     */
    public LogLevel getSessionIdleLogLevel() {
        return getLogLevel(IoEventType.SESSION_IDLE);
    }

    /**
     * This method returns the {@link LogLevel} that is used to log
     * session opened events.
     *
     * @return
     * 	The {@link LogLevel} used when logging session opened events
     */
    public LogLevel getSessionOpenedLogLevel() {
        return getLogLevel(IoEventType.SESSION_OPENED);
    }
}
