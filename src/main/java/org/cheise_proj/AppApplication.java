package org.cheise_proj;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.cheise_proj.exception.CustomExceptionMapper;
import org.cheise_proj.exception.CustomViolationExceptionMapper;
import org.cheise_proj.users.UserDao;
import org.cheise_proj.users.UserResource;
import org.cheise_proj.users.UserService;
import org.skife.jdbi.v2.DBI;

public class AppApplication extends Application<AppConfiguration> {

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
        MigrationConfig.migrate(configuration.getDatabase(), configuration.getMigrationFileLocation());
        final DBI primary = buildDBI(environment, configuration.getDatabase());
        environment.jersey().register(new UserResource(buildUserService(primary)));
        buildException(environment);
    }

    private static UserService buildUserService(DBI primary) {
        final UserDao userDao = new UserDao(primary);
        return new UserService(userDao);
    }

    private static void buildException(Environment environment) {
        environment.jersey().register(new CustomExceptionMapper());
        environment.jersey().register(new CustomViolationExceptionMapper());
    }


    private static DBI buildDBI(Environment environment, DataSourceFactory dataSourceFactory) {
        return new DBIFactory().build(environment, dataSourceFactory, "users");
    }

}
