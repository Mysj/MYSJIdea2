package com.mysj.idea;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * 根据trace
 * 计算基准时间
 * 和本实验没有关系
 */
public class RW_Files {
    /**
     * 读取文件的工具
     */
    public static BufferedReader ReadFile(String fileName){

        File file = new File(fileName);

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bufferedReader;

    }




    public static void main(String[] args) throws Exception {
        String address[] = new  String[512*1024];//slc
        String address2[] = new  String[512*1024];//mlc
        //address[0] = "END";
        int count = 0;//数组中命中地址的次数

        final int N = 64*1024;//cache的大小
        int size = 0;//记录cache的总大小
        double cost = 0; //记录总的消耗
        double R_SLC = 1.55;
        double R_MLC = 2.75;
        double W_SLC = 8.38;
        double W_MLC = 18.565;
        double R_DRAM = 41.04;
        double W_DRAM = 41.04;


        int SLC = 0;
        int MLC = 0;



        BufferedReader bufferedReader = ReadFile("D:\\实验\\徐徐实验\\实验数据\\qsort_small\\qsort_small_end.txt");
        //BufferedReader bufferedReader = ReadFile("D:\\Learnning\\科研实验\\MemoryAccessTrace\\qsort_small\\新建文本文档.txt");
        String strLine = null;
        double min = 1000000000;
        for (SLC=0,MLC=128*1024;SLC<=64*1024;SLC+=32,MLC-=64){
            count = 0;
            /**
             *读取文件数据每行进行分割存储
             */

            while(null != (strLine = bufferedReader.readLine())) {
                //每行有三个数据，分别为 读写、大小、地址
                String[] s = strLine.split(" ");
                if(s.length!=3) continue;

                /**
                 * 成功划分后，进行操作
                 * 1、Read时：先判断地址是否在数组中存在，如果不存在，就先从DRAM中读取，在写到NVM中---这里就需要计算读取DRAM的消耗+写入NVM的消耗
                 *           如果数组中存在，则直接计算在NVM中读取的消耗
                 * 2、Write时：找数组中是否有这个地址，如果有则直接计算写NVM的消耗，如果没有，则把地址加入数组再计算消耗
                 * 3、每次输入地址与地址大小，计算地址总的大小消耗，超过cache的大小时，就写到DRAM，计算写DRAM的消耗
                 */
                if (s.length==3){
                    /*System.out.println(s[2] + "------");//0是读写，1是大小，2是地址*/

                    int flag1 = 1;
                    int flag2 = 1;
                    int p = 0;//记录下标位置
                    int p2 = 0;//记录下标位置
                    int subscript = -1;
                    int subscript2 = -1;
                    for (int i = 0;i<address.length;i++){
                        if (address[i]==null){
                            p = i;
                            break;
                        }
                        if (address[i].equals(s[2])){//数组中找到该地址后
                            flag1 = 0;
                            subscript = i;//地址下标
                            break;
                        }
                    }
                    //在SLC地址数组中没有找到，则在MLC数组中找
                    if (flag1!=0){
                        for (int i = 0; i < address2.length; i++) {
                            if (address2[i]==null){
                                p2 = i;
                                break;
                            }
                            if (address2[i].equals(s[2])){//数组中找到该地址后
                                flag2 = 0;
                                subscript2 = i;//地址下标
                                break;
                            }
                        }
                    }
                    if (subscript == -1&&subscript2 == -1){//没有找到该地址，就在后面写入该地址，cost=读取DRAM+写SLC
                        /**
                         * 操作为Read时
                         * 首先是该地址的数据在DRAM中，需要从DRAM中读取出来，计算cache容量是否满足
                         * 1.如果cache容量足够，则cost = 读取DRAM开销 + 写入SLC开销
                         * 2.如果cache容量不够，则cost = 读取DRAM开销
                         * 操作为Write时
                         *
                         */

                        size += 4;
                        if(size <= SLC){
                            //容量足够，才把该地址添加在数组中
                            address[p] = s[2];
                            /*System.out.println("p的大小为："+p);*/
                            //cost = cost + (R_DRAM + W_SLC)*Double.parseDouble(s[1]); //这个是SLC
                            cost = cost + (R_DRAM + W_SLC)*4; //SLC
                            //cost = cost + (R_DRAM + W_MLC)*Double.parseDouble(s[1]);
                            //cost = cost + (R_DRAM + W_MLC)*4;//MLC
                        }else if(size <= SLC + MLC) {
                            address2[p2] = s[2];
                            cost = cost + (R_DRAM + W_MLC)*4;//MLC

                        } else {

                            if ("Read".equals(s[0])){
                                //cost += R_DRAM*Double.parseDouble(s[1]);
                                cost += R_DRAM*4;
                            }else if("Write".equals(s[0])){
                                //cost += W_DRAM*Double.parseDouble(s[1]);
                                cost += W_DRAM*4;
                            }


                        }
                    }
                    if (flag1 == 0&&subscript != -1){//地址数组里面找到了该地址
                        count++;

                        /**
                         * 地址数组中找到该地址，说明该数据在cache中
                         * cost = 读取SLC开销
                         */
                        if ("Read".equals(s[0])){
                            //cost += R_SLC*Double.parseDouble(s[1]);//这是SLC
                            cost += R_SLC*4;//这是SLC
                            //cost += R_MLC*Double.parseDouble(s[1]);
                            //cost += R_MLC*4;//MLC
                        }else if ("Write".equals(s[0])){
                            //cost += W_SLC*Double.parseDouble(s[1]);//SLC
                            cost += W_SLC*4;//SLC
                            //cost += W_MLC*Double.parseDouble(s[1]);
                            //cost += W_MLC*4;//mlc
                        }
                    }
                    if (flag2 == 0&&subscript2 !=-1){
                        count++;
                        if ("Read".equals(s[0])){

                            cost += R_MLC*4;//MLC
                        }else if ("Write".equals(s[0])){

                            cost += W_MLC*4;//mlc
                        }
                    }
                }
            }
            if (cost<min){
                min = cost;
            }
            cost = 0;
            bufferedReader.close();
            bufferedReader = ReadFile("D:\\实验\\徐徐实验\\实验数据\\typeset\\lout_end.txt");
            //BufferedReader bufferedReader = ReadFile("D:\\Learnning\\科研实验\\MemoryAccessTrace\\qsort_small\\新建文本文档.txt");
            strLine = null;

        }


        System.out.println("------mincost = "+min + "-------------");
        System.out.println("找到该地址！:"+count);

    }

}
