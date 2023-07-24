package org.cheise_proj.exception;

import io.dropwizard.setup.Environment;
import org.cheise_proj.AbstractConfigurator;
import org.cheise_proj.AppConfiguration;

public class CustomExceptionConfigurator extends AbstractConfigurator {
    public CustomExceptionConfigurator(AppConfiguration config, Environment environment) {
        super(config, environment);
    }

    public void build() {
        configureCustomException();
    }


    protected void configureCustomException() {
        environment.jersey().register(new CustomExceptionMapper());
        environment.jersey().register(new CustomViolationExceptionMapper());
    }
}
