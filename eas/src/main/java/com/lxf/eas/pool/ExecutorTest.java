package com.lxf.eas.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @Classname ExecutorTest
 * @Description
 * @Date 2024/3/29 15:20
 * @Author lxf
 */
@Component
@Slf4j
public class ExecutorTest {

    @Resource(name = "consumerQueueThreadPool")
    private ExecutorService consumerQueueThreadPool;

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(5),
            new ThreadFactoryBuilder().setNameFormat("i-pool-thread-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EXECUTOR.execute(()->{
            System.out.println(Thread.currentThread().getName() +"【1】");
        });
        EXECUTOR.execute(()->{
            System.out.println(Thread.currentThread().getName() +"【2】");
        });
        //EXECUTOR.shutdown();


        CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName() +"【3】");
        },EXECUTOR);

        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            String word = Thread.currentThread().getName() + "【4】";
            return word;
        }, EXECUTOR);

        System.out.println(supplyAsync.join());
        //System.out.println(supplyAsync.get());
        CompletableFuture<String> thenApply = CompletableFuture
                .supplyAsync(() -> Thread.currentThread().getName() + "【5】", EXECUTOR)
                .thenApply((p) -> p + ":thenApply");
        System.out.println(thenApply.join());

        CompletableFuture<Void> thenAccept = CompletableFuture
                .supplyAsync(() -> Thread.currentThread().getName() + "【6】", EXECUTOR)
                .thenAccept((p) -> System.out.println(p + ":thenAccept"));


        CompletableFuture<String> supplyAsync10 = CompletableFuture.supplyAsync(() -> {
            String word = Thread.currentThread().getName() + "【10】";
            System.out.println(word);
            return word;
        }, EXECUTOR);

        CompletableFuture<String> supplyAsync11 = CompletableFuture.supplyAsync(() -> {
            String word = Thread.currentThread().getName() + "【11】";
            System.out.println(word);
            return word;
        }, EXECUTOR);

        CompletableFuture.allOf(supplyAsync10,supplyAsync11).join();


        EXECUTOR.shutdown();
    }

}
