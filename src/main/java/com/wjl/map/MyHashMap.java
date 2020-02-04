package com.wjl.map;

/**
  *
  * 基于数组+链表的hashMap
  * @data 2018/7/13 10:04
  * @by wjl
*/
public class MyHashMap<K,V> implements MyMap<K,V>{

    private int size;

    //负载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //默认大小
    private static int DEFAULT_INITIAL_SIZE = 16;
    //懒模式
    private Node<K,V>[] table;

    public int size() {
        return size;
    }

    /**
     * 存放一个新的K,V 或者替换已有的K的V
     * @param key
     * @param value
     * @return
     */
    public V put(K key, V value) {
        initTable();
        //如果key为null的话我们就将他添加到第0个位置
        if (key == null){
            return setNull(key,value);
        }
        //扩容机制
        //当数量超过承受限度的时候就进行扩容
        if (size >= DEFAULT_LOAD_FACTOR*DEFAULT_INITIAL_SIZE){
            resize();
        }
        //计算hash,计算index下标
        int index = getIndex(key,DEFAULT_INITIAL_SIZE);
        //通过下标找到该位置并确认他是否已经存在过了
        Node<K, V> node = table[index];
        if (node == null){
            //这里是不存在的情况直接添加新的
            node = new Node<K, V>(key,value,node);
            table[index] = node;
            size++;
            return node.getValue();
        }else {
            //如果存在过了就出现了index碰撞也有可能是覆盖
            //我们将这个位置的所有节点都取出来一遍
            // 并且做出判断如果存在相同的那么我就覆盖
            //不相同我就在后面接一个
            //需要覆盖的情况调用setValue
            return hashCollision(node,key,value,index);
        }
    }

    //扩容方法
    /**
     *
     * @data 2018/7/14 16:39
     * @by wjl
     * 说到底这东西还是个数组
     * 所以在这里我还是采用类似ArrayList的方式扩容
     * 只不过这里挪移数据的时候需要注意hash是会随这扩容产生变化的
     * 所以在挪动数据的时候我们需要重新计算hash的值
     */
    private void resize() {

        //创建新的数组大小为两倍
        Node<K,V>[] newTable = new Node[DEFAULT_INITIAL_SIZE<<1];
        //先将旧的table中的数据取出来重新计算hash
        for (int i = 0; i < DEFAULT_INITIAL_SIZE; i++) {
            Node<K, V> node = table[i];
            //这里我们任然使用while循环来控制判断是否存在下一个
            while (node!=null){
                table[i] = null;//防止资源泄漏
                //计算新的下标
                int index = getIndex(node.getKey(),newTable.length);
                //将他放进去,这里有一点因为可能这个位置之前就值了
                //所以需要将这里的值取出来并且设置为当前的node的下一个再添加到容器中
                Node<K, V> kvNode = newTable[index];
                //取出当前node的下一个备用
                Node<K, V> oldNext = node.next;
                node.next = kvNode;
                newTable[index] = node;
                node = oldNext;
            }
        }
        //循环结束后替换table指向新的table
        //修改各个变量释放不必要的强引用
        table = newTable;
        DEFAULT_INITIAL_SIZE = newTable.length;
        newTable = null;
    }

    private V setNull(K key, V value) {
        Node<K, V> kvNode = table[0];
        return hashCollision(kvNode,key,value,0);
    }

    private V hashCollision(Node<K, V> node, K key,V value,int index) {
        //搞个循环来判断一下
        Node<K,V> nowNode = node;
        while (nowNode != null){
            if (nowNode.getKey().equals(key) || nowNode.getKey() == key){
                //如果他真的完全相等的话,就说明他是相同的需要覆盖
                return nowNode.setValue(value);
            }
            //当下一个不存在的时候就直接推出循环返回结果
            nowNode = nowNode.next;
        }
        //循环结束都未找到需要替换的话就使用新增
        return addNode(node,key,value,index);
    }

    private V addNode(Node<K, V> node, K key, V value,int index) {
        //这里是添加一个新的节点到新的位置上
        //后进先出原则
         node = new Node<K, V>(key,value,node);
        table[index] = node;
        size++;
        return node.getValue();
    }

    private void initTable() {
        if (table == null){
            table = new Node[DEFAULT_INITIAL_SIZE];
        }
    }

    /**
     * 源码中用的看起来很高级的运算方法
     */
    private int getIndex(K key,int length) {
        return (null==key)?0:hash(key,length);
    }

    private int hash(K key,int length) {
        int hashCode = key.hashCode();
        int index = hashCode%length;
        return index;
    }


    public V get(K key) {
        if (key == null){
            return getNull();
        }
        Node<K,V> node = getNode(key);
        return (node == null)? null:node.getValue();
    }

    private V getNull() {
        Node<K, V> kvNode = table[0];
        while (kvNode != null){
            if (kvNode.getKey() == null){
                return kvNode.value;
            }
            kvNode = kvNode.next;
        }
        return null;
    }

    //查找指定key的node
    private Node<K, V> getNode(K key) {
        int index = getIndex(key,DEFAULT_INITIAL_SIZE);
        Node<K, V> kvNode = table[index];
        while (kvNode != null){
            if (kvNode.getKey().equals(key) || kvNode.getKey()==key){
                return kvNode;
            }
            kvNode = kvNode.next;
        }
        return null;
    }

    //测试用的打印代码

    public void print(){
        for (int i = 0; i < DEFAULT_INITIAL_SIZE; i++) {
            Node<K, V> kvNode = table[i];
            System.out.print("下标["+i+"]");
            while (kvNode != null){
                System.out.print("key:"+kvNode.getKey()+"value:"+kvNode.getValue()+" "+"hashCode:"+ kvNode.getKey().hashCode() +" ");
                kvNode = kvNode.next;
            }
            System.out.println();
        }
    }

    private class Node<K,V> implements Entry<K,V>{

        K key;

        V value;

        Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

}
