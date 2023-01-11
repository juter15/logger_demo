package com.example.logger_demo.model;

import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
@ToString
public class FileInfo {
    private File dir;
    private File currentFile;
    private long currentTime;
    private long NextDateTime;
    private String fileName;

}
