package main.java.java.util;

import java.io.File;

public class Progress {
    static long allFileSize = 0; // 所有需要复制的文件大小
    static long currentFileSizeCopied = 0;// 已复制的文件总大小
    public Progress(){

    }

    /**
     * 遍历文件夹获取文件夹内容总大小
     *
     * @param file
     */
    public static void calclateAllFilesize(File file) {   //获取文件的总大小
        allFileSize = 0;
        if (file.isFile()) {
            allFileSize += file.length();
        }
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            for (File f : fs) {
                calclateAllFilesize(f);
            }
        }

    }
}
