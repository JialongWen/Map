package com.wjl.demo;

import java.util.LinkedList;

/**
 *
 *整理一下思路
 *数组加LinkedList（双向链表）
 *
 * @data 2019/1/14 21:52
 * @by wjl
*/
public class LinkedListHashMapDemo<K,V> {

    private int size;
    //这里暂时写死数组大小
    //这里表示存储的容器是一个数组，数组的每个单位都是一个链表数据结构
    private LinkedList<Entry<K,V>>[] tables = new LinkedList[998];

    public V put(K key,V value){
        //key为空
        if (key == null){

        }
        int hash = getHash(key);
        //添加方法我们需要考虑的就是hash碰撞（冲突），或者是否需要扩容
        LinkedList<Entry<K,V>> table = tables[hash];
        if (table == null) {
            //当这个hash位置没有数据的时候直接存入一个新的
            Entry<K,V> newEntry = new Entry<K, V>(key,value);
            table = new LinkedList<Entry<K, V>>();
            table.add(newEntry);
            tables[hash] = table;
            size++;
        }else {
            //这时候就可以认为是出现hash碰撞的情况
            //此时实际上有两中情况出现
            //1.两者实际上并不是同一个Key,需要添加新的Entry到该位置的链表上
            /*
            这一段错了不行
            for (Entry<K, V> kvEntry : table) {
                if (kvEntry.key.equals(key) || kvEntry.key == key){
                    Entry<K,V> newEntry = new Entry<K, V>(key,value);
                    tables[hash].add(newEntry);
                    size++;
                    return newEntry.value;
                }
            }*/
            for (Entry<K, V> kvEntry : table) {
                //2.同一个Key的情况，需要覆盖操作
                if (kvEntry.key.equals(key) || kvEntry.key == key){
                    kvEntry.setEntry(key,value);
                    return kvEntry.value;
                }
            }

            Entry<K,V> newEntry = new Entry<K, V>(key,value);
            tables[hash].add(newEntry);
            size++;
            return newEntry.value;
        }
       return value;
    }



    public V get(K key){
        int hash = getHash(key);
        Entry<K, V> entry = getEntry(hash, key);
        return (entry == null)? null : entry.value;
    }

    private Entry<K,V> getEntry(int hash, K key) {
        LinkedList<Entry<K, V>> table = tables[hash];
        if (table == null){
            return null;
        }
        for (Entry<K, V> kvEntry : table) {
            if (kvEntry.key.equals(key)){
                return kvEntry;
            }
        }
        return null;
    }

    private int getHash(K key) {
        int hashCode = key.hashCode();
        int hash = hashCode&tables.length;
        return hash;
    }

    private class Entry<K,V>{
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public V setEntry(K key,V value){
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

}
