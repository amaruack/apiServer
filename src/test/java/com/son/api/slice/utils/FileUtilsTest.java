package com.son.api.slice.utils;

import com.son.api.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class FileUtilsTest {

    @Mock
    File file;

    @Test
    void success_get_file_extension(){
        // given
        String fileName = "test.txt";
        String fileName2 = "test.csv";

        // when
        String extension = FileUtils.getFileExtension(fileName);
        String extension2 = FileUtils.getFileExtension(fileName2);

        //then
        assertEquals("txt", extension);
        assertEquals("csv", extension2);
    }

    @Test
    void success_get_file_extension_from_file(){
        // given
        String fileName = "test.txt";
        doReturn(fileName).when(file).getName();

        // when
        String extension = FileUtils.getFileExtension(file);

        //then
        assertEquals("txt", extension);
    }

}
