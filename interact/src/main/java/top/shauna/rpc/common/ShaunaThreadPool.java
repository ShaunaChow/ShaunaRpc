package top.shauna.rpc.common;

import top.shauna.rpc.config.PubConfig;

import java.util.concurrent.*;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class ShaunaThreadPool {

    public static ExecutorService threadPool;
    static {
        int cores = (PubConfig.getInstance().getThreadPoolNums()==null||PubConfig.getInstance().getThreadPoolNums().equals(0))
                    ?Runtime.getRuntime().availableProcessors()*2
                    :PubConfig.getInstance().getThreadPoolNums();
        threadPool = new ThreadPoolExecutor(
                cores,
                cores,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(cores/4+1),
                new ThreadPoolExecutor.CallerRunsPolicy()
                );
    }
}
