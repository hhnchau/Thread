package vn.ptt.threadhandler;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultExecutorSupplier {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mForBackgroundTasks;
    private final ThreadPoolExecutor mForLightWeightBackgroundTasks;
    private final Executor mMainThreadExecutor;
    private final PriorityThreadPoolExecutor mForPriorityBackgroundTasks;

    private static DefaultExecutorSupplier instance;

    public static DefaultExecutorSupplier getInstance() {
        if (instance == null)
            synchronized (DefaultExecutorSupplier.class) {
                instance = new DefaultExecutorSupplier();
            }
        return instance;
    }


    private DefaultExecutorSupplier() {
        ThreadFactory backgroundPriorityThreadFactory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

        mForBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory
        );

        mForLightWeightBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory
        );

        mMainThreadExecutor = new MainThreadExecutor();

        mForPriorityBackgroundTasks = new PriorityThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory
        );
    }

    public ThreadPoolExecutor forBackgroundTasks() {
        return mForBackgroundTasks;
    }

    public ThreadPoolExecutor forLightWeightBackgroundTask() {
        return mForLightWeightBackgroundTasks;
    }

    public Executor forMainThreadTask() {
        return mMainThreadExecutor;
    }

    public PriorityThreadPoolExecutor forPriorityBackgroundTasks() {
        return mForPriorityBackgroundTasks;
    }


    private class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            handler.post(runnable);
        }
    }


    private class PriorityThreadFactory implements ThreadFactory {
        private final int mThreadPriority;

        PriorityThreadFactory(int threadPriorityBackground) {
            mThreadPriority = threadPriorityBackground;
        }

        @Override
        public Thread newThread(final Runnable runnable) {
            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    try {
                        Process.setThreadPriority(mThreadPriority);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    runnable.run();
                }
            };
            return new Thread(runnable1);
        }
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        IMMEDIATE
    }

    public static class PriorityRunnable implements Runnable {
        private Priority priority;

        public PriorityRunnable(Priority priority) {
            this.priority = priority;
        }

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        @Override
        public void run() {

        }
    }

    public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

        public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, PriorityBlockingQueue<Runnable>workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        @Override
        public Future<?> submit(Runnable task) {
            PriorityFutureTask futureTask = new PriorityFutureTask((PriorityRunnable) task);
            execute(futureTask);
            return futureTask;
        }

        private final class PriorityFutureTask extends FutureTask<PriorityRunnable> implements Comparable<PriorityFutureTask> {
            private final PriorityRunnable priorityRunnable;

            public PriorityFutureTask(PriorityRunnable priorityRunnable) {
                super(priorityRunnable, null);
                this.priorityRunnable = priorityRunnable;
            }

            @Override
            public int compareTo(PriorityFutureTask priorityFutureTask) {
                Priority p1 = priorityRunnable.getPriority();
                Priority p2 = priorityFutureTask.priorityRunnable.getPriority();
                return p2.ordinal() - p1.ordinal();
            }
        }
    }
}