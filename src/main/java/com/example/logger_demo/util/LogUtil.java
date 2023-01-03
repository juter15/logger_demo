package com.example.logger_demo.util;

import com.example.logger_demo.model.LogConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Formatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogUtil {
    @Value("${log.configPath}")
    public String configPath;

    private static int logLevel = 1;
    public static LogConfig logConfig;

    public static void setLogLevel(int logLevel) {
        LogUtil.logLevel = logLevel;
        log.info("{}",logLevel);
    }

    //파일 형식 XML? JSON? STRING?
    public static void setConfig(String configFilePath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFilePath));
            // setConig
            // logConfig =

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    public static LogConfig getLogConfig(){
//        return logConfig;
//    }
//
    public static void getInstance() {

    }

    public static void printf(int logLevel, String format, Object... args) {
        if (LogUtil.logLevel >= logLevel) {
            String contents = new Formatter().format(format, args).toString();
            FileUtil.fileWriter(contents);
        }
    }

    public static void setAttr(String type, int val) {

    }

    public static void setAttr(String type, String val) {

    }
}
