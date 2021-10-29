package com.mysj.idea.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RWFileTool {
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

}
