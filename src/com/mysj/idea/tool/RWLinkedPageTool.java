package com.mysj.idea.tool;

import com.mysj.idea.Linked;
import com.mysj.idea.Page;

import java.io.BufferedReader;

public class RWLinkedPageTool {

    /**
     * 读写地址变量的工具
     * @param linked 新建的是Linked[],数组，0,1,2分别为 linkedSlcOne、linkedSlcOne、linkedSlcTwo、linkedSlcThree
     * @param bufferedReader
     * 如果在当前周期中，第一个SLC队列中没有被访问到的页面将被移动到第二个SLC队列（依次递推）
     * 当前周期中，被访问到的页面将会被移动到第一个SLC队列
     * 1、第一个周期时，先往one中读写数据，第一个周期结束时，需要把是否访问过该页面的flag重置
     * 2、第二个周期时，继续往one中读写数据，当第二个周期结束时，把one中flag为false的页面移动到two中，并重置所有的flag
     * 3、第三个周期时，自动读写数据，先查找前面one、two中是否存在该变量，若存在，则在对应位置读写，若不在，则将该数据写入one中
     *              周期结束时，two中为FALSE的页面迁移到three中、为TRUE的迁移到one中，然后将one中为false的迁移到two中，然后重置所有的flag
     * 4、后续周期中，先在三条链表中找，找到了就对应位置读写，如果没有找到，还是写到one中
     *              a.先把two、three链表中为true的迁移到one中
     *              b.two中为false的迁移到three中
     *              c.再把one中为false的迁移到two中
     */
    public static void RWLinkedPage(Linked[] linked, BufferedReader bufferedReader) throws Exception{
        String strLine = null;
        long x = 0;
        int period = 1;//周期
        while(null != (strLine = bufferedReader.readLine())) {
            //第一个周期，先初始化链表、页面等信息，进行第一个周期内的读写
            //第二个周期，继续往第一条链表中操作，第二个周期结束时再进行页面移动

            //每行有三个数据，分别为 读写、大小、地址
            String[] s = strLine.split(" ");

            if(s.length!=3) continue;
            int size_j = Integer.parseInt(s[1]);
            //1.当链表为空时，先存放一页，往该地址中放变量
            if (linked[0] == null){
                Page page = new Page();
                page.put(s[2],size_j);//设定每个地址变量为4bit
                linked[0].addLast(page);
                continue;
            }
            int subscript1 = linked[0].contains(s[2]);
            int subscript2 = linked[1].contains(s[2]);
            int subscript3 = linked[2].contains(s[2]);
            //2.当链表中没有该地址时，找一页存放
            if (subscript1 == 0 && subscript2 == 0 && subscript3 == 0){//意味着链表中没有该地址
                boolean bool = linked[0].add(s[2], size_j);
                //如果链表中存放失败、页数（大小）不够，则新建一个页来存放
                if (bool == false){
                    Page page = new Page();
                    page.put(s[2],size_j);
                    linked[0].addLast(page);
                    //System.out.println("新建一个页面存放在链表一中");
                }
            }else if (subscript1 != 0){     //3.在链表中找到该地址
                if ("Write".equals(s[0])){
                    linked[0].write(s[2],size_j,subscript1);
                }
                if ("Read".equals(s[0])){
                    linked[0].read(s[2],size_j,subscript1);
                }
                //System.out.println("在链表一中找到该变量");
            }else if (subscript2 != 0){
                if ("Write".equals(s[0])){
                    linked[1].write(s[2],size_j,subscript2);
                }
                if ("Read".equals(s[0])){
                    linked[1].read(s[2],size_j,subscript2);
                }
                //System.out.println("在链表二中找到该变量");
            }else if (subscript3 != 0){
                if ("Write".equals(s[0])){
                    linked[2].write(s[2],size_j,subscript3);
                }
                if ("Read".equals(s[0])){
                    linked[2].read(s[2],size_j,subscript3);
                }
                //System.out.println("在链表三中找到该变量");
            }

            x++;

            if (x % 2000000 == 0){

                System.out.println("linked[0]有：" + linked[0].getSize() + " 个页面");
                System.out.println("linked[1]有：" + linked[1].getSize() + " 个页面");
                System.out.println("linked[2]有：" + linked[2].getSize() + " 个页面");
                System.out.println("-------第----- " + period + " -----个周期结束--------");
                //当周期二结束，把one中flag为false的页面移动到two中，并重置所有的flag
                if (period == 2){
                    System.out.println(linked[0].getSize());
                    linked[0].movePageLinKedAtoB("linked[0]","linked[1]",linked[1],false);
                }
                if(period == 3){
                    linked[1].movePageLinKedAtoB("linked[1]","linked[2]",linked[2],false);
                    linked[0].movePageLinKedAtoB("linked[0]","linked[1]",linked[1],false);
                    linked[1].movePageLinKedAtoB("linked[1]","linked[0]",linked[0],true);
                }else if (period > 3){
                    //a.先把two、three链表中为true的迁移到one中
                    //b.two中为false的迁移到three中
                    //c.再把one中为false的迁移到two中
                    linked[2].movePageLinKedAtoB("linked[2]","linked[0]",linked[0],true);
                    linked[1].movePageLinKedAtoB("linked[1]","linked[0]",linked[0],true);
                    linked[1].movePageLinKedAtoB("linked[1]","linked[2]",linked[2],false);
                    linked[0].movePageLinKedAtoB("linked[0]","linked[1]",linked[1],false);
                }

                //每个周期开始时，需要进行
                period++;
                linked[0].resetCycleAccess();
                linked[1].resetCycleAccess();
                linked[2].resetCycleAccess();
                System.out.println("linked[0]有：" + linked[0].getSize() + " 个页面");
                System.out.println("linked[1]有：" + linked[1].getSize() + " 个页面");
                System.out.println("linked[2]有：" + linked[2].getSize() + " 个页面");
                System.out.println("==============================================");
            }

        }
    }

}
