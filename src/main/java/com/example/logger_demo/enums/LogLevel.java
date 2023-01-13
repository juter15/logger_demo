package com.example.logger_demo.enums;

public enum LogLevel {
    //    FATAL(1),
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4);

    private int logLevel;

    LogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public int getLogLevel() {
        return logLevel;
    }


}
