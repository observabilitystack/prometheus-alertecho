package org.observabilitystack.alertecho.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.observabilitystack.alertecho.AlertRepository;
import org.observabilitystack.alertecho.model.Alert;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Service
@Primary
public class MetricEmittingAlertRepository extends DelegatingAlertRepository {

    private static final String METRIC = "alertecho_alerts_stored";

    private final MeterRegistry meterRegistry;

    private final Map<Alert, AlertStatusMetric> registeredAlerts;

    public MetricEmittingAlertRepository(AlertRepository delegate, MeterRegistry meterRegistry) {
        super(delegate);

        this.meterRegistry = meterRegistry;
        this.registeredAlerts = new HashMap<>();

        Gauge.builder(METRIC, () -> registeredAlerts.size()).register(meterRegistry);
    }

    @Override
    public void add(Alert alert) {
        if (!registeredAlerts.containsKey(alert)) {
            final AlertStatusMetric asg = AlertStatusMetric.from(alert, meterRegistry);
            registeredAlerts.put(alert, asg);
        } else {
            registeredAlerts.get(alert).setStatus(alert.getStatus());
        }

        // proceed to delegate
        super.add(alert);
    }

    @Override
    public void addAll(Collection<Alert> alerts) {
        alerts.stream().forEach(a -> add(a));
    }

    // Visible for testing
    Map<Alert, AlertStatusMetric> getRegisteredAlerts() {
        return registeredAlerts;
    }
}
