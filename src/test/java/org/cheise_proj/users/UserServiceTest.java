package org.cheise_proj.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final AutoCloseable closeable = MockitoAnnotations.openMocks(this);
    private UserService sut;
    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        sut = new UserService(userDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void register() {
        User user = User.Builder.builder().id(1L).name("test").email("test@me.com").build();
        Mockito.when(userDao.createUser(ArgumentMatchers.any(User.class))).thenReturn(user);
        User actual = sut.register(new UserDto("test", "test@me.com"));
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("test@me.com", actual.getEmail());
    }

    @Test
    void getUser() {
        Mockito.when(userDao.getUser(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(
                        User.Builder.builder()
                                .id(1L)
                                .name("test")
                                .email("test@me.com")
                                .build())
                );
        User actual = sut.getUser(1L);
        Assertions.assertEquals("test", actual.getName());
    }

    @Test
    void getUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.Builder.builder().id(1L).name("test").email("test@me.com").build());
        Mockito.when(userDao.getUsers()).thenReturn(users);
        List<User> actual = sut.getUsers();
        Assertions.assertEquals(1, actual.size());
    }
}
