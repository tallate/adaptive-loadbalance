package com.aliware;

import java.util.Comparator;

/**
 * 循环队列
 */
public class CircularQueue<T> {

    /**
     * 数组存储队列内元素
     */
    private T[] queue;
    /**
     * 队列头部
     */
    private int front;
    /**
     * 队列尾部
     */
    private int back;
    /**
     * 队列内元素
     */
    private int size;

    @SuppressWarnings("unchecked")
    public CircularQueue(int n) {
        // 分配n + 1是为了判断队列是否满了、空了方便
        this.queue = (T[]) new Object[n + 1];
        front = back = size = 0;
    }

    public int size() {
        return queue.length;
    }

    public int emptySize() {
        return queue.length - size - 1;
    }

    /**
     * 找>=key的左边界
     * 二分查找找下界（如1, 3, 3, 5, 8, 10会找到下标1处的3）
     */
    private int findLb(T[] a, int lb, int rb, T key, Comparator<T> com) {
        while (lb < rb) {
            int mid = (lb + rb) / 2;
            if (a[mid] >= key) {
                rb = mid;
            } else {
                lb = mid + 1;
            }
        }
        return lb;
    }

    /**
     * 二分查找
     * 前提是队列中的值是从小到大添加的
     *
     * @param key 查找目标
     */
    public int binsearch(T key, Comparator<T> com) {
        return findLb(queue, front, back, key, com);
    }

    public int mid(int pos1, int pos2) {

    }

    public int getFrontPos() {
        return front;
    }

    public int getBackPos() {
        return back;
    }

    /**
     * 队列中两个位置之间的差距
     * pos2相对pos1来说更晚添加进来
     * TODO: 假设不会有pos2比pos1多绕了一圈的情况
     */
    public int getInterval(int pos1, int pos2) {
        int d = pos2 - pos1;
        // TODO: 队列长度比实际capacity多1，需不需要减1？
        return d < 0 ? queue.length - d : d;
    }

    /**
     * 获取队列内第n个位置的元素
     */
    public T get(int n) {
        // 获取队列从front开始第n个
        if (n > size) {
            throw new IllegalArgumentException("超过队列大小");
        }
        return queue[(front - n + queue.length) % queue.length];
    }

    public boolean isEmpty() {
        return front == back;
    }

    public boolean isFull() {
        return (back + 1) % queue.length == front;
    }

    /**
     * 加到队尾
     */
    public void offer(T t) {
        size++;
        back = (back + 1) % queue.length;
        queue[back] = t;
        // 如果是满的情况，循环
        if (back == front) {
            front++;
        }
    }

    /**
     * 从队首取出
     */
    public T poll() {
        if (isEmpty()) {
            throw new RuntimeException("不能对空队列调用poll");
        }
        size--;
        front = (front + 1) % queue.length;
        return queue[front];
    }

    /**
     * 获取队首元素
     */
    public T getFront() {
        if (isEmpty()) {
            throw new RuntimeException("不能对空队列调用getFront");
        }
        return queue[(front + 1) % queue.length];
    }

    /**
     * 获取队尾元素
     */
    public T getBack() {
        if (isEmpty()) {
            throw new RuntimeException("不能对空队列调用getBack");
        }
        return queue[back];
    }

}
