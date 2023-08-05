package org.cheise_proj.users;

import io.dropwizard.setup.Environment;
import org.cheise_proj.AbstractConfigurator;
import org.cheise_proj.AppConfiguration;
import org.cheise_proj.pubsub.ArtemisClient;
import org.cheise_proj.pubsub.UserCreationConsumer;
import org.skife.jdbi.v2.DBI;

public class UserConfigurator extends AbstractConfigurator {
    public UserConfigurator(final AppConfiguration config, final Environment environment) {
        super(config, environment);
    }


    public final void build(final DBI primary, ArtemisClient artemisClient) {
        final UserDao userDao = new UserDao(primary);
        final UserService userService = new UserService(userDao, artemisClient);
        final UserCreationConsumer userCreationConsumer = new UserCreationConsumer(
                environment.metrics(),
                environment.getObjectMapper(),
                artemisClient.getCreationConsumer()
        );
        userCreationConsumer.consume(userService::handleUserCreatedEvent);
        environment.jersey().register(new UserResource(userService));
    }
}
