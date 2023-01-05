package com.example.logger_demo.util;

import com.example.logger_demo.model.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtil {
    private static File dir;

    public static void fileWriter(String contents) {
        dir = new File(LogUtil.logConfig.getFilePath());
        File file = new File(dir, LogUtil.logConfig.getFileNamePatten() + ".log");
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }


            long kb = file.length() / 1024;
            long mb = kb / 1024;

            FileWriter fw = new FileWriter(file, true);
            fw.append(contents);
            fw.flush();


            if (kb > LogUtil.logConfig.getFileSize()) {
                FileInfo fileInfo = getFileInfo();
                log.info("{}", fileInfo);

                if (fileInfo.getDailyFileCount() < LogUtil.logConfig.getFileDailyLimit()) {
                    File newFile = new File(LogUtil.logConfig.getFilePath() + LogUtil.logConfig.getFileNamePatten() + fileInfo.getNextIndex() + ".log");
                    FileUtils.copyFile(file, newFile);
                    new FileWriter(file, false).close();

                } else {
                    log.info("MAX DailyLimitCount");

                }
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 디렉토리에서 조건에 맞는 파일들 조회하여 nextIndex, dailyFileCount 리턴
     */
    public static FileInfo getFileInfo() {
        FileInfo fileInfo = new FileInfo();
        File[] fileList = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean checkName = name.contains(LogUtil.logConfig.getFileNamePatten());
                boolean checkExt = name.endsWith(".log");
                boolean except = !name.equals(LogUtil.logConfig.getFileNamePatten() + ".log");
                return (checkName && checkExt && except);
            }
        });

        if (ArrayUtils.isEmpty(fileList)) {
            fileInfo.setNextIndex(1);
            fileInfo.setDailyFileCount(0);
            return fileInfo;
        } else {
            // FileList에서 파일명만 가져와 문자열 제거후 Max값
            OptionalInt fileIndex = Arrays.stream(fileList)
                    .map(dir -> dir.getName().replaceAll("[^0-9]", ""))
                    .mapToInt(Integer::parseInt)
                    .max();
            fileInfo.setNextIndex(fileIndex.getAsInt() + 1);

            // List에서 수정날짜가 오늘날짜인 파일 갯수
            fileInfo.setDailyFileCount((int) Arrays.stream(fileList).filter(dir -> dir.setLastModified(new Date().getTime())).count());
            return fileInfo;
        }

    }


}
