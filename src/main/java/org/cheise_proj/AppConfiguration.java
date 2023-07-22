package org.cheise_proj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class AppConfiguration extends Configuration {
    private final DataSourceFactory database;

    @JsonCreator
    public AppConfiguration(@JsonProperty("database") DataSourceFactory database) {
        this.database = database;
    }

    public DataSourceFactory getDatabase() {
        return database;
    }
}
