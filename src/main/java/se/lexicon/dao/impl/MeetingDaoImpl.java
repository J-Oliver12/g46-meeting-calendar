package se.lexicon.dao.impl;

import se.lexicon.dao.MeetingDao;
import se.lexicon.model.Meeting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MeetingDaoImpl implements MeetingDao {

    private Connection connection;

    public MeetingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Meeting createMeeting(Meeting meeting) {
        String insertQuery = "INSERT INTO MEETINGS(TITLE, START_TIME, END_TIME, _DESCRIPTION, CALENDAR_ID) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, meeting.getTitle());
            preparedStatement.setTimestamp(2, meeting.getStartTime());
            preparedStatement.setTimestamp(3, meeting.getEndTime());
            preparedStatement.setString(4, meeting.getDescription());
            preparedStatement.setInt(5, meeting.getCalendarId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating meeting failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int meetingId = generatedKeys.getInt(1);
                    meeting.setId(meetingId);
                } else {
                    throw new SQLException("Creating meeting failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while creating a meeting.", e);
        }

        return meeting;
    }

    @Override
    public Optional<Meeting> findById(int meetingId) {
        String selectQuery = "SELECT * FROM MEETINGS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, meetingId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Meeting meeting = extractMeetingFromResultSet(resultSet);
                    return Optional.of(meeting);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding a meeting by ID.", e);
        }

        return Optional.empty();
    }

    @Override
    public Collection<Meeting> findAllMeetingsByCalendarId(int calendarId) {
        String selectQuery = "SELECT * FROM MEETINGS WHERE CALENDAR_ID = ?";
        Collection<Meeting> meetings = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, calendarId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Meeting meeting = extractMeetingFromResultSet(resultSet);
                    meetings.add(meeting);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while finding meetings by calendar ID.", e);
        }

        return meetings;
    }

    @Override
    public boolean deleteMeeting(int meetingId) {
        String deleteQuery = "DELETE FROM MEETINGS WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, meetingId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting a meeting.", e);
        }
    }

    private Meeting extractMeetingFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String title = resultSet.getString("TITLE");
        java.sql.Timestamp startTime = resultSet.getTimestamp("START_TIME");
        java.sql.Timestamp endTime = resultSet.getTimestamp("END_TIME");
        String description = resultSet.getString("_DESCRIPTION");
        int calendarId = resultSet.getInt("CALENDAR_ID");

        return new Meeting(id, title, startTime, endTime, description, calendarId);
    }

}
