package com.example.logger_demo.controller;

import com.example.logger_demo.TestService;
import com.example.logger_demo.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConfigController {
    public final LogUtil logUtil;
    public final TestService testService;
    @GetMapping("/setLogLevel")
    public ResponseEntity setLogLevel(@RequestParam("logLevel") int logLevel){
        LogUtil.setLogLevel(logLevel);
        return ResponseEntity.ok("##SET##");
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
}
