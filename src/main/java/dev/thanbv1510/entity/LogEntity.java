package dev.thanbv1510.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogEntity {
    private int id;
    private String flow;
    private String msg;
    private String timeReceive;
    private String nodeLabel;
    private String nodeType;
}
