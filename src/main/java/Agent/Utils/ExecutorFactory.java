package Agent.Utils;

import java.util.concurrent.*;

/**
 * A factory for creating worker thread pools.
 */
public class ExecutorFactory
{
    private ExecutorFactory()
    {}

    /**
     * Creates a worker thread pool using the optimal number of threads for the current system.
     *
     * @return an {@link Executor} instance representing the worker thread pool.
     */
    public static Executor createWorkerPool()
    {
        int numOfCores = Runtime.getRuntime().availableProcessors();
        int minCores = numOfCores/4;
        int maxCores = numOfCores*2;
        return new ThreadPoolExecutor(minCores, maxCores, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}
