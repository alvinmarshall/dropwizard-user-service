package org.cheise_proj.users;

import io.dropwizard.setup.Environment;
import org.cheise_proj.AbstractConfigurator;
import org.cheise_proj.AppConfiguration;
import org.skife.jdbi.v2.DBI;

public class UserConfigurator extends AbstractConfigurator {
    public UserConfigurator(final AppConfiguration config, final Environment environment) {
        super(config, environment);
    }


    public final void build(final DBI primary) {
        final UserDao userDao = new UserDao(primary);
        UserService userService = new UserService(userDao);
        environment.jersey().register(new UserResource(userService));
    }
}
