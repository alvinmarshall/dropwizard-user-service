package org.cheise_proj.metrics;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.setup.Environment;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.MetricsServlet;

public class PrometheusRegistry {
    private PrometheusRegistry() {
    }

    public static void init(final Environment environment) {
        // Get the Dropwizard MetricRegistry
        MetricRegistry metricRegistry = environment.metrics();

        // Register Dropwizard metrics with Prometheus
        final CollectorRegistry collectorRegistry = new CollectorRegistry();
        collectorRegistry.register(new DropwizardExports(metricRegistry));

        // Register the Prometheus servlet with Dropwizard
        final MetricsServlet metricsServlet = new MetricsServlet(collectorRegistry);
        environment.servlets().addServlet("metrics", metricsServlet).addMapping("/metrics");
    }
}
