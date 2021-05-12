package ca.umontreal.iro.ift2255.equipe13;

import java.text.spi.DateFormatProvider;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * ServiceManager is a class that contains a list of services given at GYM, a hash map of professionals to their services, and methods to create, fetch, or delete services
 */
public class ServiceManager {
	private final List<Service> services;
	private final HashMap<Professional, List<Service>> servicesPerProfessional;
	private final Random rnd;

	public ServiceManager() {
		this.services = new ArrayList<>();
		this.servicesPerProfessional = new HashMap<>();
		this.rnd = new Random();
	}

	/**
	 * Tries to create a new service, and if successful, adds it to the services list.
	 * @param offeredBy a Professional.
	 * @param name a String in format "Jon Doe".
	 * @param beginningDate the String of a valid date in format "dd-mm-yyyy".
	 * @param endDate the String of a valid date in format "dd-mm-yyyy".
	 * @param serviceHour the String of a valid time in format "hh-mm".
	 * @param capacity the capacity of the service, must be between 1 and 30.
	 * @param cost the cost of the service, must be between 0 and 100.
	 * @param comments comments, maximum 100 characters.
	 * @param recurrence an array of strings representing the day of the week : "MONDAY" ...
	 * @return True if the service has been successfully created, or False if at least one of the parameters is invalid.
	 */
	public boolean createService(Professional offeredBy, String name, String beginningDate, String endDate, String serviceHour, int capacity, int cost, String comments, String[] recurrence) {

		int serviceID = 100 + rnd.nextInt(900);  			// Create a unique random service ID of 3 digits
		Service newService = new Service(serviceID, name, offeredBy);

		// Setters return true if the data is in a correct format, and false if not.
		if (newService.setRecurrence(recurrence)			//Make sure to set recurrence before setting beginning and end dates (needed to automatically generate sessions between the 2 dates)
			&& newService.setBeginningDate(beginningDate)
			&& newService.setEndDate(endDate)
			&& newService.setServiceHour(serviceHour)
			&& newService.setCapacity(capacity)
			&& newService.setCost(cost)
			&& newService.setComments(comments))
		{
			services.add(newService); 	// All data are valid, we can save the new service

			if(servicesPerProfessional.containsKey(offeredBy)) {		// Professional already have services
				servicesPerProfessional.get(offeredBy).add(newService);
			}else{														// It's the professional's first service
				List<Service> professionalServices = new ArrayList<>();
				professionalServices.add(newService);
				servicesPerProfessional.put(offeredBy, professionalServices);
			}

			return true; // Return confirmation of creation
		} else {
			return false; // At least one data is in incorrect format
		}
	}

	/**
	 *
	 * @return A copy of the HashMap containing the Professionals and a list of their services.
	 */
	public Map<Professional, List<Service>> getServicesPerProfessional() {
		return Collections.unmodifiableMap(servicesPerProfessional);
	}

	public void deleteService(Service service) {
		services.remove(service);
		servicesPerProfessional.get(service.getOfferedBy()).remove(service);
	}

	/**
	 * Delete all the services that a professional is giving (offeredBy).
	 * @param professional a professional.
	 */
	public void deleteAllServicesFromProfessional(Professional professional) {
		for (Service service : servicesPerProfessional.get(professional))
			services.remove(service);

		servicesPerProfessional.remove(professional);
	}

	/**
	 * Delete all the subscriptions to sessions of a member.
	 * @param member a member.
	 */
	public void deleteAllSubscriptionsForMember(Member member) {
		for (Service service : services) {
			service.deleteAllSubscriptionsForMember(member);
		}
	}

	/**
	 * Get a professional Services.
	 * @param professional a professional.
	 * @return the list of services that the professional is giving.
	 */
	public List<Service> getProfessionalServices(Professional professional) {
		if (servicesPerProfessional.get(professional)==null) {
			return new ArrayList<>(){};
		}
		return servicesPerProfessional.get(professional);
	}

	/**
	 * Get all the sessions of a professional that occur later today.
	 * OR if byProfessional=null, returns all the sessions of all the professionals that occur later today.
	 * @param byProfessional a professional, or null.
	 * @return a list of sessions.
	 */
	public List<Session> getSessionsForToday(Professional byProfessional) {

		LocalDate today = LocalDate.now();
		LocalTime time = LocalTime.now();
		DayOfWeek todayDayOfWeek = today.getDayOfWeek();

		ArrayList<Session> todaySessions = new ArrayList<>();

		for (Service service : this.services) {
			Session todaySession = service.fetchDaySession(today);

			if ((byProfessional == null || service.getOfferedBy() == byProfessional)
				&& service.getBeginningDate().isBefore(today)				// We find the services that should have a session today
				&& service.getEndDate().isAfter(today)
				&& service.getServiceHour().isAfter(time)
				&& service.getRecurrence().contains(todayDayOfWeek)
				&& todaySession!=null									// We make sure there is a session today and it is not cancelled
				&& !todaySession.getIsCancelled()) {

				todaySessions.add(todaySession);
			}
		}
		return todaySessions;
	}

	/**
	 * Get all the sessions that occur later today.
	 * @return a list of sessions.
	 */
	public List<Session> getSessionsForToday() {
		return getSessionsForToday(null);
	}

	/**
	 * Get the subscriptions of a member to sessions occurring later today.
	 * @param member a member.
	 * @return a list of sessions.
	 */
	public List<Session> getMemberSubscriptionsForToday(Member member) {
		List<Session> memberSessions = new ArrayList<>();

		for (Session session : getSessionsForToday(null)) {
			if (session.isMemberSubscribed(member))
				memberSessions.add(session);
		}

		return memberSessions;
	}
}
