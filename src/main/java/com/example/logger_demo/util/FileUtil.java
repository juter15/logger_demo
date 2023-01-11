package com.example.logger_demo.util;

import com.example.logger_demo.model.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUtil {
//    private final FileBatch fileBatch;

    private List<File> fileList;
    public FileWriter fw = null;
    public FileInfo fileInfo = new FileInfo();
    private File newFile;
    private File deleteFile;
    public FileRunnable fileRunnable = FileRunnable.getInstance();


    private static FileUtil instance;

    public static FileUtil getInstance() {
        if (instance == null) {
            synchronized (FileUtil.class) {
                instance = new FileUtil();
            }
        }
        return instance;
    }

    public void setCurrentFile() {
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
                getFileList();
                log.info("FILE INFO : {}", fileInfo);
                fileRunnable.start();
            }
            fw.append(contents);
            fw.flush();

            long mb = fileInfo.getCurrentFile().length() / 1024 / 1024;

            if (mb > LogUtil.logConfig.getFileSize()) {
                log.info("SIZE: {}, CONF: {}", mb, LogUtil.logConfig.getFileSize());
                if (fileList.size() >= LogUtil.logConfig.getFileDailyLimit()) {
                    deleteFile();
                }
                fw.close();
                newFile = new File(LogUtil.logConfig.getFilePath() + "/" + fileInfo.getFileName() + "." + getNextIndex() + ".log");
                fileList.add(newFile);
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
//    public static FileInfo getFileInfo() {
//        fileList = fileInfo.getDir().listFiles(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                boolean checkName = name.contains(fileInfo.getFileName());
//                boolean except = !name.equals(fileInfo.getFileName() + ".log");
//                return (checkName && except);
//            }
//        });
////        log.info("fileList: {}", (Object) fileList);
//        getFileList();
//
//        if (ArrayUtils.isEmpty(fileList)) {
//            fileInfo.setNextIndex(1);
//            fileInfo.setDailyFileCount(0);
//            return fileInfo;
//        } else {
//            // 가장 최신 파일 가져온다
//            Optional<File> fileIndex = Arrays.stream(fileList)
//                    .max(Comparator.comparingLong(File::lastModified));
//            log.info("fileIndex: {}, fileName: {}", fileIndex.get().getName(), fileInfo.getFileName());
//            fileInfo.setNextIndex(Integer.parseInt(fileIndex.get().getName().replaceFirst(fileInfo.getFileName(), "").split("\\.")[1]) + 1);
//            fileInfo.setDailyFileCount(fileList.length);
//            return fileInfo;
//        }
//
//
//    }
    public FileInfo getFileList() {
        fileList = new ArrayList<>();

        String s;
        String[] cmd = {"/bin/sh", "-c", "ls -lt -r " + LogUtil.logConfig.getFilePath() + " | awk '{print$9}'" + " | grep -v " + fileInfo.getFileName() + ".log | grep " + fileInfo.getFileName()};
        log.info("CMD: {}", (Object) cmd);
        try {
            Process p = Runtime.getRuntime().exec(cmd);


            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                fileList.add(new File(LogUtil.logConfig.getFilePath()+ "/" + s));
            }
            log.info("Command List: {}",fileList);
            p.waitFor();
            p.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    public int getNextIndex() {
        if (fileList.isEmpty()) {
            return 1;
        } else {
            return Integer.parseInt(fileList.get(fileList.size() - 1).getName().replaceFirst(fileInfo.getFileName(), "").split("\\.")[1]) + 1;
        }
    }

    public void deleteFile() {
        deleteFile = fileList.get(0);
        fileList.remove(0);
        log.info("delete File: {}", deleteFile);
        log.info("delete List: {}", fileList);
        if (deleteFile.delete()) {
            log.info("###파일 삭제 성공###");
        } else {
            log.info("###파일 삭제 실패###");
        }
    }

    public void fileExistCheck() {
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
