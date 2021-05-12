package ca.umontreal.iro.ift2255.equipe13;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Professional : subclass of User. Represents a Professional of Gym, has all the attributes of a User.
 */
public class Professional extends User {

	/**
	 * Professional : creates a Professional with his gender and phone number, sets his registration date
	 * @param gender : the professional's gender
	 * @param phoneNumber : the professional's phone number
	 */
	public Professional(Gender gender, String phoneNumber){
		super(false);
		this.setGender(gender);
		this.setPhoneNumber(phoneNumber);
		this.setRegistrationDate();
	}

}
