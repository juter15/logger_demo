package com.example.logger_demo.util;

import com.example.logger_demo.model.LogConfig;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
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
            BufferedReader br = new BufferedReader(new FileReader(configFilePath));
            JAXBContext jaxbContext = JAXBContext.newInstance(LogConfig.class); // JAXB Context 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller(); // Unmarshaller Object 생성
            logConfig = (LogConfig) unmarshaller.unmarshal(br);
            log.info("config: {}", logConfig);
            br.close();
            // setConig
            // logConfig =

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
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
