package ca.umontreal.iro.ift2255.equipe13;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A registration of a member to a session. Also contains methods to confirm his presence to the session.
 */
public class Registration {
	
	private LocalDateTime registrationDate;
	private Member member;
	private Session session;
	private String comments;
	private String confirmationComments;
	private LocalDateTime confirmationDate = null;

	public Registration(Member member, Session session, String comments) {
		this.member = member;
		this.session=session;
		this.comments=comments;
		this.registrationDate = LocalDateTime.now();
	}

	public Member getMember() {
		return member;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * Set the confirmation date (to the current date and time), and the confirmation comments.
	 * @param confirmationComments comments, maximum 100 characters.
	 * @return True if the confirmation has been successfully done, or false if not.
	 */
	public boolean setConfirmation(String confirmationComments) {
		if (confirmationComments.length()<=100) {
			this.confirmationDate = LocalDateTime.now();
			this.confirmationComments = confirmationComments;
			return true;
		}
		return false;
	}

	public boolean isConfirmed() {
		return confirmationDate != null;
	}

	public LocalDateTime getConfirmationDate() {
		return confirmationDate;
	}

	public Session getService() {
		return session;
	}

	public String getComments() {
		return comments;
	}
}
