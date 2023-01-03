package com.example.logger_demo.enums;

public enum LogLevel {
    LOG_LEVEL1(1),
    LOG_LEVEL2(2),
    LOG_LEVEL3(3),
    LOG_LEVEL4(4);

    private int logLevel;
    LogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public int getLogLevel() {
        return logLevel;
    }
}
