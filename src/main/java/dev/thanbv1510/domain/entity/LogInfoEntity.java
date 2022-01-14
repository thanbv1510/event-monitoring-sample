package dev.thanbv1510.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LogInfoEntity {
    private String msgId;
    private Integer systemId;
    private String clientIp;
    private String username;
    private LocalDateTime logTime;
    private Integer mflId;
    private String msisdn;
    private String command;
    private Integer resultFlow;
    private String description;
    private String infoLevel;
    private String stepName;
    private String contentType;
    private String serverName;
    private Long keyTime;
    private String msgContent;
    private Long id;
    private LocalDateTime msgTime;
    private String flowName;
}
