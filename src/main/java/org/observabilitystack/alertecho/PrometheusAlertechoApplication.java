package org.observabilitystack.alertecho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class PrometheusAlertechoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrometheusAlertechoApplication.class, args);
	}

}
