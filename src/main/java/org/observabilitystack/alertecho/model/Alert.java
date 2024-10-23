package org.observabilitystack.alertecho.model;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = "status")
@ToString
public class Alert {

    // TODO: store updated up as instant
    private AlertStatus status;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private String startsAt;
    private String endsAt;
    private String generatorURL;
}
