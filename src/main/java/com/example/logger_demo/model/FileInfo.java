package com.example.logger_demo.model;

import lombok.Data;
import lombok.ToString;

@Data
public class FileInfo {
    private int lastFileIndex;
    private int dailyFileCount;
}
