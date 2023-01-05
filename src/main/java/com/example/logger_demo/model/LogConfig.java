package com.example.logger_demo.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;


@Data
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "log")
@NoArgsConstructor
@AllArgsConstructor
public class LogConfig {
    @XmlAttribute(name = "filePath")
    private String filePath;
    @XmlAttribute(name = "namePattern")
    private String fileNamePatten;
    @XmlAttribute(name = "fileSize")
    private long fileSize;
    @XmlAttribute(name = "fileDailyLimit")
    private int fileDailyLimit;
    @XmlAttribute(name = "serverIp")
    private String serverIp;
    @XmlAttribute(name = "serverPort")
    private int serverPort;


}
