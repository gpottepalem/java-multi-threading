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
    public volatile int id = 100;
}
