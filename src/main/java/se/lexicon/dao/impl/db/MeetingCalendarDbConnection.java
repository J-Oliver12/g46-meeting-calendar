package se.lexicon.dao.impl.db;

import se.lexicon.exception.DBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MeetingCalendarDbConnection {

    private static final String DB_NAME = "meeting_calendar_db";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "g46";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
        } catch (SQLException e) {
            throw new DBConnectionException("Failed to connect to DB", e);
            //throw new DBConnectionException("Failed to connect to DB (" + DB_NAME + ")", e);
        }
    }


}
