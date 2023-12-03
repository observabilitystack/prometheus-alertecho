package org.observabilitystack.alertecho.internal;

import static org.junit.Assert.assertTrue;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
@AutoConfigureObservability
@Disabled
public class PrometheusAlertmanagerIntegrationTest {

    public static final Network network = Network.newNetwork();

    @Container
    public static GenericContainer prometheus = new GenericContainer(
            DockerImageName.parse("prom/prometheus:v2.48.0"))
            .withNetwork(network)
            .withNetworkAliases("prometheus")

            .withCopyToContainer(MountableFile.forClasspathResource("prometheus.yaml"),
                    "/etc/prometheus/prometheus.yaml")
            .withCopyToContainer(MountableFile.forClasspathResource("alerts.yaml"),
                    "/etc/prometheus/alerts/alerts.yaml")
            .withCommand("--config.file=/etc/prometheus/prometheus.yaml");

    @Container
    public static GenericContainer alertmanager = new GenericContainer(
            DockerImageName.parse("prom/alertmanager:v0.26.0"))
            .withNetwork(network)
            .withNetworkAliases("alertmanager")
            .withCopyToContainer(MountableFile.forClasspathResource("alertmanager.yaml"),
                    "/etc/prometheus/alertmanager.yaml")
            .withCommand("--config.file=/etc/prometheus/alertmanager.yaml");

    @Autowired
    private MetricEmittingAlertRepository alertRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test() throws InterruptedException {

        // wait for e2e alert to kick in
        while (alertRepository.getRegisteredAlerts().isEmpty()) {
            Thread.sleep(100);
        }

        // retrieve metrics
        ResponseEntity<String> metrics = testRestTemplate.getForEntity(String.format("http://localhost:%s/metrics", port), String.class);

        assertTrue(metrics.getStatusCode().is2xxSuccessful());
        MatcherAssert.assertThat(metrics.getBody(), Matchers.containsString("alertecho_alerts{alertname=\"PrometheusAlertmanagerE2eDeadManSwitch\",severity=\"critical\",} 1.0"));
    }

}
