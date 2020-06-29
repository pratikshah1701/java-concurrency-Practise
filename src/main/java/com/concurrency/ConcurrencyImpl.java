package com.concurrency;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConcurrencyImpl implements Concurrency {

    private final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void useSequential(List<MyTask> tasks) {
        System.out.println("Run on sequential manner. \nWait...");

        Instant startTime = Instant.now();
        List<Integer> result = tasks.stream()
                .map(MyTask::calculate)
                .collect(Collectors.toList());

        Instant endTime = Instant.now();
        printDuration(tasks, startTime, endTime);
    }

    public void useParallelStream(List<MyTask> tasks) {
        System.out.println("Run using a parallel stream. \nWait...");

        Instant startTime = Instant.now();
        List<Integer> result = tasks.parallelStream()
                .map(MyTask::calculate)
                .collect(Collectors.toList());

        Instant endTime = Instant.now();
        printDuration(tasks, startTime, endTime);
    }

    public void useCompletableFuture(List<MyTask> tasks) {
        System.out.println("Run using CompletableFutures. \nWait...");

        Instant startTime = Instant.now();
        List<CompletableFuture<Integer>> futures = tasks.stream()
                .map(tmpTask -> {

                    return CompletableFuture.supplyAsync(() -> {
                        return tmpTask.calculate();
                    });
                })
                .collect(Collectors.toList());

        List<Integer> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        Instant endTime = Instant.now();
        printDuration(tasks, startTime, endTime);
    }

    public void useCompletableFutureWithExecutor(List<MyTask> tasks, int noOfThreads) {
        System.out.println("Run CompletableFutures with a custom Executor. \nWait...");

        final int NUM_OF_THREADS = noOfThreads;

        Instant startTime = Instant.now();

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(tasks.size(), NUM_OF_THREADS));

        List<CompletableFuture<Integer>> futures = tasks.stream()
                .map(tmpTask -> {
                    return CompletableFuture.supplyAsync(() -> tmpTask.calculate(), executor);
                })
                .collect(Collectors.toList());

        List<Integer> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        Instant endTime = Instant.now();
        printDuration(tasks, startTime, endTime);

        executor.shutdown();
    }

    private void printDuration(List<MyTask> tasks, Instant start, Instant end) {
        Duration timeElapsed = Duration.between(start, end);
        System.out.printf("Processed %d tasks in %d milliseconds\n", tasks.size(), timeElapsed.toMillis());
    }


    public void testCompletableFutureChainWithException() {

		/*CompletableFuture<String> future = new CompletableFuture<>();
		future.thenApply(str -> {
			System.out.println("Stage 1: " + str);
			return "bar";
		});
		future.thenApply(str -> {
			System.out.println("Stage 2: " + str);
			throw new RuntimeException();
		});
		future.thenApply(str -> {
			System.out.println("Stage 3: " + str);
			return "abc";
		});
		future.exceptionally(e -> {
			System.out.println("Exceptionally");
			return null;
		});
		future.complete("foo");*/
    }


    public String postLead(String url) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("https://run.mocky.io/v3/c5365e39-c4ac-4005-bd3c-35477ad1053e?mocky-delay=2000ms"))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "error";
        }
    }

}
