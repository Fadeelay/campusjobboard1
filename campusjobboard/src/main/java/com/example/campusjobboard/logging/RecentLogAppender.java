package com.example.campusjobboard.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/** Custom Logback appender that feeds log events to RecentLogStore for the admin log view. */
public class RecentLogAppender extends AppenderBase<ILoggingEvent> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    @Override
    protected void append(ILoggingEvent event) {
        RecentLogStore store = RecentLogStore.getInstance();
        if (store == null) return;

        String timestamp = FORMATTER.format(Instant.ofEpochMilli(event.getTimeStamp()));
        String level = event.getLevel().toString();
        String logger = abbreviateLogger(event.getLoggerName());
        String message = event.getFormattedMessage();

        store.add(new RecentLogStore.LogEntry(timestamp, level, logger, message));
    }

    private String abbreviateLogger(String name) {
        String[] parts = name.split("\\.");
        if (parts.length <= 2) return name;
        return parts[parts.length - 1];
    }
}
