package org.observabilitystack.alertecho.internal;

import java.util.Set;
import java.util.stream.Collectors;

import org.observabilitystack.alertecho.model.Alert;
import org.observabilitystack.alertecho.model.AlertStatus;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.Data;

@Data
public class AlertStatusMetric {

    private static final String METRIC = "alertecho_alerts";

    public static AlertStatusMetric from(Alert alert, MeterRegistry meterRegistry) {
        return new AlertStatusMetric(alert.getStatus(), alert, meterRegistry);
    }

    private final Gauge gauge;
    private AlertStatus status;

    private AlertStatusMetric(AlertStatus status, Alert alert, MeterRegistry meterRegistry) {
        // transform alert tags to Prometheus tags
        final Set<Tag> tags = alert.getLabels().entrySet().stream()
            .map(e -> Tag.of(e.getKey(), e.getValue()))
            .collect(Collectors.toSet());

        this.gauge = Gauge
            .builder(METRIC, () -> status.getRepresentation())
            .tags(tags)
            .register(meterRegistry);
    }
}
