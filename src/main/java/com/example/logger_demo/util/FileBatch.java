package com.example.logger_demo.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileBatch implements Runnable {


    private static FileBatch instance;
    private Thread worker;
    private long nextDate;
    private static FileUtil fileUtil = FileUtil.getInstance();

    public static FileBatch getInstance() {
        if (instance == null) {
            synchronized (FileBatch.class) {
                instance = new FileBatch();
            }
        }
        return instance;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        System.out.println("### STOP ###");
    }

    @Override
    public void run() {
        nextDate = setNextDate();

        log.info("checkTime: {}, nextDate: {}", fileUtil.fileInfo.getCheckTime(), nextDate);
//        log.info("next Date: {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(nextDate), TimeZone.getDefault().toZoneId()));
        try {

            while (true) {
                if (fileUtil.fileInfo.getCheckTime()+60000 < nextDate) {
                    fileUtil.fileExistCheck();
                    Thread.sleep(60000);
//                    if(fileUtil.)
                    fileUtil.fileInfo.setCheckTime(System.currentTimeMillis());
                    fileUtil.fileInfo = fileUtil.getFileInfo();

                    log.info("Batch fileInfo: {}", fileUtil.fileInfo);
                } else {
                    Thread.sleep(nextDate - fileUtil.fileInfo.getCheckTime());
                    fileUtil.setCurrentFile();
                    nextDate = setNextDate();
                    fileUtil.fw.close();
                    fileUtil.fileInfo = fileUtil.getFileInfo();
                    fileUtil.fw = new FileWriter(fileUtil.fileInfo.getCurrentFile(), true);
                    //새로운 파일 생성
                }
                //            long newTime = System.currentTimeMillis();
                //
                //
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Long setNextDate() {
        return Instant.ofEpochMilli(fileUtil.fileInfo.getCheckTime()).atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atTime(0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
