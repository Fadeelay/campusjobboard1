package com.example.campusjobboard.logging;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Thread-safe ring buffer that holds the last N structured log entries for admin viewing. */
@Component
public class RecentLogStore {

    private static final int MAX_ENTRIES = 200;
    private static RecentLogStore instance;

    private final Deque<LogEntry> entries = new ArrayDeque<>();

    public RecentLogStore() {
        instance = this;
    }

    public static RecentLogStore getInstance() {
        return instance;
    }

    public synchronized void add(LogEntry entry) {
        if (entries.size() >= MAX_ENTRIES) {
            entries.pollFirst();
        }
        entries.addLast(entry);
    }

    public synchronized List<LogEntry> getAll() {
        List<LogEntry> copy = new ArrayList<>(entries);
        copy.sort((a, b) -> b.timestamp().compareTo(a.timestamp()));
        return copy;
    }

    public record LogEntry(String timestamp, String level, String logger, String message) {}
}
