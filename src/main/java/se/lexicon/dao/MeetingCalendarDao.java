package se.lexicon.dao;

import se.lexicon.model.Meeting;
import se.lexicon.model.MeetingCalendar;

import java.util.Collection;
import java.util.Optional;

public interface MeetingCalendarDao {

    MeetingCalendar createMeetingCalendar(String title, String username);

    Optional<MeetingCalendar> findById(int Id);

    Collection<MeetingCalendar> findByUsername(String username);

    Optional<MeetingCalendar> findByTitle(String title);

    boolean deleteCalendar(int id);

}
