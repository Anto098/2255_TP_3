package ca.umontreal.iro.ift2255.equipe13.test;
import ca.umontreal.iro.ift2255.equipe13.Member;
import ca.umontreal.iro.ift2255.equipe13.Professional;
import ca.umontreal.iro.ift2255.equipe13.User;
import ca.umontreal.iro.ift2255.equipe13.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    private UserManager userManager;
    @BeforeEach
    void setUp(){
        userManager = new UserManager();
    }

    @Test
    void createProfessional() {
        // test d'un cas "normal", tous les champs respectent les conditions spécifiées dans User
        assertTrue(userManager.createProfessional("John Doe", "john.doe@gmail.com", "333 rue ghi",
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        // Test de la longueur du nom
        assertTrue(userManager.createProfessional("0123456789012345678901234", /* 25 characters */
                "john.doe01@gmail.com", "333 rue ghi",
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        assertFalse(userManager.createProfessional("Uvuvwevwevwe Onyetenyevwe Ugwemubwem Ossas", /* over 25 characters */
                "john.doe1@gmail.com", "333 rue ghi",
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        // Test de la longueur de l'adresse
        assertTrue(userManager.createProfessional("John Doe", "john.doe02@gmail.com",
                "0123456789012345678901234", /* 25 characters */
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        assertFalse(userManager.createProfessional("John Doe", "john.doe2@gmail.com",
                "1 rue des Intergouvernementalisations", /* over 25 characters */
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        // Test de la longueur du nom de la ville
        assertTrue(userManager.createProfessional("John Doe", "john.doe03@gmail.com", "333 rue ghi",
                "01234567890123", /* 14 characters */ "BC", "POCODE",
                User.Gender.MALE, "10-01-1010",
                "1"));
        assertFalse(userManager.createProfessional("John Doe", "john.doe3@gmail.com", "333 rue ghi",
                "Saint-Remy-en-Bouzemont-Saint-Genest-et-Isson", /* over 14 characters */ "BC",
                "POCODE", User.Gender.MALE, "10-01-1010", "1"));
        // Test de la longueur du nom de la province
        assertFalse(userManager.createProfessional("John Doe", "john.doe4@gmail.com", "333 rue ghi",
                "Vancouver", "CBD", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        // Test de la taille du code postal
        assertFalse(userManager.createProfessional("John Doe", "john.doe5@gmail.com", "333 rue ghi",
                "Vancouver", "BC", "POCOD", User.Gender.MALE, "10-01-1010",
                "1"));
        // Test du formatage de la date de naissance
        assertFalse(userManager.createProfessional("John Doe", "john.doe6@gmail.com", "333 rue ghi",
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10011010",
                "1"));
        // Test du formatage de l'adresse mail
        assertFalse(userManager.createProfessional("John Doe", "john.doe7gmail.com", "333 rue ghi",
                "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010",
                "1"));
        System.out.println("createProfessional functions properly.");
    }

    @Test
    void fetchUser() {
        Member member = new Member(User.Gender.MALE, "5141234567");
        member.setName("John Doe");
        member.setEmail("john.doe@gmail.com");
        member.setBirthDate("10-01-1010");
        member.setAddress("333 rue ghi");
        member.setCity("Vancouver");
        member.setProvince("BC");
        member.setPostalCode("POCODE");
        userManager.getMembers().add(member);

        Professional professional = new Professional(User.Gender.FEMALE,"5147654321");
        professional.setName("Jeanne Doe");
        professional.setEmail("jeanne.doe@gmail.com");
        professional.setBirthDate("02-02-0202");
        professional.setAddress("1 rue Laurier");
        professional.setCity("Montréal");
        professional.setProvince("QC");
        professional.setPostalCode("ABCDEF");
        userManager.getProfessionals().add(professional);

        User johnDoe = userManager.fetchUser("john.doe@gmail.com",true);
        assertEquals(johnDoe.getGender(), member.getGender());
        assertEquals(johnDoe.getEmail(), member.getEmail());
        assertEquals(johnDoe.getAddress(), member.getAddress());
        assertEquals(johnDoe.getCity(), member.getCity());
        assertEquals(johnDoe.getProvince(), member.getProvince());
        assertEquals(johnDoe.getPostalCode(), member.getPostalCode());
        assertEquals(johnDoe.getBirthDate(), member.getBirthDate());
        assertEquals(johnDoe.getPhoneNumber(), member.getPhoneNumber());

        User jeanneDoe = userManager.fetchUser("jeanne.doe@gmail.com", false);
        assertEquals(jeanneDoe.getGender(), professional.getGender());
        assertEquals(jeanneDoe.getEmail(), professional.getEmail());
        assertEquals(jeanneDoe.getAddress(), professional.getAddress());
        assertEquals(jeanneDoe.getCity(), professional.getCity());
        assertEquals(jeanneDoe.getProvince(), professional.getProvince());
        assertEquals(jeanneDoe.getPostalCode(), professional.getPostalCode());
        assertEquals(jeanneDoe.getBirthDate(), professional.getBirthDate());
        assertEquals(jeanneDoe.getPhoneNumber(), professional.getPhoneNumber());

        System.out.println("fetchUser functions properly.");
    }
}