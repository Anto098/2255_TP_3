package ca.umontreal.iro.ift2255.equipe13;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UserManager : class used to contain Members and Professionals. Contains operations that are
 * related to Members and Professionals.
 */
public class UserManager {
	
	private final List<Member> members;
	private final List<Professional> professionals;

    /**
     * UserManager : creates an ArrayList of Member and an ArrayList of Professional
     */
	public UserManager() {
		this.members = new ArrayList<>();
		this.professionals = new ArrayList<>();
	}
    /**
     * createMember : tries to create a member, if successful, adds it to the List<Member> contained in the UserManager
     * @param name : Member's name
     * @param email : Member's email
     * @param address : Member's address
     * @param city : Member's city
     * @param province : Member's province
     * @param postalCode : Member's postal code
     * @param gender : Member's gender
     * @param birthDate : Member's birthDate
     * @param phoneNumber : Member's phoneNumber
     * @return : a boolean, to indicate whether the member's creation was successful or not
     */
	public boolean createMember(String name,
								String email,
								String address,
								String city,
								String province,
								String postalCode,
								User.Gender gender,
								String birthDate,
								String phoneNumber){
		Member member = new Member(gender, phoneNumber);
		if(member.setName(name)
				&& member.setEmail(email)
				&& member.setBirthDate(birthDate)
				&& member.setAddress(address)
				&& member.setCity(city)
				&& member.setProvince(province)
				&& member.setPostalCode(postalCode)){
			members.add(member);
			return true;
		}
		else {
			return false;
		}
	}

    /**
     * createProfessional : tries to create a professional, if successful, adds it to the List<Professional> in the UserManager
     * @param name : Professional's name
     * @param email : Professional's email
     * @param address : Professional's address
     * @param city : Professional's city
     * @param province : Professional's province
     * @param postalCode : Professional's postal code
     * @param gender : Professional's gender
     * @param birthDate : Professional's birthDate
     * @param phoneNumber : Professional's phoneNumber
     * @return : a boolean, to indicate whether the Professional's creation was successful or not
     */
	public boolean createProfessional(String name,
									  String email,
									  String address,
									  String city,
									  String province,
									  String postalCode,
									  User.Gender gender,
									  String birthDate,
									  String phoneNumber){
		Professional professional = new Professional(gender,phoneNumber);
		if(professional.setName(name)
				&& professional.setEmail(email)
				&& professional.setBirthDate(birthDate)
				&& professional.setAddress(address)
				&& professional.setCity(city)
				&& professional.setProvince(province)
				&& professional.setPostalCode(postalCode)){
			professionals.add(professional);
			return true;
		}
		else{
			return false;
		}
	}
    /**
     *  fetchUser with userID : searches a user by his userID
     * @param userID : user's unique ID
     * @param searchForMember : boolean that indicates whether we're looking for a Member or a Professional
     * @return : the User found, null if no user matches this ID
     */
	public User fetchUser(int userID, boolean searchForMember){
		List<User> userList = (List<User>)(searchForMember ? members : professionals);
		for(User user : userList){
			if(user.getUserID() == userID){
				return user;
			}
		}
		return null;
	}
    /**
     *  fetchUser with email, searches a user by his email
     * @param email : User's email
     * @param searchForMember : boolean that indicates whether we're looking for a Member or a Professional
     * @return : the User found, null if no user matches this ID
     */
	public User fetchUser(String email, boolean searchForMember){
		List<User> userList = (List<User>)(searchForMember ? members : professionals);
		for(User user : userList){
			if(user.getEmail().equals(email)){
				return user;
			}
		}
		return null;
	}
    /**
     * Removes a Member from the List<Member>
     * @param userID : the Member's unique ID
     */
	public void deleteMember(int userID){
		for(Member member : members){
			if (member.getUserID() == userID) {
				members.remove(member);
				break;
			}
		}
	}
    /**
     * Removes a Professional from the List<Professional>
     * @param userID : the Professional's unique ID
     */
	public void deleteProfessional(int userID){
		for(Professional professional : professionals){
			if(professional.getUserID() == userID){
				professionals.remove(professional);
				break;
			}
		}
	}
	public List<Member> getMembers(){
		return members;
	}
	public List<Professional> getProfessionals(){
		return professionals;
	}
}
