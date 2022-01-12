package com.mysj.idea.tool;

import com.mysj.idea.Linked;
import com.mysj.idea.Page;

import java.io.BufferedReader;

public class MyLruTool {

    /**
     * 实现自己的LRU算法来获取基准对比数据（全SLC、全MLC的时间、能耗）
     * 采用头插法，热数据存放再链表头部，当链表空间不足时，释放链表尾部的页面，
     *      当某一个页面被访问时，就把该页面迁移到头部
     * @param linked 提供链表有两种，一种为SLC，另一种为MLC
     * @param bufferedReader 读取trace
     * @throws Exception
     */
    public static void MyLRU(Linked linked, BufferedReader bufferedReader) throws Exception{
        final int Xia_Biao = 0;
        final int MaxPageSize = 100;//LRU最大页面数量,slc与mlc数量不同
        int RemovePageSize = 0;
        long WriteNumber = 0;
        long ReadNumber = 0;
        String strLine = null;
        long x = 0;
        int period = 1;//周期
        long RWTime = 0;
        //linked.addLast(new Page());
        //读取trace进行内存访问模拟
        while(null != (strLine = bufferedReader.readLine())){
            // 读写 大小 地址
            String[] s = strLine.split(" ");
            double SIZE1 = 0;
            if(s.length!=3) continue;
            int size_j = Integer.parseInt(s[1]);
            //1.当链表为空时，先存放一页，往该地址中放变量
            if (linked.isEmpty()){
                Page page = new Page();
                page.put(s[2],size_j);
                WriteNumber += size_j;//记录总共写大小
                linked.addLast(page);//采用头插法，热数据放在头部  这里用尾部代替头部
                continue;
            }
            //查询链表中是否有该地址变量
            int subscript = linked.contains(s[2]);

            if(subscript == Xia_Biao){
                boolean bool = linked.add(s[2], size_j);
                WriteNumber += size_j;
                //如果链表中存放失败、页数（大小）不够，则新建一个页来存放
                if (bool == false){
                    //判断链表大小，是否需要进行页面逐出
                    if (MaxPageSize - linked.getSize() < 10 ){
                        //System.out.println("当前页面数量：" + linked.getSize());
                        for (int i = 0; i < 10; i++) {
                            linked.removeFirst();
                            RemovePageSize ++;
                        }
                    }
                    Page page = new Page();
                    page.put(s[2],size_j);
                    linked.addLast(page);
                }
                if ("Read".equals(s[0])){
                    //linked.readLRU(s[2],size_j,0);
                    ReadNumber += size_j;
                }else if ("Write".equals(s[0])){
                    ////////////////////////////////////////
                    //WriteNumber += size_j;
                }
            }else if (subscript != Xia_Biao){
                if ("Write".equals(s[0])){
                    long W = linked.writeLRU(s[2], size_j, subscript - 1);
                    RWTime += W;
                    WriteNumber += size_j;
                }
                if ("Read".equals(s[0])){
                    long R = linked.readLRU(s[2], size_j, subscript - 1);
                    RWTime += R;
                    ReadNumber += size_j;
                }
            }

        }

        System.out.println("============LRU===================");
        System.out.println("算法时间成本：" + RWTime);//毫秒
        System.out.println("WriteNumber: " + WriteNumber);
        System.out.println("ReadNumber: " + ReadNumber);
        System.out.println("RemovePageSize: " + RemovePageSize);
        System.out.println("SLC: " + (WriteNumber*8.38 + ReadNumber*1.55 + RemovePageSize*1024*8*4*8.38));
        System.out.println("MLC: " + (WriteNumber*18.565 + ReadNumber*2.75));
    }

}
