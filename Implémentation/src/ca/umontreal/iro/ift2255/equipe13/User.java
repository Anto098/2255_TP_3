package ca.umontreal.iro.ift2255.equipe13;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
  * User : Class that contains all the information about a user and methods to get/set it.
 */
public abstract class User {
	/**
	 * Gender : Enum that contains all the possible genders a User could be.
	 */
	public enum Gender {
		MALE("M"),
		FEMALE("F"),
		UNSPECIFIED("X");

		static Gender findGender(String abbrv) {
			for (Gender gender : Gender.values()) {
				if (abbrv.equals(gender.abbrv))
					return gender;
			}
			return null;
		}

		String abbrv;

		Gender(String abbrv) {
			this.abbrv = abbrv;
		}

		@Override
		public String toString() {
			return abbrv;
		}
	}

	private static final Random rand = new Random();

	private final int userID;
	private String name;
	private String address;
	private String city;
	private String province;
	private String postalCode;
	private Gender gender;
	private LocalDate birthDate;
	private String phoneNumber;
	private String email;
	private LocalDate registrationDate;
	private String latestAccountingReport;

	/**
	 * User : Gives a user his userID. Member IDs are between 100m and 200m. Professional IDs are between 200m and 300m.
	 * @param isMember : boolean that indicates whether the User is a Member or Professional.
	 */
	protected User(boolean isMember) {
		int num = isMember? 100000000 : 200000000;
		this.userID = num + rand.nextInt(100000000);
		// [100m,200m[ : Members
		// [200m,300m[ : Professionals
	}
	public void setRegistrationDate(){
		this.registrationDate = LocalDate.now();
	}
	/**
	 * setName : sets the user's name if it's under 26 characters
	 * @param name : User's name
	 * @return : true if the name was set, false otherwise.
	 */
	public boolean setName(String name) {
		if(name.length() <= 25){
			this.name = name;
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * setAddress : sets the user's address if it's under 26 characters
	 * @param address : the user's address
	 * @return : true if the address was set correctly, false otherwise.
	 */
	public boolean setAddress(String address){
		if(address.length()<=25){
			this.address = address;
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * setCity : sets the user's city if it's 14 characters or less
	 * @param city : user's city
	 * @return : true if city's name is 14 characters or less, otherwise return false.
	 */
	public boolean setCity(String city){
		if(city.length()<=14){
			this.city = city;
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * setProvince : sets the user's province if it's 2 characters long.
	 * @param province : the user's province
	 * @return : true if the province is 2 characters long, false otherwise.
	 */
	public boolean setProvince(String province){
		if(province.length() == 2){
			this.province = province;
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * setPostalCode : sets the postal code if it's length is 6
	 * @param postalCode : the user's postal code
	 * @return : true if the user's postal code is of length 6, false otherwise.
	 */
	public boolean setPostalCode(String postalCode){
		if(postalCode.length() == 6){
			this.postalCode = postalCode;
			return true;
		}
		else{
			return false;
		}
	}
	public void setGender(Gender gender){
		this.gender = gender;
	}
	/**
	 * setBirthDate : sets the user's birth date if the format is correct.
	 * @param birthDate : user's birth date
	 * @return : true if the user's birth date has been set correctly ( was properly formatted), false otherwise.
	 */
	public boolean setBirthDate(String birthDate){
		if(DateOperations.isCorrectFormat(birthDate,DateOperations.getDayPattern())){
			this.birthDate = DateOperations.dayParse(birthDate);
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * setEmail : sets the user's email if the format is correct
	 * @param email : user's email
	 * @return : true if the email has been set ( was correctly formatted), false otherwise.
	 */
	public boolean setEmail(String email){
		if (Pattern.matches("[0-9a-zA-Z.!#$%&'*+\\-/=?^_`{|}~]+@[0-9a-zA-Z._-]+\\.[a-zA-Z]{2,4}", email)) {
			this.email = email;
			return true;
		}
		return false;
	}
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	public void setLatestAccountingReport(String latestAccountingReport){
		this.latestAccountingReport = latestAccountingReport;
	}
	public int getUserID(){
		return userID;
	}
	public String getName(){
		return name;
	}
	public String getAddress(){return address;}
	public String getCity(){return city;}
	public String getProvince(){return province;}
	public String getPostalCode(){return postalCode;}
	public Gender getGender(){
		return gender;
	}
	public LocalDate getBirthDate(){
		return birthDate;
	}
	public String getEmail(){
		return email;
	}
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public String getLatestAccountingReport(){
		return latestAccountingReport;
	}
	public LocalDate getRegistrationDate(){
		return registrationDate;
	}
}
