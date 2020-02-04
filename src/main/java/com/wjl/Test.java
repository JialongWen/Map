package com.wjl;

import com.wjl.demo.LinkedListHashMapDemo;
import com.wjl.map.MyHashMap;
import com.wjl.map.MyMap;

public class Test {
    public static void main(String[] args) {
       /* LinkedListHashMapDemo<String,String> linkedListHashMapDemo = new LinkedListHashMapDemo<String, String>();
        linkedListHashMapDemo.put("1号", "aaa");
        linkedListHashMapDemo.put("b", "bbb");
        linkedListHashMapDemo.put("14号", "cccc");
        System.out.println(linkedListHashMapDemo.get("1号"));*/

        MyMap<String,String> myHashMap = new MyHashMap<String, String>();
        myHashMap.put("1号","1号");
        myHashMap.put("2号","2号");
        myHashMap.put("3号","3号");
        myHashMap.put("4号","4号");
        myHashMap.put("5号","5号");
        myHashMap.put("6号","6号");
        myHashMap.put("7号","7号");
        myHashMap.put("8号","8号");
        myHashMap.put("9号","9号");
        myHashMap.put("10号","10号");
        System.out.println("扩容前：");
        ((MyHashMap<String, String>) myHashMap).print();
        myHashMap.put("11号","11号");
        myHashMap.put("12号","12号");
        myHashMap.put("13号","13号");
        myHashMap.put("14号","14号");
        myHashMap.put("15号","15号");
        myHashMap.put("16号","16号");
        myHashMap.put("17号","17号");
        myHashMap.put("18号","18号");
        System.out.println("扩容后：");
        ((MyHashMap<String, String>) myHashMap).print();
    }
}
