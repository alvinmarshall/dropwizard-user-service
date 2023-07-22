package org.cheise_proj.users;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(UserDto input) {
        User user = User.Builder.builder()
                .name(input.getName())
                .email(input.getEmail())
                .build();
        return userDao.createUser(user);
    }


    public User getUser(long userId) {
        return userDao.getUser(userId).orElseThrow(() -> new RuntimeException("user not found"));
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }


}
