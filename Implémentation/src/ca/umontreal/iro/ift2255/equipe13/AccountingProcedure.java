package ca.umontreal.iro.ift2255.equipe13;

import java.time.LocalDate;
import java.util.*;

/**
 * Generates accounting reports, payment notices and invoices.
 */
public class AccountingProcedure {

    private Map<Professional, Integer> paymentPerProfessional;
    private Map<Professional, Integer> nbOfferedServicesPerProfessional;
    private Map<Professional, List<Session>> sessionsOfferedPerProfessional;
    private Map<Member, List<Session>> sessionsReceivedPerMember;
    private int totalProfessionalsWhoOfferedServices;
    private int totalServices;
    private int totalPayment;

    /**
     * Performs all necessary calculations to generate reports.
     *
     * Must be called <b>before</b> any other method.
     *
     * @param from Interval beginning date
     * @param to Interval end date
     * @param servicesPerProfessional Objct containing, for each professional, the list of all services they offer
     */
    public void computePayments(LocalDate from, LocalDate to,
                                Map<Professional, List<Service>> servicesPerProfessional) {
        Set<Professional> professionals = servicesPerProfessional.keySet();

        paymentPerProfessional = new HashMap<>(professionals.size());
        nbOfferedServicesPerProfessional = new HashMap<>(professionals.size());
        sessionsOfferedPerProfessional = new HashMap<>(professionals.size());
        sessionsReceivedPerMember = new TreeMap<>(Comparator.comparingInt(Object::hashCode));
        totalProfessionalsWhoOfferedServices = 0;
        totalServices = 0;
        totalPayment = 0;

        for (Professional professional : professionals) {
            int nbOfferedServices = 0;
            int payment = 0;
            List<Session> sessionsOffered = new ArrayList<>();

            for (Service service : servicesPerProfessional.get(professional)) {
                List<Session> sessionsBetweenDates = service.getSessionsBetweenDates(from, to);
                sessionsOffered.addAll(sessionsBetweenDates);

                for (Session session : sessionsBetweenDates) {
                    int nbConfirmedRegistrations = 0;

                    for (Registration registration : session.getRegistrations()) {
                        if (registration.isConfirmed()) {
                            nbConfirmedRegistrations++;
                            Member participant = registration.getMember();
                            if (!sessionsReceivedPerMember.containsKey(participant)) {
                                List<Session> newList = new ArrayList<>();
                                newList.add(session);
                                sessionsReceivedPerMember.put(participant, newList);
                            }  else {
                                sessionsReceivedPerMember.get(participant).add(session);
                            }
                        }
                    }

                    payment += service.getCost() * nbConfirmedRegistrations;
                    nbOfferedServices += nbConfirmedRegistrations;
                }
            }

            paymentPerProfessional.put(professional, payment);
            nbOfferedServicesPerProfessional.put(professional, nbOfferedServices);
            sessionsOfferedPerProfessional.put(professional, sessionsOffered);
            totalServices += nbOfferedServices;
            totalPayment += payment;

            if (nbOfferedServices > 0)
                totalProfessionalsWhoOfferedServices++;
        }
    }

    /**
     * Generates EFT data.
     * @return EFT data
     */
    public String generateTefData() {
        StringBuilder out = new StringBuilder();

        for (Professional professional : paymentPerProfessional.keySet()) {
            out.append(professional.getName())
                    .append(" (").append(professional.getUserID()).append(") :\n- Nombre de services offerts : ")
                    .append(nbOfferedServicesPerProfessional.get(professional)).append("\n- Frais totaux : ")
                    .append(paymentPerProfessional.get(professional)).append("$\n");
        }

        return out.toString();
    }

    /**
     * Generates the accounting report.
     * @return Accounting report
     */
    public String generateReport() {
        StringBuilder out = new StringBuilder();

        for (Professional professional : paymentPerProfessional.keySet()) {
            int nbServices = nbOfferedServicesPerProfessional.get(professional);

            if (nbServices > 0) {
                out.append(professional.getName())
                        .append(" (").append(professional.getUserID()).append(") : ")
                        .append(nbServices)
                        .append(" services offerts; frais de ")
                        .append(paymentPerProfessional.get(professional)).append("$\n");
            }
        }

        out.append("NB. PROFESSIONNELS AYANT OFFERT DES SERVICES : ")
                .append(totalProfessionalsWhoOfferedServices)
                .append("\nNB. TOTAL DE SERVICES : ")
                .append(totalServices)
                .append("\nFRAIS TOTAUX : ")
                .append(totalPayment).append("$\n");

        return out.toString();
    }

    /**
     * Generates a member's invoice.
     * @param member Member for whom to generate an invoice
     * @return The member's invoice
     */
    public String generateMemberReport(Member member) {
        StringBuilder out = new StringBuilder();

        out.append(member.getName())
                .append(" (").append(member.getUserID()).append(")").append("\n")
                .append(member.getAddress()).append(", ")
                .append(member.getCity()).append(", ")
                .append(member.getProvince()).append(", ")
                .append(member.getPostalCode()).append("\nServices reçus :\n");

        if (sessionsReceivedPerMember.containsKey(member)) {
            for (Session session : sessionsReceivedPerMember.get(member)) {
                Service service = session.getService();
                out.append(service.getName()).append(" par ")
                        .append(service.getOfferedBy().getName()).append(", ")
                        .append(DateOperations.dayFormat(session.getSessionDate()))
                        .append("\n");
            }
        } else {
            out.append("Aucun\n");
        }

        return out.toString();
    }

    /**
     * Generates a professional's payment notice.
     * @param professional Professional for whom to generate a payment notice
     * @return The professional's payment notice.
     */
    public String generateProfessionalReport(Professional professional) {
        StringBuilder out = new StringBuilder();

        out.append(professional.getName())
           .append(" (").append(professional.getUserID()).append(")").append("\n")
           .append(professional.getAddress()).append(", ")
           .append(professional.getCity()).append(", ")
           .append(professional.getProvince()).append(", ")
           .append(professional.getPostalCode()).append("\nServices rendus :\n");

        if (sessionsOfferedPerProfessional.containsKey(professional)) {
            for (Session session : sessionsOfferedPerProfessional.get(professional)) {
                Service service = session.getService();
                Set<Registration> registrations = session.getRegistrations();

                out.append(service.getName())
                        .append(", séance n°").append(session.getSessionID()).append(", ")
                        .append(DateOperations.dayFormat(session.getSessionDate())).append("\n");

                for (Registration registration : registrations) {
                    if (registration.isConfirmed()) {
                        Member participant = registration.getMember();
                        out.append("- ").append(participant.getName())
                                .append(" (").append(participant.getUserID()).append("), ")
                                .append(service.getCost()).append("$, confirmé le ")
                                .append(DateOperations.dayFormat(registration.getConfirmationDate().toLocalDate()))
                                .append(" à ")
                                .append(DateOperations.timeFormat(registration.getConfirmationDate().toLocalTime()))
                                .append("\n");
                    }
                }
            }

            out.append("NOMBRE DE SERVICES RENDUS : ").append(nbOfferedServicesPerProfessional.get(professional));
            out.append("\nPAIEMENT TOTAL : ").append(paymentPerProfessional.get(professional)).append("$\n");
        } else {
            out.append("Aucun\n");
        }

        return out.toString();
    }
}
