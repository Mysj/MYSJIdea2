package com.mysj.idea.tool;

import com.mysj.idea.Linked;
import com.mysj.idea.Page;

import java.io.BufferedReader;

public class RWLinkedPageTool {

    /**
     * 读写地址变量的工具
     * @param linked
     * @param bufferedReader
     */
    public static void RWLinkedPage(Linked linked, BufferedReader bufferedReader) throws Exception{
        String strLine = null;
        long x = 0;
        int period = 1;//周期
        while(null != (strLine = bufferedReader.readLine())) {
            //第一个周期，先初始化链表、页面等信息，进行第一个周期内的读写
            if (period == 1){

            }else {

            }

            //每行有三个数据，分别为 读写、大小、地址
            String[] s = strLine.split(" ");
            if(s.length!=3) continue;
            //1.当链表为空时，先存放一页，往该地址中放变量
            if (linked == null){
                Page page = new Page();
                page.put(s[2],4);//设定每个地址变量为4bit
                linked.addLast(page);
                continue;
            }

            int subscript = linked.contains(s[2]);
            //2.当链表中没有该地址时，找一页存放
            if (subscript == 0){//意味着链表中没有该地址
                boolean bool = linked.add(s[2], 4);
                //如果链表中存放失败、页数（大小）不够，则新建一个页来存放
                if (bool == false){
                    Page page = new Page();
                    page.put(s[2],4);
                    linked.addLast(page);
                }
            }else {     //3.在链表中找到该地址
                if ("Write".equals(s[0])){
                    linked.write(s[2],4,subscript);
                }
                if ("Read".equals(s[0])){
                    linked.read(s[2],4,subscript);
                }
            }
            x++;

        }
    }

    public void RWPage(){

    }
}
