package com.concurrency;

import com.concurrency.b2b.ParallelCallsDemoService;

import java.util.Arrays;
import java.util.List;

public class TestHttpPostwithCompletableFuture {

    public static void main(String[] args) {
        ParallelCallsDemoService parallelCallsDemoService = new ParallelCallsDemoService();
        List<String> testProviders = Arrays.asList("Provider1","Provider2", "Provider3", "Provider4", "Provider5");
        parallelCallsDemoService.getProviders(testProviders);
        
    }
}
