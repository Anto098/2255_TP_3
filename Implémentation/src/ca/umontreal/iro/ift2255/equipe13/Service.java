package ca.umontreal.iro.ift2255.equipe13;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * A service given by the GYM. Contains all necessary data (service ID, name, ...) and a list of sessions. Also contains all the necessary methods for the service or the creation/cancellation of sessions.
 */
public class Service {
	private final int serviceID;
	private String name;
	private final Professional offeredBy;
	private LocalDate beginningDate = null;
	private LocalDate endDate = null;
	private LocalTime serviceHour;	//HH:MM
	private int capacity;
	private int cost;
	private Set<DayOfWeek> recurrence;
	private String comments;
	private final ArrayList<Session> sessions;
	private int nextSessionID = 0;

	Service(int serviceID, String name, Professional offeredBy) {
		this.serviceID = serviceID;
		this.name = name;
		this.offeredBy = offeredBy;
		this.sessions = new ArrayList<>();
	}

	private void generateSessions(LocalDate from, LocalDate to) {		//Generate all the sessions between these 2 dates (excluding end date)
		long noOfDaysBetween = DAYS.between(from, to);
		for (int i=0; i<noOfDaysBetween; i++) {
			LocalDate sessionDate = from.plusDays(i);

			if (recurrence.contains(sessionDate.getDayOfWeek())) { 	// We only add sessions at days in the recurrence

				//We make sure to not create duplicates
				Session oldSessionSameDate = fetchDaySession(sessionDate);
				if(oldSessionSameDate!=null) {
					if (oldSessionSameDate.getIsCancelled()) {			//If the old session is cancelled, we remove it from the list (to add a fresh one)
						sessions.remove(oldSessionSameDate);
					} else {											//If the old session is not cancelled, then we just keep it
						continue;
					}
				}

				//We add the new session
				String stringSessionID = ""
						+ String.format("%03d", this.serviceID)
						+ String.format("%02d", this.nextSessionID++)
						+ String.format("%02d", this.offeredBy.getUserID()%100);

				int sessionID = Integer.parseInt(stringSessionID);
				sessions.add(new Session(this, sessionID, sessionDate));
			}
		}
	}

	private void cancelSessions(LocalDate from, LocalDate to) { 	// Cancels all the session on and between these 2 dates
		for (Session session : sessions) {
			LocalDate sDate = session.getSessionDate();
			if (sDate.isEqual(from) || sDate.isEqual(to) || (sDate.isAfter(from) && sDate.isBefore(to))) {
				session.setIsCancelled(true);
			}
		}
	}

	/**
	 * Sets the beginning date of this service instance. Checks if the given date is valid (correct format, is before EndDate...).
	 * @param beginningDate the String of a valid date in format "dd-mm-yyyy".
	 * @return True if the new beginning date has been successfully updated, or false if not.
	 */
	public boolean setBeginningDate(String beginningDate) {
		if(DateOperations.isCorrectFormat(beginningDate, DateOperations.getDayPattern())) {
			LocalDate newBegDate = DateOperations.dayParse(beginningDate);
			if(this.endDate!=null && newBegDate.isAfter(this.endDate)) {
				return false;
			}

			if (this.beginningDate!=null && this.endDate!=null) {
				if (newBegDate.isBefore(this.beginningDate)) {
					generateSessions(newBegDate, this.beginningDate.plusDays(1));

				} else if (newBegDate.isAfter(this.beginningDate)) {
					cancelSessions(this.beginningDate, newBegDate.minusDays(1));
				}
			} else if (this.endDate!=null) {		//Only called when creating a new service
				generateSessions(newBegDate, this.endDate);
			}

			this.beginningDate = newBegDate;
			return true;
		} else {
			return false;
		}
	}


	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date of this service instance. Checks if the given date is valid (correct format, is after BeginningDate...).
	 * @param endDate the String of a valid date in format "dd-mm-yyyy".
	 * @return True if the new end date has been successfully updated, or false if not.
	 */
	public boolean setEndDate(String endDate) {
		if(DateOperations.isCorrectFormat(endDate, DateOperations.getDayPattern())) {
			LocalDate newEndDate = DateOperations.dayParse(endDate);
			if(this.beginningDate!=null && newEndDate.isBefore(this.beginningDate)) {
				return false;
			}

			if (this.beginningDate!=null && this.endDate!=null) {
				if (newEndDate.isBefore(this.endDate)) {
					cancelSessions(newEndDate, this.endDate);
				} else if (newEndDate.isAfter(this.endDate)) {
					generateSessions(this.endDate, newEndDate);
				}
			} else if (this.beginningDate!=null) {		//Only called when creating a new service
				generateSessions(this.beginningDate, newEndDate);
			}

			this.endDate = newEndDate;
			return true;
		} else {
			return false;
		}
	}

