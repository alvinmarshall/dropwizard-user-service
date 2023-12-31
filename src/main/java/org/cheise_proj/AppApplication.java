package org.cheise_proj;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.cheise_proj.metrics.PrometheusRegistry;

public class AppApplication extends Application<AppConfiguration> {

    private static final String APP_NAME = "App";

    public static void main(final String[] args) throws Exception {
        new AppApplication().run(args);
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<AppConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(AppConfiguration appConfiguration) {
                return appConfiguration.getDatabase();
            }
        });
        bootstrap.addBundle(new SwaggerBundle<AppConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfiguration appConfiguration) {
                return appConfiguration.getSwagger();
            }
        });
    }

    @Override
    public void run(final AppConfiguration configuration, final Environment environment) {
        MigrationConfig.migrate(configuration.getDatabase(), configuration.getMigrationFileLocation());
        PrometheusRegistry.init(environment);
        buildComponentFactory(configuration, environment).buildAll();
    }

    protected ComponentFactory buildComponentFactory(final AppConfiguration config, final Environment environment) {
        return new ComponentFactory(config, environment);
    }

}
