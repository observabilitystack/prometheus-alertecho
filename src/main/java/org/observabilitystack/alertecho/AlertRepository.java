package org.observabilitystack.alertecho;

import java.util.Collection;
import java.util.stream.Stream;

import org.observabilitystack.alertecho.model.Alert;

public interface AlertRepository {

    void add(Alert alert);

    void addAll(Collection<Alert> alerts);

    Stream<Alert> stream();

}
