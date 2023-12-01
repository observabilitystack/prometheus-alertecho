package org.observabilitystack.alertecho;

import org.observabilitystack.alertecho.model.Alerts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertReceiverController {

    @Autowired
    private AlertRepository repository;

    @PostMapping("/alerts")
    public void alert(@RequestBody Alerts alerts) {
        repository.addAll(alerts.getAlerts());
    }

}
