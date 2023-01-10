package com.example.logger_demo;

import com.example.logger_demo.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    private LogUtil logUtil = LogUtil.getInstance();
    @Value("${log.configPath}")
    public String configPath;
    private boolean state = false;

    public void testConfigStart(boolean state) {
        logUtil.setConfig(configPath);
        logUtil.setLogLevel(4);
        this.state = state;

        Random random = new Random();

        int index = 1;
        while (this.state) {
            int rd = random.nextInt(4) + 1;
            LogUtil.printf(rd, "%d %s\n", index, "TEST" + rd);
            index++;
        }

    }
}
