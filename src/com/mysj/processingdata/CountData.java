package com.mysj.processingdata;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CountData {
    public static void main(String[] args) throws Exception {
        String filepath = "D:\\实验\\数据MemoryAccess\\jpeg\\cjpeg\\cjpeg.txt";
        //String filepath = "D:\\实验\\数据MemoryAccess\\qsort\\新建文本文档.txt";
        String filepath2 = "D:\\实验\\数据MemoryAccess\\jpeg\\cjpeg\\Count_cjpeg.txt";

        File file = new File(filepath);
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),4*1024*1024);// 用5M的缓冲读取文本文件

        //写入的新文件
        File file2 = new File(filepath2);
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file2));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"),1024);

        //对应的是：地址变量 : 大小、读取次数、写次数、总访问次数
        Map<String,Integer[]> map = new HashMap<String,Integer[]>();

        String line = "";
        int k = 0;
        String str = "";

        System.out.println(str);
        while((line = reader.readLine()) != null ){
            //TODO: write your business
            //分为 读写、大小、地址 0 1 2
            String[] s = line.split(" ");
            //System.out.println(line);
            //System.out.println("==============");
            System.out.println("---------------------------");
            if (s.length != 3) continue;
            System.out.println(s[2]);

            if (map.get(s[2]) == null){
                Integer[] data = new Integer[4];
                data[0] = Integer.parseInt(s[1]);
                data[3] = 1;
                if ("Write".equals(s[0])){
                    data[2] = 1;
                    data[1] = 0;
                }else {
                    data[1] = 1;
                    data[2] = 0;
                }
                map.put(s[2],data);
            }else {
                Integer[] a = map.get(s[2]);
                if ("Write".equals(s[0])){
                    a[2]++;
                    a[3]++;
                }else {
                    a[1]++;
                    a[3]++;
                }
                map.put(s[2],a);
            }

        }

        //System.out.println("----------------------------------");
        //遍历map
        for(String addr : map.keySet()){
            String str2 = addr;
            Integer[] b = map.get(addr);
            //对应的是：地址变量 : 大小、读取次数、写次数、总访问次数
            str2 = str2 + " " + b[0] + " " + b[1] + " " + b[2] + " " + b[3];
            writer.write(str2);
            writer.write(10);
            System.out.println(str2);
        }
        reader.close();
        writer.close();
    }
}
