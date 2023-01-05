package com.example.logger_demo.util;

import com.example.logger_demo.model.LogConfig;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Formatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogUtil {
    @Value("${log.configPath}")
    public String configPath;

    private static LogUtil instance;

    private static int logLevel = 1;
    public static LogConfig logConfig;

    public static LogUtil getInstance() {
        if(instance == null){
            synchronized (LogUtil.class){
                instance = new LogUtil();
            }
        }
        return instance;
    }

    public void setLogLevel(int logLevel) {
        LogUtil.logLevel = logLevel;
        log.info("{}", LogUtil.logLevel);
    }

    //파일 형식 XML? JSON? STRING?
    public void setConfig(String configFilePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFilePath));
            JAXBContext jaxbContext = JAXBContext.newInstance(LogConfig.class); // JAXB Context 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller(); // Unmarshaller Object 생성
            logConfig = (LogConfig) unmarshaller.unmarshal(br);
            log.info("config: {}", logConfig);
            br.close();

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }


    public static void printf(int logLevel, String format, Object... args) {
        if (LogUtil.logLevel >= logLevel) {
            String contents = new Formatter().format(format, args).toString();
            FileUtil.fileWriter(contents);
        }
    }


    /**
     * type에는 callid 외 추가 기능
     */
    public void setAttr(String type, int val) {

    }

    public void setAttr(String type, String val) {

    }
}
