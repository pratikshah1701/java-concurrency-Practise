package com.concurrency;

import com.concurrency.reactor.utils.Options;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class TestConcurrencyWithReactor {
    
    public static void main(String[] args) {
        
        Concurrency concurrency = new ConcurrencyImpl();

        Instant startTime = Instant.now();
        
        List<String> result = Flux.fromIterable(Arrays.asList("Provider1","Provider2", "Provider3", "Provider4", "Provider5"))
                .flatMap(url -> Mono.just(url)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(___ -> Options.logIdentity(url, "Mono.just() --> getPayload"))
                .map(concurrency::postLead)
                .doOnNext(___ -> Options.logIdentity(url, "map() -> httpPost")))
                .collectList()
                .doOnNext(file -> Options.logIdentity(file, "collectList()"))
                .doOnSuccess(postResponse -> System.out.println("response size:" + postResponse.size()))
                .block();
        
        Instant endTime = Instant.now();
        Duration timeElapsed = Duration.between(startTime, endTime);
        System.out.printf("Processed %d httpPost with each  2 sec delay in %d milliseconds\n", result.size(), timeElapsed.toMillis());

    }

}
