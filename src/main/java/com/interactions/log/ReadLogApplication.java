package com.interactions.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

/**
 * Read Log Application.
 *
 * @author gpottepalem
 * Created on Sep 07, 2019
 */
public class ReadLogApplication {

    /**
     * The main application. Spawns a {@link LogFileReader} thread and multiple configurable {@link LogReader} threads.
     * @param args
     */
    public static void main(String[] args) {
        try {
            // blocking queues for two categories of reader threads
            BlockingQueue<String> blockingQueueA = new ArrayBlockingQueue<>(5);
            BlockingQueue<String> blockingQueueB = new ArrayBlockingQueue<>(5);

            Thread logFileReaderThread = startLogFileReader(blockingQueueA, blockingQueueB);
            List<Thread> readerThreads = startLogReaders(blockingQueueA, blockingQueueB);

            logFileReaderThread.join();
            for (Thread thread : readerThreads) {
                thread.join();
            }
            System.out.println(ReadLogApplication.class.getSimpleName() + " done...");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method, spawns multiple {@link LogReader} threads with two categories of threads that can be identified by
     * their category names.
     *
     * @param blockingQueueA
     * @param blockingQueueB
     * @return list of spawned threads
     * @throws IOException
     */
    private static List<Thread> startLogReaders(BlockingQueue<String> blockingQueueA, BlockingQueue<String> blockingQueueB)
            throws IOException {
        AppConfig appConfig = AppConfig.getInstance();
        List<Thread> threads = new ArrayList<>();

        // create A category readers
        IntStream.rangeClosed(1, appConfig.getNumberOfCidAWriters()).forEach(i ->
            threads.add(
                new Thread(
                    new LogReader(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_A, blockingQueueA),
                    getThreadName(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_A, i)
                )
            )
        );

        // create B category readers
        IntStream.rangeClosed(1, appConfig.getNumberOfCidBWriters()).forEach(i->
            threads.add(
                new Thread(
                    new LogReader(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_B, blockingQueueB),
                    getThreadName(AppConfig.PROPERTY_KEY_N_CID_CATEGORY_B, i)
                )
            )
        );

        // start reader threads
        threads.stream().forEach(thread->
            thread.start()
        );
        return threads;
    }

    /**
     * Convenient method, forms and returns thread name.
     * @param category
     * @param threadNumber
     * @return thread name
     */
    private static String getThreadName(String category, int threadNumber) {
        return "reader-thread-" + category + threadNumber;
    }

    /**
     * Helper method, spawns a {@link LogFileReader} thread
     * @param blockingQueueA the blocking queue for category A threads
     * @param blockingQueueB the blocking queue for category B threads
     * @return the thread
     */
    private static Thread startLogFileReader(BlockingQueue<String> blockingQueueA, BlockingQueue<String> blockingQueueB) {
        Thread logFileReader = new Thread(new LogFileReader(blockingQueueA, blockingQueueB), "LogFileReader");
        logFileReader.start();
        return logFileReader;
    }
}
