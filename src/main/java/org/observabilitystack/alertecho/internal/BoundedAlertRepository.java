package org.observabilitystack.alertecho.internal;

import java.util.Collection;
import java.util.stream.Stream;

import org.apache.commons.collections4.BoundedCollection;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.observabilitystack.alertecho.AlertRepository;
import org.observabilitystack.alertecho.model.Alert;
import org.springframework.stereotype.Repository;

@Repository
public class BoundedAlertRepository implements AlertRepository {

    private final BoundedCollection<Alert> storage;

    public BoundedAlertRepository() {
        this(4096);
    }

    public BoundedAlertRepository(int maxAlerts2echo) {
        this.storage = new CircularFifoQueue<Alert>(maxAlerts2echo);
    }

    public int getCapacity() {
        return this.storage.maxSize();
    }

    @Override
    public void add(Alert alert) {
        storage.add(alert);
    }

    @Override
    public void addAll(Collection<Alert> alerts) {
        storage.addAll(alerts);
    }

    @Override
    public Stream<Alert> stream() {
        return storage.stream();
    }

}
