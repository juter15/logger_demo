package com.example.logger_demo.controller;

import com.example.logger_demo.TestService;
import com.example.logger_demo.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConfigController {
    private final LogUtil logUtil = LogUtil.getInstance();
    public final TestService testService;
    @GetMapping("/setLogLevel")
    public ResponseEntity setLogLevel(@RequestParam("logLevel") int logLevel){
        logUtil.setLogLevel(logLevel);
        return ResponseEntity.ok("##SET LOG LEVEL##");
    }

    @GetMapping("/start")
    public ResponseEntity testStart(){
        testService.testConfigStart(true);
        return ResponseEntity.ok("##START##");
    }
    @GetMapping("/stop")
    public ResponseEntity testStop(){
        testService.testConfigStart(false);
        return ResponseEntity.ok("##STOP##");
    }
    @PostMapping("/setConfig")
    public ResponseEntity setConfig(@RequestBody String configPath){
        log.info("configPath: {}", configPath);
        logUtil.setConfig(configPath);
        return ResponseEntity.ok("### SET CONFIG ###");
    }
}
