package org.cheise_proj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class AppConfiguration extends Configuration {
    private final DataSourceFactory database;
    private final String migrationFileLocation;
    private final SwaggerBundleConfiguration swagger;

    @JsonCreator
    public AppConfiguration(
            @JsonProperty("database") DataSourceFactory database,
            @JsonProperty("migrationFileLocation") String migrationFileLocation,
            @JsonProperty("swagger") SwaggerBundleConfiguration swagger) {
        this.database = database;
        this.migrationFileLocation = migrationFileLocation;
        this.swagger = swagger;
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
}
