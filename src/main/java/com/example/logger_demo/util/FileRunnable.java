//package com.example.logger_demo.util;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.Instant;
//import java.time.ZoneId;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FileRunnable implements Runnable {
//
//
//    private static FileRunnable instance;
//    private Thread worker;
//    private long nextDate;
//    private static FileUtil fileUtil = FileUtil.getInstance();
//
//    public static FileRunnable getInstance() {
//        if (instance == null) {
//            synchronized (FileRunnable.class) {
//                instance = new FileRunnable();
//            }
//        }
//        return instance;
//    }
//
//    public void start() {
//        worker = new Thread(this);
//        worker.start();
//    }
//
//    public void stop() {
//        System.out.println("### STOP ###");
//    }
//
//    @Override
//    public void run() {
//        nextDate = setNextDate();
//        log.info("checkTime: {}, nextDate: {}", fileUtil.fileInfo.getCheckTime(), nextDate);
////        log.info("next Date: {}", LocalDateTime.ofInstant(Instant.ofEpochMilli(nextDate), TimeZone.getDefault().toZoneId()));
//
//        while (true) {
//            try {
//                fileUtil.fileExistCheckAndCreate();
//                if (fileUtil.fileInfo.getCheckTime() + 60000 < nextDate) {
//                    Thread.sleep(60000);
//                } else {
//                    Thread.sleep(nextDate - fileUtil.fileInfo.getCheckTime());
//                    fileUtil.fw.close();
//                    fileUtil.setCurrentFile();
//                    fileUtil.fw = new FileWriter(fileUtil.fileInfo.getCurrentFile(), true);
//                    nextDate = setNextDate();
//                    //새로운 파일 생성
//                }
//                fileUtil.fileInfo.setCheckTime(System.currentTimeMillis());
//                fileUtil.fileInfo = fileUtil.getFileList();
//                log.info("Batch CheckTime: {}", fileUtil.fileInfo.getCheckTime());
//            } catch (InterruptedException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//}
