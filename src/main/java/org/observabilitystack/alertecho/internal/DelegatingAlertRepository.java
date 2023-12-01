package org.observabilitystack.alertecho.internal;

import java.util.Collection;
import java.util.stream.Stream;

import org.observabilitystack.alertecho.AlertRepository;
import org.observabilitystack.alertecho.model.Alert;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DelegatingAlertRepository implements AlertRepository {

    private final AlertRepository delegate;

    @Override
    public void add(Alert alert) {
        delegate.add(alert);
    }

    @Override
    public void addAll(Collection<Alert> alerts) {
        delegate.addAll(alerts);
    }

    @Override
    public Stream<Alert> stream() {
        return delegate.stream();
    }

}
