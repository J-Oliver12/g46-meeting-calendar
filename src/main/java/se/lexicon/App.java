package se.lexicon;

import se.lexicon.dao.MeetingDao;
import se.lexicon.dao.UserDao;
import se.lexicon.dao.impl.MeetingDaoImpl;
import se.lexicon.dao.impl.UserDaoImpl;

import se.lexicon.dao.impl.db.MeetingCalendarDbConnection;
import se.lexicon.exception.DBConnectionException;
import se.lexicon.model.Meeting;
import se.lexicon.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public class App 
{
    public static void main( String[] args )
    {
        /*try {

            Connection connection = MeetingCalendarDbConnection.getConnection();


            UserDao userDao = new UserDaoImpl(connection);


            User newUser = userDao.createUser("admin");


            System.out.println("User created successfully:");
            System.out.println("Username: " + newUser.getUsername());
            System.out.println("Password: " + newUser.getPassword());
            System.out.println("Expired: " + newUser.isExpired());


            connection.close();
        } catch (DBConnectionException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }*/
    }

}
