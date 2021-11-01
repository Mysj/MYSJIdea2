package com.mysj.idea;

import java.util.HashMap;
import java.util.Map;

/**
 * Page类，就是一个页面
 * 大小设置为4KB
 * 存放变量，变量的总大小不能超过4KB
 */
public class Page {
    private int size = 0;//记录本页面的大小
    private long accessNumber = 0;//记录本页面的访问次数
    //private int[] variable;//存放的变量，这里就是地址
    private Map<String,Integer[]> map = new HashMap<String, Integer[]>();//存放地址，和读写次数， 数组存放变量大小，读次数，写次数
    private Boolean flag = false;//当前周期页面是否被访问

    private Boolean isSlc = true;//页面默认为SLC

    public void setIsSlc(){
        this.isSlc = false;//说明是MLC
    }
    public Boolean getIsSlc(){
        return isSlc;
    }


    public void setSize(int size){
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
    public void setFlag(){
        this.flag = !flag;
    }
    public Boolean getFlag(){
        return flag;
    }
    public Map getMap(){
        return map;
    }
    public long getAccessNumber(){
        return accessNumber;
    }

    /**
     * 判断本页面能否存入该地址在链表中判断
     * @param add
     * @param size
     */
    public boolean put(String add,int size){

        if (this.size + size <=4*1024*8){
            map.put(add, new Integer[]{size,0,1});
            this.size += size;
            accessNumber++;
            return true;

        }else {
            return false;
        }
    }

    /**
     * 写该地址变量
     * @param add
     * @return
     */
    public int write(String add){
        accessNumber++;
        flag = true;
        map.get(add)[2]++;
        return 0;
    }
    public int read(String add){
        accessNumber++;
        flag = true;
        map.get(add)[1]++;
        return 0;
    }



}
