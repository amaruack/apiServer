package com.son.daou.integration;

import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.properties.DaouConfigProperties;
import com.son.daou.service.file.FileProcessService;
import com.son.daou.service.shop.ShopHistoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class FileProcessTest {

    @Autowired
    DaouConfigProperties daouConfigProperties;

    @Autowired
    FileProcessService fileProcessService;

    @Autowired
    ShopHistoryService shopHistoryService;

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
        String rootPath = daouConfigProperties.getRootPath();
        File source = ResourceUtils.getFile("classpath:sample/csvFile.csv");
        File target = new File(rootPath + File.separator + "csvFile.csv");
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when
        fileProcessService.fileProcess();

        // then
        Page<ShopHistoryResponse> pages = shopHistoryService.search(ShopHistoryQueryParam.builder().build(), PageRequest.of(0, 99));

        assertNotNull(pages);
        assertEquals(24, pages.getTotalElements());

        target.delete();
    }

    @Test
    void fail_file_process_csv_file_from_diff_separator() throws FileNotFoundException {

        // given
        String rootPath = daouConfigProperties.getRootPath();
        File source = ResourceUtils.getFile("classpath:sample/csvFile_dif_separator.csv");
        File target = new File(rootPath + File.separator + "csvFile.csv");
        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when
        fileProcessService.fileProcess();

        // then
        Page<ShopHistoryResponse> pages = shopHistoryService.search(ShopHistoryQueryParam.builder().build(), PageRequest.of(0, 99));
        assertNotNull(pages);
        assertEquals(0, pages.getTotalElements());

        target.delete();
    }


    @Test
    void success_file_process_txt_file() throws FileNotFoundException {

        // given
        String rootPath = daouConfigProperties.getRootPath();
        File source = ResourceUtils.getFile("classpath:sample/txtFile.txt");
        File target = new File(rootPath + File.separator + "txtFile.txt");
        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when
        fileProcessService.fileProcess();

        // then
        Page<ShopHistoryResponse> pages = shopHistoryService.search(ShopHistoryQueryParam.builder().build(), PageRequest.of(0, 99));
        assertNotNull(pages);
        assertEquals(24, pages.getTotalElements());

        target.delete();
    }

    @Test
    void fail_file_process_txt_file_from_diff_separator() throws FileNotFoundException {

        // given
        String rootPath = daouConfigProperties.getRootPath();
        File source = ResourceUtils.getFile("classpath:sample/txtFile_dif_separator.txt");
        File target = new File(rootPath + File.separator + "txtFile.txt");
        try ( FileInputStream fis = new FileInputStream(source);
              FileOutputStream fos = new FileOutputStream(target); ){
            fos.write(fis.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // when
        fileProcessService.fileProcess();

        // then
        Page<ShopHistoryResponse> pages = shopHistoryService.search(ShopHistoryQueryParam.builder().build(), PageRequest.of(0, 99));
        assertNotNull(pages);
        assertEquals(0, pages.getTotalElements());

        target.delete();
    }

}
