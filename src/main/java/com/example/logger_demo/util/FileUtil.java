package com.example.logger_demo.util;

import com.example.logger_demo.model.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUtil {
//    private final FileBatch fileBatch;

    public static File[] fileList;

    public Queue que = new LinkedList();
    public FileWriter fw = null;
    public static FileInfo fileInfo = new FileInfo();


    public static FileBatch fileBatch = FileBatch.getInstance();


    private static FileUtil instance;
    public static FileUtil getInstance() {
        if(instance == null){
            synchronized (FileUtil.class){
                instance = new FileUtil();
            }
        }
        return instance;
    }

    public static void setCurrentFile() {
        fileInfo.setFileName(LogUtil.logConfig.getName() + "." + new SimpleDateFormat(LogUtil.logConfig.getFileNamePatten()).format(new Date()));
        fileInfo.setCurrentFile(new File(fileInfo.getDir(), fileInfo.getFileName() + ".log"));
    }

    public void fileWriter(String contents) {

        try {
            if (fw == null) {
                fileInfo.setCheckTime(System.currentTimeMillis());
                fileInfo.setDir(new File(LogUtil.logConfig.getFilePath()));
                setCurrentFile();
                fileExistCheck();

                fw = new FileWriter(fileInfo.getCurrentFile(), true);
                fileInfo = getFileInfo();
                fileBatch.start();

            }
//            log.info("contents: {}", contents);
            fw.append(contents);
            fw.flush();

//            long kb =  ;
            long mb = fileInfo.getCurrentFile().length() / 1024 / 1024;

            if (mb > LogUtil.logConfig.getFileSize()) {
                log.info("SIZE: {}, CONF: {}", mb, LogUtil.logConfig.getFileSize());
//                FileInfo fileInfo = getFileInfo();
                // 일별 최대갯수 보다 많이 생성될 경우 가장 오래된 파일 삭제 한다.
                // ???: 오늘의 파일중 가장 오래된? 그동안 생성된 파일중?
                if (fileInfo.getDailyFileCount() >= LogUtil.logConfig.getFileDailyLimit()) {
                    Optional<File> deleteFile = Arrays.stream(fileList)
                            .min(Comparator.comparingLong(File::lastModified));
                    deleteFile.get().delete();
                }
                    File newFile = new File(LogUtil.logConfig.getFilePath() + "/" + fileInfo.getFileName() + "." + fileInfo.getNextIndex() + ".log");
//                    newFile.createNewFile();
                    fw.close();
                    FileUtils.moveFile(fileInfo.getCurrentFile(), newFile);
                    fileInfo.getCurrentFile().createNewFile();
                    fw = new FileWriter(fileInfo.getCurrentFile(), true);
//                    FileUtils.copyFile(fileInfo.getCurrentFile(), newFile);
//                    new FileWriter(fileInfo.getCurrentFile(), false).close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    /**
     * 디렉토리에서 조건에 맞는 파일들 조회하여 nextIndex, dailyFileCount 리턴
     */
    public static FileInfo getFileInfo() {
        fileList = fileInfo.getDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean checkName = name.contains(fileInfo.getFileName());
                boolean except = !name.equals(fileInfo.getFileName() + ".log");
                return (checkName && except);
            }
        });
//        log.info("fileList: {}", (Object) fileList);

//        String s;
////        String[] cmd = {"ls", "-lt", LogUtil.logConfig.getFilePath(), "|",  "awk",  "'{print$9}'", grep "+fileInfo.getCurrentFile()};
//        String[] cmd = {"/bin/bash", "-c", "'ls -lt \\$0 | awk \\$1 | grep \\$2'", LogUtil.logConfig.getFilePath(),"'{print$9}'", fileInfo.getFileName()};
//        log.info("CMD: {}", (Object) cmd);
//        try{
//
//            Process p = Runtime.getRuntime().exec(cmd);
//
//            log.info("INFO: {}", p.info());
//            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            while ((s = br.readLine()) != null)
//                System.out.println("read: "+s);
//            p.waitFor();
//            System.out.println("exit: " + p.exitValue());
//            p.destroy();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if (ArrayUtils.isEmpty(fileList)) {
            fileInfo.setNextIndex(1);
            fileInfo.setDailyFileCount(0);
            return fileInfo;
        } else {
            // 가장 최신 파일 가져온다
            Optional<File> fileIndex = Arrays.stream(fileList)
                    .max(Comparator.comparingLong(File::lastModified));
            log.info("fileIndex: {}, fileName: {}", fileIndex.get().getName() , fileInfo.getFileName());
            fileInfo.setNextIndex(Integer.parseInt(fileIndex.get().getName().replaceFirst(fileInfo.getFileName(),"").split("\\.")[1])+1);
            fileInfo.setDailyFileCount(fileList.length);
            return fileInfo;
        }


    }


    public static void fileExistCheck() {
        if (!fileInfo.getDir().exists()) {
            fileInfo.getDir().mkdirs();
        }
        if (!fileInfo.getCurrentFile().exists()) {
            try {

                fileInfo.getCurrentFile().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
