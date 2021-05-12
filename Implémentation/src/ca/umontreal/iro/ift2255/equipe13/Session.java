package ca.umontreal.iro.ift2255.equipe13;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Session is an occurrence of a Service at a specified date. Contains the sessionDate, sessionID and a set of member registrations.
 */
public class Session {
    private final Service service;
    private final int sessionID;
    private final LocalDate sessionDate;
    private boolean isCancelled;
    private final Set<Registration> registrations;

    Session(Service service, int sessionID, LocalDate sessionDate) {
        this.service = service;
        this.sessionID = sessionID;
        this.sessionDate = sessionDate;
        this.isCancelled = false;
        this.registrations = new HashSet<>();
    }

    public int getSessionID() {
        return sessionID;
    }

    public Service getService() {
        return service;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public boolean getIsCancelled() {
        return isCancelled;
    }


    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    /**
     * Checks if a member is registered to this session.
     * @param member a member.
     * @return True if the member is registered, or false if not.
     */
    public boolean isMemberSubscribed(Member member) {
        return registrations.stream().anyMatch(r -> r.getMember() == member);
    }

    /**
     * Unregisters a member from this session (if he is actually registered).
     * @param member a member.
     */
    public void unsubscribeMember(Member member) {
        registrations.removeIf(r -> r.getMember() == member);
    }
}
