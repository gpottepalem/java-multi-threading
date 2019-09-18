package com.interactions.log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds a volatile id for use across multiple threads.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class CommitLogId {
    /**
     * Thread-safe, non-blocking (no synchronized or lock overhead) atomic commit id.
     * A unique id across log messages logged by various categories of {@link LogWriter}s
     * */
    private AtomicInteger id = new AtomicInteger(100);

    /**
     * Increments id by 1 and and returns.
     * @return next id
     */
    public int nextId() {
        return id.addAndGet(1);
    }
}
