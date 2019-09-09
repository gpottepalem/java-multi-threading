package com.interactions.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Log writer thread.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class LogWriter implements Runnable {
    String categoryName; // A, B etc.
    CommitLogId commitLogId;
    BufferedWriter bufferedWriter;

    /**
     * Constructor, creates and initializes an instance of {@link LogWriter}
     *
     * @param categoryName
     * @param commitLogId
     * @param bufferedWriter
     */
    LogWriter(String categoryName, CommitLogId commitLogId, BufferedWriter bufferedWriter) {
        this.categoryName = categoryName;
        this.commitLogId = commitLogId;
        this.bufferedWriter = bufferedWriter;
    }

    /**
     * Writes random number of messages to the log file writer by writing ONE message per each run. Once a message is
     * written, this thread yields for other threads letting another thread to get a chance to write. Finishes once
     * random number messages are successfully written.
     */
    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < random.nextInt(10); i++) {
            synchronized (bufferedWriter) {
                int commitId = commitLogId.nextId();
                String log = categoryName + ": " + commitId + ": sample log by " +
                    this.getClass().getSimpleName() + " :" + Thread.currentThread().getName();
                System.out.println(log);
                try {
                    bufferedWriter.write(log);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    Thread.yield();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
