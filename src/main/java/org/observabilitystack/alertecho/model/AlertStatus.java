package org.observabilitystack.alertecho.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertStatus {

    resolved(0), firing(1);

    private final Number representation;
}
