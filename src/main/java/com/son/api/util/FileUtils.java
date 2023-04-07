package com.son.api.util;

import java.io.File;

public class FileUtils {

    public static final int INDEX_DATE_TIME = 0;
    public static final int INDEX_REGISTER_COUNT = 1;
    public static final int INDEX_DELETE_COUNT = 2;
    public static final int INDEX_PAYMENT = 3;
    public static final int INDEX_USED = 4;
    public static final int INDEX_SALES = 5;

    public static String getFileExtension(File file){
        return getFileExtension(file.getName());
    }

    public static String getFileExtension(String fileName){
        String extension = fileName.substring(fileName.lastIndexOf('.')+1);
        return extension;
    }

}
