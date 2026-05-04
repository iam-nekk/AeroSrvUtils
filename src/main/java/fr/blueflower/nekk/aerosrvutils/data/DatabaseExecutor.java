package fr.blueflower.nekk.aerosrvutils.data;

import net.minecraft.server.MinecraftServer;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

// when running queries, it will block the thread that minecraft runs on until it's done
// so, we run queries on a separate thread using an executor
public class DatabaseExecutor {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public static void runAsync(Runnable task){
        EXECUTOR.submit(task);
    }

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> task){
        return CompletableFuture.supplyAsync(task, EXECUTOR);
    }

    public static <T> void thenOnMainThread(CompletableFuture<T> future, MinecraftServer server, Consumer<T> action){
        future.thenAccept(result -> server.execute(()-> action.accept(result)));
    }

    public static void shutdown(){
        EXECUTOR.shutdown();
    }


}
