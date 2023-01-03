package com.example.logger_demo;

import com.example.logger_demo.model.LogConfig;
import com.example.logger_demo.util.FileUtil;
import com.example.logger_demo.util.LogUtil;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    private final LogUtil logUtil;
    private boolean state = false;

    public void testConfigStart(boolean state) {
        try {
            this.state = state;
            BufferedReader br = new BufferedReader(new FileReader(logUtil.configPath));

            JAXBContext jaxbContext = JAXBContext.newInstance(LogConfig.class); // JAXB Context 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller(); // Unmarshaller Object 생성
            LogUtil.logConfig = (LogConfig) unmarshaller.unmarshal(br);
            log.info("config: {}", LogUtil.logConfig);

            Random random = new Random();

            while (this.state) {
                int rd = random.nextInt(4) + 1;
                LogUtil.printf(rd, "%s\n%s\n", "TEST" + rd, "TEST" + rd);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }
}