	public LocalTime getServiceHour() {
		return serviceHour;
	}

	/**
	 * Sets the service hour of this service instance. Checks if the given time is valid (correct format).
	 * @param serviceHour the String of a valid time in format "hh-mm".
	 * @return True if the new service hour has been successfully updated, or false if not.
	 */
	public boolean setServiceHour(String serviceHour) {
		if(DateOperations.isCorrectFormat(serviceHour, DateOperations.getTimePattern())) {
			this.serviceHour = DateOperations.timeParse(serviceHour);
			return true;
		} else {
			return false;
		}
	}

	public int getServiceID() {
		return serviceID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Professional getOfferedBy() {
		return this.offeredBy;
	}

	public LocalDate getBeginningDate() {
		return beginningDate;
	}

	public int getCapacity() {
		return capacity;
	}

	/**
	 * Sets the capacity of this service instance. Checks if the given capacity is valid.
	 * @param capacity the capacity of the service, must be between 1 and 30.
	 * @return True if the new capacity has been successfully updated, or false if not.
	 */
	public boolean setCapacity(int capacity) {
		if (1 <= capacity && capacity <= 30) {
			this.capacity = capacity;
			return true;
		}
		return false;
	}

	public int getCost() {
		return cost;
	}

	/**
	 * Sets the cost of this service instance. Checks if the given cost is valid.
	 * @param cost the cost of the service, must be between 0 and 100.
	 * @return True if the new cost has been successfully updated, or false if not.
	 */
	public boolean setCost(int cost) {
		if (0 <= cost && cost <= 100) {
			this.cost=cost;
			return true;
		}
		return false;
	}

	public Set<DayOfWeek> getRecurrence() {
		return recurrence;
	}

	/**
	 * Sets the recurrence of this service instance. Checks if the given recurrence is valid (actual day(s) of week). Also deletes duplicates in the given recurrence String Array.
	 * @param recurrence an array of strings representing the day of the week : "MONDAY" ...
	 * @return True if the new recurrence has been successfully updated, or false if not.
	 */
	public boolean setRecurrence(String[] recurrence) {
		HashSet<DayOfWeek> recurrenceDays = new HashSet<>();
		for (String r : recurrence) {
			try {
				DayOfWeek dayOfWeek = DayOfWeek.valueOf(r.trim().toUpperCase());
				recurrenceDays.add(dayOfWeek);
			} catch (Exception e) {
				return false;
			}
		}

		if (recurrenceDays.size() > 0) {
			this.recurrence = recurrenceDays;
			return true;
		} else {
			return false;
		}
	}

	public String getComments() {
		return comments;
	}

	/**
	 * Sets the comments of this service instance. Checks if given comments are valid.
	 * @param comments comments, maximum 100 characters.
	 * @return True if the new comments have been successfully updated, or false if not.
	 */
	public boolean setComments(String comments) {
		if (comments.length() <= 100) {
			this.comments = comments;
			return true;
		}
		return false;
	}

	/**
	 * Fetch the session of this service occurring at the specified date. Returns null if there are no such session.
	 * @param day a LocalDate date.
	 * @return a session or null.
	 */
	public Session fetchDaySession(LocalDate day) {
		for (Session session : this.sessions) {
			if (session.getSessionDate().isEqual(day)) {
				return session;
			}
		}
		return null;
	}

	/**
	 * Registers a member to the session occurring later today (if it exists) of this service instance.
	 * @param member a member.
	 * @param comments comments, maximum 100 characters.
	 * @return True if the member has been successfully registered, or false if not.
	 */
	public boolean makeRegistrationForToday(Member member, String comments) {
		LocalDate today = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		DayOfWeek todayDayOfWeek = today.getDayOfWeek();

		Session todaysSession = fetchDaySession(today);

		if (member!=null && beginningDate.isBefore(today)
				&& endDate.isAfter(today)
				&& serviceHour.isAfter(currentTime)
				&& recurrence.contains(todayDayOfWeek)) {

			if (todaysSession != null && todaysSession.getRegistrations().size() < this.capacity && !todaysSession.getIsCancelled()) {		// If there is a session later today and it is not yet at max capacity or cancelled, we register the member
				Registration newRegistration = new Registration(member, todaysSession, comments);
				todaysSession.getRegistrations().add(newRegistration);
				return true;
			}
		}

		return false;
	}

	/**
	 * Confirms the registration of a member to the session occurring later today (if it exists) of this service instance.
	 * @param member a member.
	 * @param comments comments, maximum 100 characters.
	 * @return True if the member has been successfully registered, or false if not.
	 */
	public boolean makeConfirmation(Member member, String comments) {
		LocalDate today = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		DayOfWeek todayDayOfWeek = today.getDayOfWeek();

		Session todaysSession = fetchDaySession(today);

		if (member != null
				&& beginningDate.isBefore(today)
				&& endDate.isAfter(today)
				&& serviceHour.isAfter(currentTime)
				&& recurrence.contains(todayDayOfWeek)) {

			if (todaysSession != null && !todaysSession.getIsCancelled()) {        // If there is a session later today and it is not cancelled
				for (Registration registration : todaysSession.getRegistrations()) {
					if (registration.getMember().getUserID() == member.getUserID()) {	//If the member is registered we can confirm his presence
						registration.setConfirmation(comments);
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Cancels the session at the specified date (if it exists) of this service instance.
	 * @param canceledSessionDateString the String of a valid date in format "dd-mm-yyyy".
	 * @return True if the matching session has been cancelled, or false if not.
	 */
	public boolean cancelSession(String canceledSessionDateString) {
		if (DateOperations.isCorrectFormat(canceledSessionDateString, DateOperations.getDayPattern())) {
			LocalDate canceledSessionDate = DateOperations.dayParse(canceledSessionDateString);
			Session thatDaySession = fetchDaySession(canceledSessionDate);
			if (thatDaySession != null) {
				thatDaySession.setIsCancelled(true);
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the future sessions cancelled.
	 * @return a list of sessions.
	 */
	public List<Session> getCancelledSessionsToCome() {
		LocalDate today = LocalDate.now();
		List<Session> cancelledSessions = new ArrayList<>();
		for (Session session : sessions) {
			LocalDate sDate = session.getSessionDate();
			if (session.getIsCancelled() && (sDate.isEqual(today) || sDate.isAfter(today))) {
				cancelledSessions.add(session);
			}
		}
		return cancelledSessions;
	}

	/**
	 * Returns the sessions occurring between the 2 dates.
	 * @param from a LocalDate date.
	 * @param to a LocalDate date.
	 * @return a list of sessions.
	 */
	public List<Session> getSessionsBetweenDates(LocalDate from, LocalDate to) {
		List<Session> sessionsToReturn = new LinkedList<>();

		for (Session session : sessions) {
			if (!session.getIsCancelled()
					&& (session.getSessionDate().equals(from) || session.getSessionDate().equals(to)
						|| (session.getSessionDate().isAfter(from) && session.getSessionDate().isBefore(to)))) {
				sessionsToReturn.add(session);
			}
		}

		return sessionsToReturn;
	}

	/**
	 * Removes registrations of the member to sessions of this service instance.
	 * @param member a member.
	 */
	public void deleteAllSubscriptionsForMember(Member member) {
		for (Session session : sessions) {
			if (session.isMemberSubscribed(member)) {
				session.unsubscribeMember(member);
			}
		}
	}

}
