package org.cheise_proj;

import io.dropwizard.setup.Environment;

public abstract class AbstractConfigurator {
    protected final AppConfiguration config;
    protected final Environment environment;

    protected AbstractConfigurator(final AppConfiguration config, final Environment environment) {
        this.config = config;
        this.environment = environment;
    }
}
