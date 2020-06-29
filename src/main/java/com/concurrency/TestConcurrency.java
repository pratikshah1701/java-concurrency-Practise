package com.concurrency;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestConcurrency {
	private final static Logger LOGGER =
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static final int MAX_TASKS = 5;
	public static final int SECONDS = 2;
	public static final int NO_OF_THREADS_FOR_CUSTOM_EXECUTOR = 50;

	public static void main(String[] args) throws Exception {

		List<MyTask> tasks = IntStream.range(0,MAX_TASKS) // MAX_TASKS
					.mapToObj(i -> new MyTask(SECONDS))
					.collect(Collectors.toList());

		Concurrency concurrency = new ConcurrencyImpl();

		//sequential Stream
		//concurrency.useSequential(tasks);

		//CompletableFutureWithExecutor
		//concurrency.useCompletableFutureWithExecutor(tasks, NO_OF_THREADS_FOR_CUSTOM_EXECUTOR);

		
		//CompletableFutureWithDefaultExecutor
		//concurrency.useCompletableFuture(tasks);
		
		//using parallel stream
		//concurrency.useParallelStream(tasks);
		
		/*
		//Test multiple ping request using CompletableFutureWithExecutor
		IntStream.range(1,100).parallel().forEach((i)-> {
			try {
				Thread.sleep(1000);
				concurrency.useCompletableFutureWithExecutor(tasks, NO_OF_THREADS_FOR_CUSTOM_EXECUTOR);
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			}
			
		});
		*/
			
		
		
		
	}
	
	
}
