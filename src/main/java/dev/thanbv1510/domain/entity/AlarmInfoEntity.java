package dev.thanbv1510.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AlarmInfoEntity {
    private Long alarmId;
    private LocalDate alarmTime;
    private Integer mflId;
    private LocalDate sendTime;
    private Integer status;
    private Integer systemId;
    private String alarm;
    private String serverName;
    private String stepName;
    private Long maxSendId;
    private String msgId;
}
