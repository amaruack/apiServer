package com.son.api.slice.service;

import com.son.api.properties.DaouConfigProperties;
import com.son.api.service.file.FileProcessService;
import com.son.api.service.shop.ShopHistoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@Import({FileProcessService.class})
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileProcessServiceTest {

    @Mock
    ShopHistoryService shopHistoryService;
    @Mock
    DaouConfigProperties daouConfigProperties;
    @InjectMocks
    FileProcessService fileProcessService;

    @BeforeAll
    public void beforeAll() {

    }

    @Test
    void success_get_files_from_root_path() throws FileNotFoundException {
        // given
        URL url = ResourceUtils.getURL("classpath:sample");
        String rootPath = url.getPath();
        // when
        File[] files = fileProcessService.getFiles(rootPath);
        // then
        assertEquals(12, files.length);
    }

    @Test
    void success_file_process_csv_file() throws FileNotFoundException {

        // given
        String rootPath = ResourceUtils.getURL("classpath:file").getPath();
        File source = ResourceUtils.getFile("classpath:sample/csvFile.csv");
        File target = new File(rootPath + File.separator + "csvFile.csv");
        try ( FileInputStream fis = new FileInputStream(source);
                FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doReturn(rootPath).when(daouConfigProperties).getRootPath();
        doReturn(List.of()).when(shopHistoryService).createAll(anyList());

        // when
        fileProcessService.fileProcess();

        // then
        verify(shopHistoryService, times(1)).createAll(any());

        target.delete();
    }

    @Test
    void fail_file_process_csv_file_from_diff_separator() throws FileNotFoundException {

        // given
        String rootPath = ResourceUtils.getURL("classpath:file").getPath();
        File source = ResourceUtils.getFile("classpath:sample/csvFile_dif_separator.csv");
        File target = new File(rootPath + File.separator + "csvFile.csv");
        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doReturn(rootPath).when(daouConfigProperties).getRootPath();

        // when
        fileProcessService.fileProcess();

        // then
        verify(shopHistoryService, times(0)).createAll(any());

        target.delete();
    }


    @Test
    void success_file_process_txt_file() throws FileNotFoundException {

        // given
        String rootPath = ResourceUtils.getURL("classpath:file").getPath();
        File source = ResourceUtils.getFile("classpath:sample/txtFile.txt");
        File target = new File(rootPath + File.separator + "txtFile.txt");
        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doReturn(rootPath).when(daouConfigProperties).getRootPath();
        doReturn(List.of()).when(shopHistoryService).createAll(anyList());

        // when
        fileProcessService.fileProcess();

        // then
        verify(shopHistoryService, times(1)).createAll(any());

        target.delete();
    }

    @Test
    void fail_file_process_txt_file_from_diff_separator() throws FileNotFoundException {

        // given
        String rootPath = ResourceUtils.getURL("classpath:file").getPath();
        File source = ResourceUtils.getFile("classpath:sample/txtFile_dif_separator.txt");
        File target = new File(rootPath + File.separator + "txtFile.txt");
        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        doReturn(rootPath).when(daouConfigProperties).getRootPath();

        // when
        fileProcessService.fileProcess();

        // then
        verify(shopHistoryService, times(0)).createAll(any());

        target.delete();
    }

}
