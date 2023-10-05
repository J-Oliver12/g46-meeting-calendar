package se.lexicon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.lexicon.dao.UserDao;
import se.lexicon.exception.AuthenticationFailedException;
import se.lexicon.exception.MySQLException;
import se.lexicon.exception.UserExpiredException;
import se.lexicon.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LogManager.getLogger(UserDaoImpl.class);

    private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User createUser(String username) {
        String query = "INSERT INTO USERS(USERNAME, _PASSWORD) VALUES(?,?)";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            User user = new User(username);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getHashedPassword());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new MySQLException("Creating user failed, no rows affected");
            }
            return user;
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while creating user: " + username, e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM USERS WHERE USERNAME = ?";

        try (
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String foundUsername = resultSet.getString("USERNAME");
                String foundPassword = resultSet.getString("_PASSWORD");
                boolean foundExpired = resultSet.getBoolean("EXPIRED");
                User user = new User(foundUsername, foundPassword, foundExpired);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new MySQLException("Error occurred while finding user by username: " + username, e);
        }

    }

    @Override
    public boolean authenticate(User user) throws AuthenticationFailedException, UserExpiredException {
        String query = "SELECT * FROM USERS WHERE USERNAME = ?";
        log.info("authenticate user: {}:", user.getUsername());;
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {

            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                boolean isExpired = resultSet.getBoolean("EXPIRED");
                if (isExpired) {
                    log.warn("User is Expired. username: {}", user.getUsername());
                    throw new UserExpiredException("User is Expired. username: " + user.getUsername());
                }

                String hashedPassword = resultSet.getString("_PASSWORD");
                user.checkHash(hashedPassword);

            } else {
                log.warn("Authentication failed. Invalid credentials.");
                throw new AuthenticationFailedException("Authentication failed. Invalid credentials.");
            }

            return true;

        } catch (SQLException e) {
            throw new MySQLException("Error occurred while authenticating user by username: " + user.getUsername(), e);
        }
    }

    @Override
    public String getHashedPassword(User user) {
        String query = "SELECT _PASSWORD FROM USERS WHERE USERNAME = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("_PASSWORD");
            } else {
                return null; // User not found
            }
        } catch (SQLException e) {
            throw new MySQLException("Error occurred while retrieving hashed password for user: " + user.getUsername(), e);
        }
    }

}
