package com.example.logger_demo.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUtil {
    private static File filePath = new File(LogUtil.logConfig.getFilePath());
    public static void fileWriter(String contents) {
        File file = new File(LogUtil.logConfig.getFilePath()+LogUtil.logConfig.getFileNamePatten()+".log");

        if(file.length() == 0){
            log.info("size: {}", file.length());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        long kb = file.length() / 1024;
        long mb = kb / 1024;

        // 설정된 FileSize보다 작을때
        if (kb < LogUtil.logConfig.getFileSize()) {
            try {
                log.info("<");
                StringBuilder tmp = new StringBuilder();

                BufferedReader br = new BufferedReader(new FileReader(file));
                String fileContents = "";
                while ((fileContents = br.readLine()) != null) {
                    tmp.append(fileContents+"\n");
                }
                log.info(tmp.toString());
                br.close();
                System.out.println(tmp);

                FileWriter fw = new FileWriter(file);
                fw.write(tmp + contents);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 클경우
        // 1. 기존파일 INDEX+1
        // 2. 새로운 파일 생성
        else{
            if(getDailyFileCount() <= LogUtil.logConfig.getFileDailyLimit()) {
                log.info("MAX");
                File newFile = new File(LogUtil.logConfig.getFilePath() + LogUtil.logConfig.getFileNamePatten() + getLastFileIndex() + ".log");
                if (file.renameTo(newFile)) {
                    try {

                        FileWriter fw = new FileWriter(file);
                        BufferedWriter bw = new BufferedWriter(fw);

                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                log.info("MAX DailyLimitCount");
            }
        }
    }
    public static int getLastFileIndex(){
        File[] fileList = filePath.listFiles(new FilenameFilter() {
                                            @Override
                                            public boolean accept(File dir, String name) {
                                                boolean checkName = name.contains(LogUtil.logConfig.getFileNamePatten());
                                                boolean checkExt = name.endsWith(".log");
                                                boolean except = !name.equals(LogUtil.logConfig.getFileNamePatten()+".log");
                                                return (checkName && checkExt && except);
                                            }
        });
        log.info("fileList: {}", fileList);
        if(ArrayUtils.isEmpty(fileList)){
            return 1;
        }
        else{
            String fileIndex = Arrays.stream(fileList).sorted().collect(Collectors.toList()).get(fileList.length - 1).toString()
                    .replace(LogUtil.logConfig.getFilePath(), "")
                    .replace(LogUtil.logConfig.getFileNamePatten(), "")
                    .split("\\.")[0];
            int nextIndex = Integer.parseInt(fileIndex)+ 1;
            return nextIndex;
        }

    }
    public static int getDailyFileCount() {
        File[] fileList = filePath.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean checkName = name.contains(LogUtil.logConfig.getFileNamePatten());
                boolean checkExt = name.endsWith(".log");
                boolean checkDailyCount = dir.setLastModified(new Date().getTime());
                return (checkName && checkExt && checkDailyCount);
            }
        });
        if(ArrayUtils.isEmpty(fileList)){
            return 0;
        }
        else{
            log.info("DailyCount: {}", fileList.length-1);
            return fileList.length-1;
        }
    }
}
