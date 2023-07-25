package org.cheise_proj;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.cheise_proj.exception.CustomExceptionConfigurator;
import org.cheise_proj.users.UserConfigurator;
import org.skife.jdbi.v2.DBI;

public class ComponentFactory extends AbstractConfigurator {

    private static final String CLIENT_NAME = "users";

    protected ComponentFactory(final AppConfiguration config, final Environment environment) {
        super(config, environment);
    }

    public void buildAll() {
        final DBI primary = buildDBI();
        configureUser(primary);
        configureException();
    }

    private void configureUser(final DBI primary) {
        new UserConfigurator(config, environment).build(primary);
    }

    private void configureException() {
        new CustomExceptionConfigurator(config, environment).build();
    }

    private DBI buildDBI() {
        return new DBIFactory().build(environment, config.getDatabase(), CLIENT_NAME);
    }
}
