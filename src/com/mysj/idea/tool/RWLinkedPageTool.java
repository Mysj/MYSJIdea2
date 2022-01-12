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
     *              6000002
     */
    public static void RWLinkedPage(Linked[] linked,Linked[] linkedMLC, BufferedReader bufferedReader) throws Exception{

        //统计变量
        int countSLCtoMLC = 0;
        int countMLCtoSLC = 0;
        int total = 0;
        //用变量记录总的写大小和读大小，好计算成本
        double ReadSLC = 0;
        double WriteSLC = 0;
        double ReadMLC = 0;
        double WriteMLC = 0;

        double SIZE2 = 0;
        final int Xia_Biao = 0;
        String strLine = null;
        long x = 0;
        int period = 1;//周期
        while(null != (strLine = bufferedReader.readLine())) {
            //第一个周期，先初始化链表、页面等信息，进行第一个周期内的读写
            //第二个周期，继续往第一条链表中操作，第二个周期结束时再进行页面移动
            //每行有三个数据，分别为 读写、大小、地址
            String[] s = strLine.split(" ");
            double SIZE1 = 0;
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
            int subscript4 = linkedMLC[0].contains(s[2]);
            int subscript5 = linkedMLC[1].contains(s[2]);
            int subscript6 = linkedMLC[2].contains(s[2]);

            //先判断MLC链表中是否有
            if (subscript4 == Xia_Biao && subscript5 == Xia_Biao && subscript6 == Xia_Biao){
                //2.当链表中没有该地址时，找一页存放
                if (subscript1 == Xia_Biao && subscript2 == Xia_Biao && subscript3 == Xia_Biao){//意味着链表中没有该地址
                    boolean bool = linked[0].add(s[2], size_j);
                    //如果链表中存放失败、页数（大小）不够，则新建一个页来存放
                    if (bool == false){
                        Page page = new Page();
                        page.put(s[2],size_j);
                        linked[0].addLast(page);
                        //System.out.println("新建一个页面存放在链表一中");
                    }
                    WriteSLC += size_j;

                    //第一次把该变量写入页面时，如果是读操作，就加上
                    if ("Read".equals(s[0])){
                        ReadSLC += size_j;
                    }
                }else {
                    if (subscript1 != Xia_Biao){     //3.在链表中找到该地址
                        if ("Write".equals(s[0])){
                            linked[0].write(s[2],size_j,subscript1 -1 );
                            WriteSLC += size_j;
                        }
                        if ("Read".equals(s[0])){
                            linked[0].read(s[2],size_j,subscript1 - 1);
                            ReadSLC += size_j;
                        }
                        //System.out.println("在链表一中找到该变量");
                    }else if (subscript2 != Xia_Biao){
                        if ("Write".equals(s[0])){
                            linked[1].write(s[2],size_j,subscript2 - 1);
                            WriteSLC += size_j;
                        }
                        if ("Read".equals(s[0])){
                            linked[1].read(s[2],size_j,subscript2 - 1);
                            ReadSLC += size_j;
                        }
                        //System.out.println("在链表二中找到该变量");
                    }else if (subscript3 != Xia_Biao){
                        if ("Write".equals(s[0])){
                            linked[2].write(s[2],size_j,subscript3 - 1);
                            WriteSLC += size_j;
                        }
                        if ("Read".equals(s[0])){
                            linked[2].read(s[2],size_j,subscript3 - 1);
                            ReadSLC += size_j;
                        }
                    }
                }
            }else {
                if (subscript4 != Xia_Biao){
                    if ("Write".equals(s[0])){
                        linkedMLC[0].write(s[2],size_j,subscript4 - 1);
                        WriteMLC += size_j;
                    }
                    if ("Read".equals(s[0])){
                        linkedMLC[0].read(s[2],size_j,subscript4 - 1);
                        ReadMLC += size_j;
                    }
                }else if (subscript5 != Xia_Biao){
                    if ("Write".equals(s[0])){
                        linkedMLC[1].write(s[2],size_j,subscript5 - 1);
                        WriteMLC += size_j;
                    }
                    if ("Read".equals(s[0])){
                        linkedMLC[1].read(s[2],size_j,subscript5 - 1);
                        ReadMLC += size_j;
                    }
                }else if (subscript6 != Xia_Biao){
                    if ("Write".equals(s[0])){
                        linkedMLC[2].write(s[2],size_j,subscript6 - 1);
                        WriteMLC += size_j;
                    }
                    if ("Read".equals(s[0])){
                        linkedMLC[2].read(s[2],size_j,subscript6 - 1);
                        ReadMLC += size_j;
                    }
                }
            }

            x++;

            if (x % 1500000 == 0){

                System.out.println("====================================================");
                System.out.println("linked[0]有：" + linked[0].getSize() + " 个页面");
                System.out.println("linked[1]有：" + linked[1].getSize() + " 个页面");
                System.out.println("linked[2]有：" + linked[2].getSize() + " 个页面");
                System.out.println("linkedMLC[0]有：" + linkedMLC[0].getSize() + " 个页面");
                System.out.println("linkedMLC[1]有：" + linkedMLC[1].getSize() + " 个页面");
                System.out.println("linkedMLC[2]有：" + linkedMLC[2].getSize() + " 个页面");
                System.out.println("-----------第---------- " + period + " -----------个周期结束--------");

                //当周期二结束，把one中flag为false的页面移动到two中，并重置所有的flag
                if (period == 2){
                    //System.out.println(linked[0].getSize());
                    int a1 = linked[0].movePageLinKedAtoB(0,"linked[0]","linked[1]",linked[1],false);
                    total += a1;
                }
                if(period == 3){
                    int a2 = linked[1].movePageLinKedAtoB(0,"linked[1]","linked[2]",linked[2],false);
                    int a3 = linked[0].movePageLinKedAtoB(0,"linked[0]","linked[1]",linked[1],false);
                    int a4 = linked[1].movePageLinKedAtoB(0,"linked[1]","linked[0]",linked[0],true);
                    total += a2 + a3 + a4;
                }
                if (period > 3){
                    //a.先把two、three链表中为true的迁移到one中
                    //b.two中为false的迁移到three中
                    //c.再把one中为false的迁移到two中
                    int a5 = linked[2].movePageLinKedAtoB(0,"linked[2]","linked[0]",linked[0],true);
                    int a6 = linked[1].movePageLinKedAtoB(0,"linked[1]","linked[0]",linked[0],true);
                    //执行x++操作,（还进行了判断页面转换操作）
                    int SLCtoMLC = linked[2].changeX(linkedMLC[0]);

                    countSLCtoMLC += SLCtoMLC;
                    System.out.println("linked[2] 往 linkedMLC[0] 迁移了" + SLCtoMLC + " 个页面");

                    int a7 = linked[1].movePageLinKedAtoB(0,"linked[1]","linked[2]",linked[2],false);
                    int a8 = linked[0].movePageLinKedAtoB(0,"linked[0]","linked[1]",linked[1],false);

                    int MLCtoSLC = linkedMLC[2].movePageLinKedAtoB(2,"linkedMLC[2]","linked[0]",linked[0],true);

                    int a10 = linkedMLC[1].movePageLinKedAtoB(0,"linkedMLC[1]","linkedMLC[2]",linkedMLC[2],true);
                    int a9 = linkedMLC[0].movePageLinKedAtoB(0,"linkedMLC[0]","linkedMLC[1]",linkedMLC[1],true);

                    total += a5 + a6 + a7 + a8 + a9 + a10;
                    countMLCtoSLC += MLCtoSLC;
                }

                //每个周期开始时，需要进行
                period++;
                //printHotPage(linked[0]);
                linked[0].resetCycleAccess();
                //printHotPage(linked[1]);
                linked[1].resetCycleAccess();
                //printHotPage(linked[2]);
                linked[2].resetCycleAccess();
                //printHotPage(linkedMLC[0]);
                linkedMLC[0].resetCycleAccess();
                //printHotPage(linkedMLC[1]);
                linkedMLC[1].resetCycleAccess();
                //printHotPage(linkedMLC[2]);
                linkedMLC[2].resetCycleAccess();
                System.out.println("linked[0]有：" + linked[0].getSize() + " 个页面");
                System.out.println("linked[1]有：" + linked[1].getSize() + " 个页面");
                System.out.println("linked[2]有：" + linked[2].getSize() + " 个页面");
                System.out.println("linkedMLC[0]有：" + linkedMLC[0].getSize() + " 个页面");
                System.out.println("linkedMLC[1]有：" + linkedMLC[1].getSize() + " 个页面");
                System.out.println("linkedMLC[2]有：" + linkedMLC[2].getSize() + " 个页面");
                System.out.println("=====================================================");

            }

        }

        System.out.println("============MyRW==================");
        System.out.println("countSLCtoMLC = " + countSLCtoMLC);
        System.out.println("countMLCtoSLC = " + countMLCtoSLC);
        System.out.println("total = " + total);
        System.out.println("ReadSLC = " + ReadSLC);
        System.out.println("WriteSLC = " + WriteSLC);
        System.out.println("ReadMLC = " + ReadMLC);
        System.out.println("WriteMLC = " + WriteMLC);
        double totalS = ReadSLC*1.55 + WriteSLC*8.38 + ReadMLC*2.75 + WriteMLC*18.565 +
                countSLCtoMLC*(1.55 + 18.565)*4*1024*8 + countMLCtoSLC*(2.75 + 8.38)*4*1024*8;
        System.out.println("总成本 = "+ totalS);
        System.out.println("===================================");
    }

    public static boolean checkOnlyOne(Linked[] linked,Linked[] linkedMLC,BufferedReader bufferedReader)throws Exception{
        String strLine = null;
        while(null != (strLine = bufferedReader.readLine())) {
            int subscript1 = linked[0].contains(strLine);
            int subscript2 = linked[1].contains(strLine);
            int subscript3 = linked[2].contains(strLine);
            int subscript4 = linkedMLC[0].contains(strLine);
            int subscript5 = linkedMLC[1].contains(strLine);
            int subscript6 = linkedMLC[2].contains(strLine);
            int i = 0;
            if (subscript1 != 0)i++;
            if (subscript2 != 0)i++;
            if (subscript3 != 0)i++;
            if (subscript4 != 0)i++;
            if (subscript5 != 0)i++;
            if (subscript6 != 0)i++;
            //System.out.println(i);
            if (i>3) System.out.println(strLine);
            //if(i != 1)return false;
        }
        return true;
    }

}
