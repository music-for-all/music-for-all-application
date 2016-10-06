package com.musicforall.common.cache;

/**
 * @author ENikolskiy.
 */
public interface CacheProvider<K, V> {
    V put(K key, V value);

    V get(K key);

    V remove(K key);
}
