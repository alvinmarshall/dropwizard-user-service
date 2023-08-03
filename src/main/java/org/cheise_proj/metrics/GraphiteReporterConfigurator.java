package org.cheise_proj.metrics;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import io.dropwizard.setup.Environment;
import org.cheise_proj.AbstractConfigurator;
import org.cheise_proj.AppConfiguration;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class GraphiteReporterConfigurator extends AbstractConfigurator {
    public GraphiteReporterConfigurator(AppConfiguration config, Environment environment) {
        super(config, environment);
    }

    public void build() {
        String serverUrl = config.getGraphiteConfig().getServerUrl();
        int port = config.getGraphiteConfig().getPort();
        Graphite graphite = new Graphite(new InetSocketAddress(serverUrl, port));
        GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(environment.metrics())
                .prefixedWith(environment.getName())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        graphiteReporter.start(config.getGraphiteConfig().getReportInterval(), TimeUnit.SECONDS);
    }
}
