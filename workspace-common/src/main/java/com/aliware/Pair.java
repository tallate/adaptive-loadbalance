package com.aliware;

/**
 * @param <K> 表示 key 或 left 值
 * @param <V> 表示 value 或 right 值
 */
public class Pair<K, V> {

    private K k;
    private V v;

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    private Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public Pair<K, V> setK(K k) {
        this.k = k;
        return this;
    }

    public V getV() {
        return v;
    }

    public Pair<K, V> setV(V v) {
        this.v = v;
        return this;
    }
}
