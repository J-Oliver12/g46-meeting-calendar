package se.lexicon.dao.impl;

import se.lexicon.dao.MeetingCalendarDao;
import se.lexicon.exception.MySQLException;
import se.lexicon.model.MeetingCalendar;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MeetingCalendarDaoImpl implements MeetingCalendarDao {

        private Connection connection;

        public MeetingCalendarDaoImpl(Connection connection) {
            this.connection = connection;
        }


        @Override
        public MeetingCalendar createMeetingCalendar(String title, String username) {
            String insertQuery = "INSERT INTO meeting_calendars (username, title) VALUES (?, ?)";

            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, title);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    String errorMessage = "Creating calendar failed, no rows affected.";
                    throw new MySQLException(errorMessage);
                }

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int calendarId = generatedKeys.getInt(1);
                        return new MeetingCalendar(calendarId, username, title);
                    } else {
                        String errorMessage = "Creating calendar failed, no ID obtained.";
                        throw new MySQLException(errorMessage);
                    }
                }

            } catch (SQLException e) {
                String errorMessage = "An error occurred while creating a calendar.";
                throw new MySQLException(errorMessage, e);
            }
        }

        @Override
        public Optional<MeetingCalendar> findById(int id) {
            String selectQuery = "SELECT * FROM meeting_calendars WHERE id = ?";

            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String title = resultSet.getString("title");
                        return Optional.of(new MeetingCalendar(id, username, title));
                    }
                }

            } catch (SQLException e) {
                String errorMessage = "Error occurred while finding MeetingCalendar by ID: " + id;
                throw new MySQLException(errorMessage, e);
            }

            return Optional.empty();
        }

        @Override
        public Optional<MeetingCalendar> findByUsername(String username) {
            String selectQuery = "SELECT * FROM meeting_calendars WHERE username = ?";
            MeetingCalendar calendars = null;

            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)
            ) {

                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String title = resultSet.getString("title");
                        calendars = new MeetingCalendar(id, username, title);
                    }
                }

            } catch (SQLException e) {
                String errorMessage = "Error occurred while finding MeetingCalendars by username: " + username;
                throw new MySQLException(errorMessage, e);
            }
            return Optional.ofNullable(calendars);
        }

        @Override
        public Optional<MeetingCalendar> findByTitle(String title) {
            String selectQuery = "SELECT * FROM meeting_calendars WHERE title = ?";
            MeetingCalendar calendar = null;

            try (
                    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

                preparedStatement.setString(1, title);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        calendar = new MeetingCalendar(id, username, title);
                    }
                }

            } catch (SQLException e) {
                String errorMessage = ("Error occurred while finding MeetingCalendar by title: " + title);
                throw new MySQLException(errorMessage, e);
            }

            return Optional.ofNullable(calendar);
        }

    @Override
    public boolean deleteCalendar(int id) {
        String deleteQuery = "DELETE FROM meeting_calendars WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0; // Return true if one or more rows were deleted
        } catch (SQLException e) {
            String errorMessage = "Error occurred while deleting MeetingCalendar with ID: " + id;
            throw new MySQLException(errorMessage, e);
        }
    }


}
