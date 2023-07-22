package org.cheise_proj;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final AppConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
