package org.cheise_proj.users;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * The UserDao class is a Data Access Object (DAO) implementation for managing user data
 * in a database. It provides methods to create new users and retrieve users by their IDs.
 */
public class UserDao {
    private final DBI primary;
    private static final int QUERY_TIMEOUT = 1;
    private static final String INSERT_USER = "INSERT INTO users (name, email) VALUES (?, ?)";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String GET_USERS = "SELECT * FROM users";

    /**
     * A private static nested class that implements the ResultSetMapper interface
     * with type Long. It is used to map the result of a query to a Long value.
     */
    private static class LongResultSetMapper implements ResultSetMapper<Long> {
        @Override
        public Long map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
            return resultSet.getLong(1);
        }
    }

    /**
     * A private static nested class that implements the ResultSetMapper interface
     * with type User. It is used to map the result of a query to a User object.
     */
    private static class UserResultSetMapper implements ResultSetMapper<User> {
        @Override
        public User map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
            return User.Builder.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .email(resultSet.getString("email"))
                    .build();
        }
    }

    /**
     * Constructs a new UserDao object with the given DBI instance.
     *
     * @param primary The primary DBI instance used for connecting to the database.
     */
    public UserDao(DBI primary) {
        this.primary = primary;
    }

    /**
     * Creates a new user in the "users" table and returns the newly created user.
     * The method is wrapped in a transaction.
     *
     * @param user The User object representing the user to be created.
     * @return The created User object with the assigned ID.
     */
    @Transaction
    User createUser(final User user) {
        return primary.withHandle(handle -> {
            Long createdId = handle.createStatement(INSERT_USER)
                    .bind(0, user.getName())
                    .bind(1, user.getEmail())
                    .setQueryTimeout(QUERY_TIMEOUT)
                    .executeAndReturnGeneratedKeys(new LongResultSetMapper())
                    .first();

            return User.Builder.builder()
                    .id(Long.parseLong(String.valueOf(createdId)))
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();

        });
    }

    /**
     * Retrieves a user from the "users" table by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return An Optional containing the User object if found, or an empty Optional if the user is not found.
     */
    Optional<User> getUser(final long userId) {
        return Optional.ofNullable(
                this.primary.withHandle(handle ->
                        handle.createQuery(GET_USER_BY_ID)
                                .bind(0, userId)
                                .setQueryTimeout(QUERY_TIMEOUT)
                                .map(new UserResultSetMapper())
                                .first()
                )
        );
    }

    /**
     * Retrieves a list of users from the "users" table in the database.
     *
     * @return A List containing User objects representing all the users retrieved from the database.
     */
    List<User> getUsers() {
        return this.primary.withHandle(handle ->
                handle.createQuery(GET_USERS)
                        .setQueryTimeout(QUERY_TIMEOUT)
                        .map(new UserResultSetMapper())
                        .list()
        );
    }
}
