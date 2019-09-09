package com.interactions.log;

/**
 * Holds a volatile id for use across multiple threads.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class CommitLogId {
    /**
     * Volatile commit id.
     * A unique id across log messages logged by various categories of {@link LogWriter}s
     * */
    private volatile int id = 100;

    /**
     * Increments id by and and returns.
     * @return next id
     */
    public int nextId() {
        id++;
        return id;
    }
}
