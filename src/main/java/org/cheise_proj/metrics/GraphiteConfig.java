package org.cheise_proj.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphiteConfig {
    private final String serverUrl;
    private final int port;
    private final int reportInterval;

    @JsonCreator
    public GraphiteConfig(
            @JsonProperty("serverUrl") String serverUrl,
            @JsonProperty("port") String port,
            @JsonProperty("reportInterval") String reportInterval
    ) {
        this.serverUrl = serverUrl;
        this.port = Integer.parseInt(port);
        this.reportInterval = Integer.parseInt(reportInterval);
    }

    public String getServerUrl() {
        return serverUrl;
    }
    public int getPort() {
        return port;
    }
    public int getReportInterval() {
        return reportInterval;
    }
}
