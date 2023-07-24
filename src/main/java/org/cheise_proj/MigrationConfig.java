package org.cheise_proj;

import io.dropwizard.db.DataSourceFactory;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MigrationConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MigrationConfig.class);
    private MigrationConfig() {
    }

    public static void migrate(DataSourceFactory dataSourceFactory, String migrationFileLocation) {
        LOG.info("Calling Liquibase before starting up...");
        try (Connection connection = DriverManager.getConnection(
                dataSourceFactory.getUrl(),
                dataSourceFactory.getUser(),
                dataSourceFactory.getPassword())
        ) {
            Liquibase liquibase = new Liquibase(
                    migrationFileLocation,
                    new ClassLoaderResourceAccessor(), DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection))
            );
            liquibase.update("");
        } catch (SQLException | LiquibaseException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Liquibase complete");
    }
}
