package com.mysj.processingdata;


import java.io.*;

import static com.mysj.idea.tool.RWFileTool.ReadFile;

public class OriginalData {
    public static void main(String[] args) throws Exception {
        String filepath = "D:\\share\\dijkstra_large.txt";
        String filepath2 = "D:\\实验\\数据MemoryAccess\\dijkstra\\dijkstra_large_1.txt";

        File file = new File(filepath);
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件

        //写入的新文件
        File file2 = new File(filepath2);
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file2));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"),1024);

        //Write from cpu.data of size 8 on address 0x23be20
        String line = "";
        int k = 0;
        String str = "";

        /*String[] s = "      0: global: 00000000  73 69 7a 65 20 6e 6f 74  20 74 68 65 20 65 78 70   size not the exp".split(" ");
        System.out.println(line);
        System.out.println("==============");
        for (int i = 0; i < s.length - 1; i++) {
            if ("Write".equals(s[i])){
                str += "Write ";
            }
            if ("Read".equals(s[i])){
                str += "Read ";
            }
            if ("size".equals(s[i])){
                str = str + s[i+1] + " " + s[i+4];
                break;
            }
        }*/
        System.out.println(str);
        while((line = reader.readLine()) != null ){
            //TODO: write your business

            String[] s = line.split(" ");
            //String[] s = "      0: global: 00000000  73 69 7a 65 20 6e 6f 74  20 74 68 65 20 65 78 70   size not the exp".split(" ");
            System.out.println(line);
            System.out.println("==============");
            for (int i = 0; i < s.length - 4; i++) {
                if ("Write".equals(s[i])){
                    str += "Write ";
                }
                if ("Read".equals(s[i])){
                    str += "Read ";
                }
                if (!"".equals(str) && "size".equals(s[i])){
                    str = str + s[i+1] + " " + s[i+4];
                    break;
                }
                /*if ("size".equals(s[i])){
                    str = str + s[i+1] + " " + s[i+4];
                    break;
                }*/
            }
            System.out.println(str);
            if (!"".equals(str)){
                writer.write(str);
                writer.write(10);
            }

            str = "";

        }
        reader.close();
        writer.close();
    }

}
