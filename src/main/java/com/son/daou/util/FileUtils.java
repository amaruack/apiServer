package com.son.daou.util;

import java.io.File;

public class FileUtils {

    public static String getFileExtension(File file){
        return getFileExtension(file.getName());
    }

    public static String getFileExtension(String fileName){
        String extension = fileName.substring(fileName.lastIndexOf('.')+1);
        return extension;
    }

}
