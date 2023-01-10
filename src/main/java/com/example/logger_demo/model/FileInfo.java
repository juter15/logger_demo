package com.example.logger_demo.model;

import lombok.Data;
import lombok.ToString;

import java.io.File;

@Data
public class FileInfo {
    private File dir;
    private File currentFile;
    private long checkTime;
    private String fileName;

    private int nextIndex;
    private int dailyFileCount;
}
