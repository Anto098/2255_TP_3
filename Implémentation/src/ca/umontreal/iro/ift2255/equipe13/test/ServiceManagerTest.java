package ca.umontreal.iro.ift2255.equipe13.test;

import ca.umontreal.iro.ift2255.equipe13.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceManagerTest {
    private ServiceManager serviceManager;
    private Professional offeredBy;

    @BeforeEach
    public void setUp() {
        UserManager userManager = new UserManager();
        this.serviceManager = new ServiceManager();
        userManager.createProfessional("Jean Tremblay", "jean.tremblay@gmail.com", "111 rue abc", "montreal", "QC", "ABCDEF", User.Gender.MALE, "11-11-1111", "12");
        this.offeredBy = (Professional) userManager.fetchUser("jean.tremblay@gmail.com", false);
    }

    @Test
    public void testCreateValidService() {
        assertTrue(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021", "15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Service did not create correctly");
    }

    @Test public void testCreateInvalidService() {
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "12-1-2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid beginning date");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "12-2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid beginning date");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "12--2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid beginning date");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "12-0-2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid beginning date");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-1-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid end date");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid end date");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15--2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid end date");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-0-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid end date");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-02-2021", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with beginning date AFTER end date");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","25:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid service hour");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid service hour");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:0", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid service hour");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","-5:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid service hour");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 100, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid capacity (100)");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", -20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid capacity (-20)");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 20, 150, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid cost (150)");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 20, -20, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid cost (-20)");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 20, 30, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}),
                "Fail : Created service with invalid comments (length > 100)");

        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{}),
                "Fail : Created service with invalid recurrence (empty)");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"Y"}),
                "Fail : Created service with invalid recurrence (incorrect string)");
        assertFalse(serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021","15:00", 20, 30, "no comments", new String[]{"Y", "MONDAY"}),
                "Fail : Created service with invalid recurrence (correct and incorrect strings)");
    }

    @Test
    public void testGetProfessionalServicesEmpty() {
        assertTrue(serviceManager.getProfessionalServices(offeredBy).isEmpty(),
                "Is not null even if professional never created a service");

        serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021", "15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
        List<Service> services = serviceManager.getProfessionalServices(offeredBy);
        serviceManager.deleteService(services.get(0));

        assertTrue(serviceManager.getProfessionalServices(offeredBy).isEmpty(),
                "Is not empty when it should be");
    }

    @Test
    public void testGetProfessionalServicesOne() {
        serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021", "15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
        assertEquals(1,serviceManager.getProfessionalServices(offeredBy).size(),
                "Is not 1");
    }

    @Test
    public void testGetProfessionalServicesMultiple() {
        serviceManager.createService(offeredBy, "Cours de Tennis", "15-12-2020", "15-01-2021", "15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
        serviceManager.createService(offeredBy, "Cours de Football", "15-12-2020", "15-01-2021", "15:00", 20, 30, "no comments", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});

        assertEquals(2,serviceManager.getProfessionalServices(offeredBy).size(),
                "Is not 2");
    }
}