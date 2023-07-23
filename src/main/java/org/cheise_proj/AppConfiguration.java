package org.cheise_proj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class AppConfiguration extends Configuration {
    private final DataSourceFactory database;
    private final String migrationFileLocation;

    @JsonCreator
    public AppConfiguration(
            @JsonProperty("database") DataSourceFactory database,
            @JsonProperty("migrationFileLocation") String migrationFileLocation) {
        this.database = database;
        this.migrationFileLocation = migrationFileLocation;
    }

    public DataSourceFactory getDatabase() {
        return database;
    }

    public String getMigrationFileLocation() {
        return migrationFileLocation;
    }
}
