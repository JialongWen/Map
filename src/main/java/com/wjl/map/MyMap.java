package com.wjl.map;

public interface MyMap<K,V> {

    int size();

    V put(K key,V value);

    V get(K key);

    interface Entry<K,V>{
        K getKey();

        V getValue();

        V setValue(V value);
    }

}
