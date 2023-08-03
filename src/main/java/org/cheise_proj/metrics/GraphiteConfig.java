package org.cheise_proj.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphiteConfig {
    private String serverUrl;
    private int port;
    private int reportInterval;

    @JsonCreator
    public GraphiteConfig(
            @JsonProperty("serverUrl") String serverUrl,
            @JsonProperty("port") int port,
            @JsonProperty("reportInterval") int reportInterval
    ) {
        this.serverUrl = serverUrl;
        this.port = port;
        this.reportInterval = reportInterval;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReportInterval() {
        return reportInterval;
    }

    public void setReportInterval(int reportInterval) {
        this.reportInterval = reportInterval;
    }
}
