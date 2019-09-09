package com.interactions.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Commit Log App.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class CommitLogApplication {
    private static final CommitLogId commitLogId = new CommitLogId();

    /**
     * Convenient method, given a filename, creates and returns a writer.
     *
     * @param fileName
     * @return BufferedWriter
     * @throws IOException
     */
    static BufferedWriter getWriter(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
        return new BufferedWriter(new FileWriter(fileName, true));
    }

    /**
     * Convenient method, forms and returns thread name.
     * @param category
     * @param threadNumber
     * @return thread name
     */
    private static String getThreadName(String category, int threadNumber) {
        return "writer-thread-" + category + threadNumber;
    }

    /**
     * The main application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            AppConfig appConfig = AppConfig.getInstance();
            BufferedWriter fileWriter = getWriter(appConfig.getCommitLogFileName());
            List<Thread> writerThreads = new ArrayList<>();

            // create A category writers
            IntStream.rangeClosed(1, appConfig.getNumberOfCidAWriters()).forEach( i ->
                writerThreads.add(
                    new Thread(
                        new LogWriter(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_A, commitLogId, fileWriter),
                        getThreadName(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_A, i)
                    )
                )
            );
            // create B category writers
            IntStream.rangeClosed(1, appConfig.getNumberOfCidBWriters()).forEach( i ->
                writerThreads.add(
                    new Thread(
                        new LogWriter(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_B, commitLogId, fileWriter),
                        getThreadName(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_B, i)
                    )
                )
            );
            // start writer threads
            writerThreads.stream().forEach(thread ->
                thread.start()
            );

            for (Thread thread : writerThreads) {
                thread.join();
            }

            fileWriter.close();
            System.out.println(CommitLogApplication.class.getSimpleName() + " done...");
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
