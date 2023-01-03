package com.example.logger_demo.util;

import com.example.logger_demo.model.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUtil {
    private static File dir;
    private static File file;

    public static void fileWriter(String contents) {
        file = new File(LogUtil.logConfig.getFilePath()+LogUtil.logConfig.getFileNamePatten()+".log");

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
        else {
            FileInfo fileInfo = getFileInfo();
            log.info("{}", fileInfo);
            if (fileInfo.getDailyFileCount() < LogUtil.logConfig.getFileDailyLimit()) {
                File newFile = new File(LogUtil.logConfig.getFilePath() + LogUtil.logConfig.getFileNamePatten() + fileInfo.getLastFileIndex() + ".log");
                if (file.renameTo(newFile)) {
                    try {

                        FileWriter fw = new FileWriter(file);
                        fw.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                log.info("MAX DailyLimitCount");
            }
        }
    }

    public static FileInfo getFileInfo(){
        FileInfo fileInfo = new FileInfo();
        dir = new File(LogUtil.logConfig.getFilePath());

        File[] fileList = dir.listFiles(new FilenameFilter() {
                                            @Override
                                            public boolean accept(File dir, String name) {
                                                boolean checkName = name.contains(LogUtil.logConfig.getFileNamePatten());
                                                boolean checkExt = name.endsWith(".log");
                                                boolean except = !name.equals(LogUtil.logConfig.getFileNamePatten()+".log");
                                                return (checkName && checkExt && except);
                                            }
        });

        if(ArrayUtils.isEmpty(fileList)){
            fileInfo.setLastFileIndex(1);
            fileInfo.setDailyFileCount(0);
            return fileInfo;
        }
        else{
            // FileList에서 파일명만 가져와 문자열 제거후 Max값
            OptionalInt fileIndex = Arrays.stream(fileList)
                    .map(dir -> dir.getName().replaceAll("[^0-9]", ""))
                    .mapToInt(Integer::parseInt)
                    .max();
            fileInfo.setLastFileIndex(fileIndex.getAsInt() + 1);

            // List에서 수정날짜가 오늘날짜인 파일 갯수
            fileInfo.setDailyFileCount((int) Arrays.stream(fileList).filter(dir -> dir.setLastModified(new Date().getTime())).count());
            return fileInfo;
        }

    }


}
