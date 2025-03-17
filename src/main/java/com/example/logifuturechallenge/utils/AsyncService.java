package com.example.logifuturechallenge.utils;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.*;

@Configuration
@RequiredArgsConstructor
public class AsyncService {

    private final ConcurrentHashMap<Integer, ThreadPoolExecutor> threadPools = new ConcurrentHashMap<>();
    private static final int MAX_POOL_SIZE = 50;

    public <T> T submitTask(String id, Callable<T> task) throws Throwable {
        var future = generateThread(id).submit(task);
        try {
            return future.get();
        } catch (CancellationException | InterruptedException | ExecutionException exception) {
            throw exception.getCause();
        }
    }

    public ExecutorService generateThread(String id) throws InterruptedException {
        while (threadPools.size() > MAX_POOL_SIZE) {
            Thread.sleep(1000);
        }
        var threadId = Math.abs(id.hashCode()) % MAX_POOL_SIZE;
        if (threadPools.get(threadId) == null) {
            threadPools.put(threadId,
                    new ThreadPoolExecutor(0, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>()));
        }
        return threadPools.get(threadId);
    }


    @Scheduled(fixedRate = 500)
    public void removeEmptyExecutors() {
        for (Map.Entry<Integer, ThreadPoolExecutor> entry : threadPools.entrySet()) {
            var executor = entry.getValue();
            if (executor.getPoolSize() == 0 && executor.getQueue().isEmpty()) {
                executor.shutdown();
                threadPools.remove(entry.getKey());
            }
        }
    }

    @PreDestroy
    public void cleanUp() {
        threadPools.forEach((id, executor) -> executor.shutdown());
    }
}
