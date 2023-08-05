package org.cheise_proj.users;

import org.cheise_proj.pubsub.ArtemisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;
    private final ArtemisClient artemisClient;

    public UserService(final UserDao userDao, ArtemisClient artemisClient) {
        this.userDao = userDao;
        this.artemisClient = artemisClient;
    }

    public User register(UserDto input) {
        User user = User.Builder.builder()
                .name(input.getName())
                .email(input.getEmail())
                .build();
        User entity = userDao.createUser(user);
        UserCreatedEvent event = UserCreatedEvent.Builder.builder()
                .userId(entity.getId())
                .email(entity.getEmail())
                .dateCreated(Instant.now())
                .build();
        artemisClient.produceMessage(event, ArtemisClient.USER_CREATION_QUEUE_NAME);
        return entity;
    }


    public User getUser(long userId) {
        return userDao.getUser(userId).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }


    public void handleUserCreatedEvent(UserCreatedEvent event) {
        LOG.info("handling user created event: {}", event);
    }
}
