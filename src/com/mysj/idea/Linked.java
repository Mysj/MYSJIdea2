package com.mysj.idea;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.SplittableRandom;

public class Linked {

    class Node{
        private Page t;
        private Node next;
        public Node(Page t,Node next){
            this.t = t;
            this.next = next;
        }
        public Node(Page t){
            this(t,null);
        }
    }
    private Node head;    		//头结点
    private int size;			//链表元素个数
    //构造函数
    public Linked(){
        this.head = null;
        this.size = 0;
    }

    //获取链表元素的个数
    public int getSize(){
        return this.size;
    }
    //判断链表是否为空
    public boolean isEmpty(){
        return this.size == 0;
    }
    //链表头部添加元素
    public void addFirst(Page t){
        Node node = new Node(t);	//节点对象
        node.next = this.head;
        this.head = node;
        // this.head = new Node(e,head);等价上述代码
        this.size++;
    }
    //向链表尾部插入元素
    public void addLast(Page t){
        this.add(t, this.size);
    }

    public boolean add(String add,int size){
        Node cur = this.head;
        int i = 1;
        while (cur != null){
            if (cur.t.getSize() + size <= 4*1024){
                boolean b = cur.t.put(add, size);
                if (!b){
                    System.out.println("编号 "+i+" 存放失败！！！");
                }
                return true;
            }
            cur =cur.next;
            i++;
        }
        return false;
    }



    //先找到该地址变量的位置，在写
    public int write(String add,int size,int subscript){
        Node cur = this.head;
        for (int i = 0;i < subscript;i++){
            cur = cur.next;
        }
        Page page = cur.t;
        page.write(add);
        Map map = page.getMap();
        Integer[] o = (Integer[]) map.get(add);
        System.out.println("写 "+ add + "第 " + o[2] +"次");
        return 0;
    }
    //先找到该地址变量的位置，再读
    public int read(String add,int size,int subscript){
        Node cur = this.head;
        for (int i = 0;i < subscript;i++){
            cur = cur.next;
        }
        cur.t.read(add);
        Page page = cur.t;
        page.read(add);
        Map map = page.getMap();
        Integer[] o = (Integer[]) map.get(add);
        System.out.println("读 "+ add + "第 " + o[2] +"次");
        return 0;
    }

    //向链表中间插入元素
    public void add(Page t,int index){      
        if (index <0 || index >size){
            throw new IllegalArgumentException("index is error");
        }
        if (index == 0){
            this.addFirst(t);
            return;
        }
        Node preNode = this.head;
        //找到要插入节点的前一个节点
        for(int i = 0; i < index-1; i++){
            preNode = preNode.next;
        }
        Node node = new Node(t);
        //要插入的节点的下一个节点指向preNode节点的下一个节点
        node.next = preNode.next;
        //preNode的下一个节点指向要插入节点node
        preNode.next = node;
        this.size++;
    }
    //删除链表元素
    public void remove(Page t){
        if(head == null){
            System.out.println("无元素可删除");
            return;
        }
        //要删除的元素与头结点的元素相同
        while(head != null && head.t.equals(t)){
            head = head.next;
            this.size--;
        }
        /**
         * 上面已经对头节点判别是否要进行删除
         * 所以要对头结点的下一个结点进行判别
         */
        Node cur = this.head;
        while(cur != null && cur.next != null){
            if(cur.next.t.equals(t)){
                this.size--;
                cur.next = cur.next.next;
            }
            else cur = cur.next;
        }

    }
    //删除链表第一个元素
    public Page removeFirst(){
        if(this.head == null){
            System.out.println("无元素可删除");
            return null;
        }
        Node delNode = this.head;
        this.head = this.head.next;
        delNode.next =null;
        this.size--;
        return delNode.t;
    }
    //删除链表的最后一个元素
    public Page removeLast(){
        if(this.head == null){
            System.out.println("无元素可删除");
            return null;
        }
        //只有一个元素
        if(this.getSize() == 1){
            return this.removeFirst();
        }
        Node cur = this.head;	//记录当前结点
        Node pre = this.head;	//记录要删除结点的前一个结点
        while(cur.next != null){
            pre = cur;
            cur = cur.next;
        }
        pre.next = cur.next;
        this.size--;
        return cur.t;
    }
    //链表中是否包含某个元素，返回值为0，则没有找到，返回值为i则为该地址在该链表的位置
    public int contains(String addr){
        int i = 0;
        Node cur = this.head;
        while(cur != null){
            Page page = cur.t;

            if(page.getMap().containsKey(addr)){
                return i;
            } else {
                cur = cur.next;
                i++;
            }
        }
        return 0;
    }

    /**
     * 一个周期结束、或者一个周期新开始，会将是否被访问重置
     */
    public void resetCycleAccess(){
        Node cur = this.head;
        while (cur != null){
            Page page = cur.t;
            //当该page页面该周期被访问，即flag==true，则重置为false
            if (page.getFlag() == true){
                page.setFlag();
            }
            cur = cur.next;
        }
    }


    /**
     * 打印该链表中每个页面的页面占用大小
     */
    public void getPageSize(){
        Node cur = this.head;
        int i = 1;
        while (cur != null){
            Page page = cur.t;
            System.out.println("第" + i + "个页面的大小为：" + page.getSize());
            i++;
            cur = cur.next;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Node cur = this.head;
        while(cur != null){
            sb.append(cur.t+"->");
            cur = cur.next;
        }
        sb.append("NULL");
        return sb.toString();
    }


}


