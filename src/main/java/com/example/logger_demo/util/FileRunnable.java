package com.example.logger_demo.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileRunnable implements Runnable {


    private static FileRunnable instance;
    private Thread worker;
    private long nextDate;
    private static FileUtil fileUtil = FileUtil.getInstance();

    public static FileRunnable getInstance() {
        if (instance == null) {
            synchronized (FileRunnable.class) {
                instance = new FileRunnable();
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
                fileUtil.fileExistCheck();
                if (fileUtil.fileInfo.getCheckTime()+60000 < nextDate) {
                    Thread.sleep(60000);
                    fileUtil.fileInfo = fileUtil.getFileList();
                    log.info("Batch fileInfo: {}", fileUtil.fileInfo);
                    fileUtil.fileInfo.setCheckTime(System.currentTimeMillis());
                } else {
                    Thread.sleep(nextDate - fileUtil.fileInfo.getCheckTime());
                    fileUtil.setCurrentFile();
                    nextDate = setNextDate();
                    fileUtil.fw.close();
                    fileUtil.fileInfo = fileUtil.getFileList();
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
