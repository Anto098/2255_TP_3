package ca.umontreal.iro.ift2255.equipe13.test;

import ca.umontreal.iro.ift2255.equipe13.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountingProcedureTest {

    private AccountingProcedure accountingProcedure;
    private UserManager userManager;
    private ServiceManager serviceManager;

    private Professional testProfessional;
    private Member testMember;

    void createOneService() {
        serviceManager.createService(testProfessional,
                "Cours de yoga",
                "01-12-2020",
                "30-06-2021",
                "23:59",
                15,
                30,
                "des commentaires",
                new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});

        Service service = serviceManager.getSessionsForToday().get(0).getService();

        service.makeRegistrationForToday(testMember, "");
        service.makeConfirmation(testMember, "");
    }

    void createTwoServices() {
        serviceManager.createService(testProfessional,
                "Cours de yoga",
                "01-12-2020",
                "30-06-2021",
                "23:59",
                15,
                30,
                "des commentaires",
                new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
        serviceManager.createService(testProfessional,
                "Cours de ping-pong",
                "01-12-2020",
                "30-06-2021",
                "23:59",
                15,
                50,
                "des commentaires",
                new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});

        for (Session session : serviceManager.getSessionsForToday()) {
            Service service = session.getService();
            service.makeRegistrationForToday(testMember, "");
            service.makeConfirmation(testMember, "");
        }
    }

    void computePayments() {
        accountingProcedure.computePayments(
                DateOperations.dayParse("01-12-2020"),
                DateOperations.dayParse("30-06-2021"),
                serviceManager.getServicesPerProfessional());
    }

    @BeforeEach
    void setUp() {
        accountingProcedure = new AccountingProcedure();
        userManager = new UserManager();
        serviceManager = new ServiceManager();

        userManager.createProfessional("Jean Tremblay", "jean.tremblay@gmail.com", "111 rue abc", "montreal", "QC", "ABCDEF", User.Gender.MALE, "11-11-1111", "12");
        userManager.createMember("John Doe", "john.doe@gmail.com", "333 rue ghi", "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010", "1");

        testProfessional = (Professional) userManager.fetchUser("jean.tremblay@gmail.com", false);
        testMember = (Member) userManager.fetchUser("john.doe@gmail.com", true);
    }

    @Test
    void testGenerateTefData() {
        createOneService();
        computePayments();

        String[] tefData = accountingProcedure.generateTefData().split("\n");

        // Nom et ID affichés
        assertTrue(tefData[0].matches("^Jean Tremblay \\(\\d{9}\\).*"));
        // Nombre de services
        assertTrue(tefData[1].matches(".+ : 1$"));
        // Prix
        assertTrue(tefData[2].matches(".+ : 30\\$$"));
    }

    @Test
    void testGenerateTefDataWithTwoServices() {
        createTwoServices();
        computePayments();

        String[] tefData = accountingProcedure.generateTefData().split("\n");

        // Nom et ID affichés
        assertTrue(tefData[0].matches("^Jean Tremblay \\(\\d{9}\\).*"));
        // Nombre de services
        assertTrue(tefData[1].matches(".+ : 2$"));
        // Prix
        assertTrue(tefData[2].matches(".+ : 80\\$$"));
    }

    @Test
    void testGenerateTefDataWithoutServices() {
        computePayments();
        assertEquals("", accountingProcedure.generateTefData());
    }

    @Test
    void generateReport() {
        createOneService();
        computePayments();
        String[] report = accountingProcedure.generateReport().split("\n");

        // Nom, ID,  nb. services, frais
        assertTrue(report[0].matches("^Jean Tremblay \\(\\d{9}\\) : 1 .* 30\\$$"));
        // Nb. professionnels ayant offert des services
        assertTrue(report[1].matches(".+ : 1$"));
        // Nb. total de services
        assertTrue(report[2].matches(".+ : 1$"));
        // Frais totaux
        assertTrue(report[3].matches(".+ : 30\\$$"));
    }

    @Test
    void generateReportWithTwoServices() {
        createTwoServices();
        computePayments();
        String[] report = accountingProcedure.generateReport().split("\n");

        // Nom, ID,  nb. services, frais
        assertTrue(report[0].matches("^Jean Tremblay \\(\\d{9}\\) : 2 .* 80\\$$"));
        // Nb. professionnels ayant offert des services
        assertTrue(report[1].matches(".+ : 1$"));
        // Nb. total de services
        assertTrue(report[2].matches(".+ : 2$"));
        // Frais totaux
        assertTrue(report[3].matches(".+ : 80\\$$"));
    }

    @Test
    void generateReportWithoutServices() {
        computePayments();
        String[] report = accountingProcedure.generateReport().split("\n");

        // Nb. professionnels ayant offert des services
        assertTrue(report[0].matches(".+ : 0$"));
        // Nb. total de services
        assertTrue(report[1].matches(".+ : 0$"));
        // Frais totaux
        assertTrue(report[2].matches(".+ : 0\\$$"));
    }
}