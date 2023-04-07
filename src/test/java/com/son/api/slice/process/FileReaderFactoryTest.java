package com.son.api.slice.process;

import com.son.api.process.*;
import com.son.api.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileReaderFactoryTest {

    @Test
    void success_text_file_read_class_get() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile.txt");

        //when
        FileReader fileReader = FileReaderFactory.getFileReader(FileUtils.getFileExtension(file));

        //then
        assertEquals(TxtFileReader.class, fileReader.getClass());

    }

    @Test
    void success_csv_file_read_class_get() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/csvFile.csv");

        //when
        FileReader fileReader = FileReaderFactory.getFileReader(FileUtils.getFileExtension(file));

        //then
        assertEquals(CsvFileReader.class, fileReader.getClass());
    }

    @Test
    void fail_not_found_file_read_class_from_string_extension() {
        //given
        String extension = "doc";

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FileReader fileReader = FileReaderFactory.getFileReader(extension);
        }) ;

        //then
        assertEquals(RuntimeException.class, exception.getClass());
        assertEquals("NOT ACCEPTED FILE EXTENSION", exception.getMessage());
    }

    @Test
    void fail_not_found_file_read_class_from_enum_extension() {
        //given
        FileExtension fileExtension = null;

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> {
            FileReader fileReader = FileReaderFactory.getFileReader(fileExtension);
        }) ;

        //then
        assertEquals(NullPointerException.class, exception.getClass());
    }

}
