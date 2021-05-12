package ca.umontreal.iro.ift2255.equipe13;

import java.util.Date;

/**
 * Member : subclass of User. Represents a Member of Gym, has all the attributes of a User.
 */
public class Member extends User {
	
	private boolean validity = false;

	public boolean getValidity(){
		return validity;
	}
	public void setValidity(boolean validity){
		this.validity = validity;
	}

	/**
	 * Member : creates a Member with his gender and phonenumber, sets his validity and registration date
	 * @param gender : The member's Gender (from User.Gender)
	 * @param phoneNumber : The member's phone number
	 */
	public Member (Gender gender, String phoneNumber){
		super(true);
		this.setGender(gender);
		this.setPhoneNumber(phoneNumber);
		this.setValidity(true);
		this.setRegistrationDate();
	}
}
