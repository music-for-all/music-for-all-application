package com.musicforall.persistence;

/**
 * @author ENikolskiy.
 */
public interface KeyValueRepository<K, V> {
    void put(K key, V value);

    V get(K key);

    V remove(K key);
}
