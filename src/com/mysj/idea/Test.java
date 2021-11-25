package com.mysj.idea;

import com.mysj.idea.tool.RWLinkedPageTool;

import java.io.BufferedReader;

import static com.mysj.idea.tool.RWFileTool.ReadFile;
import static com.mysj.idea.tool.RWLinkedPageTool.RWLinkedPage;

/**
 * 目前实现：单个链表的存放地址变量
 * 还未实现：
 *      1、分周期运行：根据读取数据条数来划分周期、每次周期结束进行冷热页面移动、移动后把页面的是否访问重置
 *      2、冷热面移动功能的实现
 *      3、SLC/MLC的转换，设定阈值（先不用成本效应模型）
 */
public class Test {
    public static void main(String[] args) throws Exception {
        /*Linked linked = new Linked();
        for(int i = 0; i < 10; i++){
            linked.addFirst(new Page());
            System.out.println(linked);
        }
        System.out.println(linked.getSize());*/
        //使用RWLinkedPageTool工具来进行操作
        //RWLinkedPageTool rwLinkedPageTool = new RWLinkedPageTool();
        Linked[] linked =new Linked[3];
        linked[0] = new Linked();
        linked[1] = new Linked();
        linked[2] = new Linked();
        Linked[] linkedMLC =new Linked[3];
        linkedMLC[0] = new Linked();
        linkedMLC[1] = new Linked();
        linkedMLC[2] = new Linked();
        BufferedReader bufferedReader = ReadFile("D:\\实验\\数据MemoryAccess\\patricia\\patricia.txt");

        //开始运行
        RWLinkedPage(linked,linkedMLC,bufferedReader);

        System.out.println(linked[0].getSize());
        linked[0].getPageSize();

    }
}
