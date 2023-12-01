package org.observabilitystack.alertecho.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Alerts {

    private String version;
    private String groupKey;
    private AlertStatus status;
    private String receiver;
    private Map<String, String> groupLabels;
    private Map<String, String> commonLabels;
    private String externalURL;
    private List<Alert> alerts;
}
