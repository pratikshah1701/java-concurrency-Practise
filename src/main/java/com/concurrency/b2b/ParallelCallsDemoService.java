package com.concurrency.b2b;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ParallelCallsDemoService {


    HttpClient client = HttpClient.newHttpClient();

    public List<String> getProviders(List<String> ids) {
        Instant startTime = Instant.now();
        List<CompletableFuture<String>> futures =
                ids.stream()
                          .map(id -> doHttpPost(id))
                          .collect(Collectors.toList());

        List<String> result =
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        Instant endTime = Instant.now();
        Duration timeElapsed = Duration.between(startTime, endTime);
        System.out.printf("Processed %d tasks in %d milliseconds\n", ids.size(), timeElapsed.toMillis());
        return result;
    }


    CompletableFuture<String> doHttpPost(String id) {
        System.out.println("calling provider :" + id);
        return CompletableFuture.supplyAsync(() -> {
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
        );
        
    }

}
