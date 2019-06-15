package com.aliware;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 这个工具尽可能吞掉线程操作中可能出现的 InterruptedException 异常
 *
 * @see Guava - Uninterruptibles
 */
public final class Uninterruptibles {

    public static void awaitUninterruptibly(CountDownLatch latch) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    latch.await();
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static boolean awaitUninterruptibly(CountDownLatch latch,
            long timeout, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;

            while (true) {
                try {
                    // CountDownLatch treats negative timeouts just like zero.
                    return latch.await(remainingNanos, NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void joinUninterruptibly(Thread toJoin) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    toJoin.join();
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <V> V getUninterruptibly(Future<V> future)
            throws ExecutionException {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return future.get();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <V> V getUninterruptibly(
            Future<V> future, long timeout, TimeUnit unit)
            throws ExecutionException, TimeoutException {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;

            while (true) {
                try {
                    // Future treats negative timeouts just like zero.
                    return future.get(remainingNanos, NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void joinUninterruptibly(Thread toJoin,
            long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.timedJoin() treats negative timeouts just like zero.
                    NANOSECONDS.timedJoin(toJoin, remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    queue.put(element);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(sleepFor);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                    // TimeUnit.sleep() treats negative timeouts just like zero.
                    NANOSECONDS.sleep(remainingNanos);
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static boolean tryAcquireUninterruptibly(
            Semaphore semaphore, long timeout, TimeUnit unit) {
        return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
    }

    public static boolean tryAcquireUninterruptibly(
            Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
        boolean interrupted = false;
        try {
            long remainingNanos = unit.toNanos(timeout);
            long end = System.nanoTime() + remainingNanos;

            while (true) {
                try {
                    // Semaphore treats negative timeouts just like zero.
                    return semaphore.tryAcquire(permits, remainingNanos, NANOSECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Uninterruptibles() {
    }
}
