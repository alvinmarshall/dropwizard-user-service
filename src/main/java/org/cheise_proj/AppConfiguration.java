package org.cheise_proj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.cheise_proj.metrics.GraphiteConfig;

public class AppConfiguration extends Configuration {
    private final DataSourceFactory database;
    private final String migrationFileLocation;
    private final SwaggerBundleConfiguration swagger;
    private final GraphiteConfig graphiteConfig;

    @JsonCreator
    public AppConfiguration(
            @JsonProperty("database") final DataSourceFactory database,
            @JsonProperty("migrationFileLocation") final String migrationFileLocation,
            @JsonProperty("swagger") final SwaggerBundleConfiguration swagger,
            @JsonProperty("graphite") GraphiteConfig graphiteConfig) {
        this.database = database;
        this.migrationFileLocation = migrationFileLocation;
        this.swagger = swagger;
        this.graphiteConfig = graphiteConfig;
    }

    public DataSourceFactory getDatabase() {
        return database;
    }

    public String getMigrationFileLocation() {
        return migrationFileLocation;
    }

    public SwaggerBundleConfiguration getSwagger() {
        return swagger;
    }

    public GraphiteConfig getGraphiteConfig() {
        return graphiteConfig;
    }
}
