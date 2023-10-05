package se.lexicon.controller;

import se.lexicon.dao.MeetingCalendarDao;
import se.lexicon.dao.MeetingDao;
import se.lexicon.dao.UserDao;
import se.lexicon.exception.CalendarExceptionHandler;
import se.lexicon.model.Meeting;
import se.lexicon.model.MeetingCalendar;
import se.lexicon.model.User;
import se.lexicon.view.CalendarView;

import java.util.Optional;

public class CalendarController {

    // dependencies
    private CalendarView view;
    private UserDao userDao;
    private MeetingCalendarDao calendarDao;
    private MeetingDao meetingDao;

    // fields
    private boolean isLoggedIn;
    private String username;

    public CalendarController(CalendarView view, UserDao userDao, MeetingCalendarDao calendarDao, MeetingDao meetingDao) {
        this.view = view;
        this.userDao = userDao;
        this.calendarDao = calendarDao;
        this.meetingDao = meetingDao;
    }

    public void run() {
        while (true) {
            view.displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 0:
                    register();
                    break;
                case 1:
                    login();
                    break;
                case 2:
                    createCalendar();
                    break;
                case 3:
                    createMeeting();
                    break;
                case 4:
                    deleteCalendar();
                    break;
                case 5:
                    displayCalendar();
                    break;
                case 6:
                    getHashedPassword();
                    break;
                case 7:
                    isLoggedIn = false;
                    break;
                case 8:
                    System.exit(0);
                    break;
                default:
                    view.displayWarningMessage("Invalid choice. Please select a valid option.");
            }

        }

    }


    private int getUserChoice() {
        String operationType = view.promoteString();
        int choice = -1;
        try {
            choice = Integer.parseInt(operationType);
        } catch (NumberFormatException e) {
            view.displayErrorMessage("Invalid input. Please enter a number.");
        }
        return choice;
    }


    private void register() {
        view.displayMessage("Enter you username:");
        String username = view.promoteString();
        User registeredUser = userDao.createUser(username);
        view.displayUser(registeredUser);
    }

    private void login() {
        User user = view.promoteUserForm();
        try {
            isLoggedIn = userDao.authenticate(user);
            username = user.getUsername();
            view.displaySuccessMessage("Login successful.");
        } catch (Exception e) {
            CalendarExceptionHandler.handleException(e);
        }

    }


    private void createCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }

        String calendarTitle = view.promoteCalendarForm();
        MeetingCalendar createdMeetingCalendar = calendarDao.createMeetingCalendar(calendarTitle, username);
        view.displaySuccessMessage("Calendar created successfully. ;)");
        view.displayCalendar(createdMeetingCalendar);

    }

    private void createMeeting() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }

        Meeting newMeeting = view.promoteMeetingForm();

        MeetingCalendar userCalendar = calendarDao.findByUsername(username).orElse(null);
        if (userCalendar != null) {
            newMeeting.setCalendar(userCalendar);
        } else {
            view.displayErrorMessage("User's calendar not found.");
            return;
        }

        Meeting createdMeeting = meetingDao.createMeeting(newMeeting);
        view.displaySuccessMessage("Meeting created successfully.");
        view.displayMeeting(createdMeeting);
    }


    private void deleteCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }


        MeetingCalendar userCalendar = calendarDao.findByUsername(username).orElse(null);
        if (userCalendar == null) {
            view.displayErrorMessage("User's calendar not found.");
            return;
        }

        // Confirm the deletion with the user
        view.displayMessage("Are you sure you want to delete your calendar? (yes/no): ");
        String confirmation = view.promoteString();

        if (confirmation.equalsIgnoreCase("yes")) {
            // Delete the calendar in the database
            if (calendarDao.deleteCalendar(userCalendar.getId())) {
                view.displaySuccessMessage("Calendar deleted successfully.");
            } else {
                view.displayErrorMessage("Failed to delete the calendar.");
            }
        } else {
            view.displayMessage("Calendar deletion canceled.");
        }
    }


    private void displayCalendar() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }

        // Retrieve the currently logged-in user's calendar
        MeetingCalendar userCalendar = calendarDao.findByUsername(username).orElse(null);
        if (userCalendar == null) {
            view.displayErrorMessage("User's calendar not found.");
            return;
        }

        view.displayCalendar(userCalendar);
    }

    private void getHashedPassword() {
        if (!isLoggedIn) {
            view.displayWarningMessage("You need to login first.");
            return;
        }

        // Get input from the user for the username
        view.displayMessage("Enter the username to get the hashed password: ");
        String usernameToGetPassword = view.promoteString();

        // Find the user by username
        Optional<User> optionalUser = userDao.findByUsername(usernameToGetPassword);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Get and display the hashed password
            String hashedPassword = user.getHashedPassword();
            view.displayMessage("Hashed Password for User " + usernameToGetPassword + ": " + hashedPassword);
        } else {
            view.displayErrorMessage("User not found.");
        }
    }

}

