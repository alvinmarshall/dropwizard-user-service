package org.cheise_proj;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.cheise_proj.users.UserDao;
import org.cheise_proj.users.UserResource;
import org.cheise_proj.users.UserService;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppApplication extends Application<AppConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(AppApplication.class);

    public static void main(final String[] args) throws Exception {
        new AppApplication().run(args);
    }

    @Override
    public String getName() {
        return "App";
    }

    @Override
    public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<AppConfiguration>() {

            @Override
            public PooledDataSourceFactory getDataSourceFactory(AppConfiguration appConfiguration) {
                return appConfiguration.getDatabase();
            }

        });
    }

    @Override
    public void run(final AppConfiguration configuration, final Environment environment) {
        migrate(configuration.getDatabase(), configuration.getMigrationFileLocation());
        final DBI primary = buildDBI(environment, configuration.getDatabase());
        final UserDao userDao = new UserDao(primary);
        final UserService userService = new UserService(userDao);
        environment.jersey().register(new UserResource(userService));
    }


    private static DBI buildDBI(Environment environment, DataSourceFactory dataSourceFactory) {
        return new DBIFactory().build(environment, dataSourceFactory, "users");
    }

    private static void migrate(DataSourceFactory dataSourceFactory, String migrationFileLocation) {
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
