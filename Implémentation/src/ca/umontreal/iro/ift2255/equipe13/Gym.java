package ca.umontreal.iro.ift2255.equipe13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Shows menus and receives user input.
 */
public class Gym {

	/**
	 * User selectable menu options
	 */
	private enum ConsoleCommand {
		// Menu principal
		AGENT("1", "Se connecter en tant qu'agent"),
		PROFESSIONAL("2", "Se connecter en tant que professionnel"),
		MEMBER("3", "Se connecter en tant que membre"),
		ACCOUNTING_PROCEDURE("4", "Exécuter la procédure comptable hebdomadaire"),

		// Menu agent
		NEW_CLIENT("1", "Inscrire un nouveau client"),
		NEW_PROFESSIONAL("2", "Inscrire un nouveau professionnel"),
		ADD_SERVICE("3", "Ajouter un service"),
		MODIFY_SERVICE("4", "Modifier les détails d'un service"),
		MANAGE_CANCELLED_SESSIONS("5", "Gérer les séances annulées"),
		REMOVE_SERVICE("6", "Supprimer un service"),
		ACCOUNTING_REPORT("7", "Générer un rapport comptable provisoire"),

		// Menu membre
		SUBSCRIBE_TO_SERVICE("1", "S'inscrire à un service"),
		CANCEL_SUBSCRIBTION_TO_SERVICE("2", "Annuler l'inscription à un service"),
		SEE_MEMBER_INVOICE("3", "Voir la facture de la semaine précédente"),
		UPDATE_MEMBER_INFO("4", "Mettre à jour votre profil"),
		DELETE_MEMBER("5", "Résilier votre inscription"),

		// Menu professionnel
		CONFIRM_MEMBER_SUBSCRIPTION("1", "Vérifier l'inscription à une séance et confirmer la présence"),
		SEE_SUBSCRIPTIONS("2", "Voir les inscriptions à un service"),
		SEE_PROFESSIONAL_PAYMENT_NOTICE("3", "Voir l'avis de paiement pour la semaine précédente"),
		UPDATE_PROFESSIONAL_INFO("4", "Mettre à jour votre profil"),
		DELETE_PROFESSIONAL("5", "Supprimer votre compte"),

		// Gérer les séances annulées
		CANCEL_SESSION("c", "Annuler une autre séance"),
		REMOVE_CANCELLED_SESSION("o", "Ré-offrir une séance annulée"),

		// Commun
		RETURN("r", "Retour"),
		QUIT("x", "Quitter");

		private final String command;
		private final String label;

		ConsoleCommand(String command, String label) {
			this.command = command.toLowerCase();
			this.label = label;
		}
	}

	/**
	 * Structure containing data typed by a user describing a profile (member or professional).
	 */
	private static class UserProfileInput {
		private String name;
		private String email;
		private String address;
		private String city;
		private String province;
		private String postalCode;
		private User.Gender gender;
		private String birthDate;
		private String phoneNumber;
	}

	private final UserManager userManager = new UserManager();
	private final ServiceManager serviceManager = new ServiceManager();
	private final AccountingProcedure accountingProcedure = new AccountingProcedure();

	private final ConsoleCommand[] MAIN_MENU = new ConsoleCommand[] {
			ConsoleCommand.AGENT,
			ConsoleCommand.PROFESSIONAL,
			ConsoleCommand.MEMBER,
			ConsoleCommand.ACCOUNTING_PROCEDURE,
			ConsoleCommand.QUIT
	};

	private final ConsoleCommand[] AGENT_ACTIONS = new ConsoleCommand[] {
			ConsoleCommand.NEW_CLIENT,
			ConsoleCommand.NEW_PROFESSIONAL,
			ConsoleCommand.ADD_SERVICE,
			ConsoleCommand.MODIFY_SERVICE,
			ConsoleCommand.MANAGE_CANCELLED_SESSIONS,
			ConsoleCommand.REMOVE_SERVICE,
			ConsoleCommand.ACCOUNTING_REPORT,
			ConsoleCommand.QUIT
	};

	private final ConsoleCommand[] MEMBER_ACTIONS = new ConsoleCommand[] {
			ConsoleCommand.SUBSCRIBE_TO_SERVICE,
			ConsoleCommand.CANCEL_SUBSCRIBTION_TO_SERVICE,
			ConsoleCommand.SEE_MEMBER_INVOICE,
			ConsoleCommand.UPDATE_MEMBER_INFO,
			ConsoleCommand.DELETE_MEMBER,
			ConsoleCommand.QUIT
	};

	private final ConsoleCommand[] PROFESSIONAL_ACTIONS = new ConsoleCommand[] {
			ConsoleCommand.CONFIRM_MEMBER_SUBSCRIPTION,
			ConsoleCommand.SEE_SUBSCRIPTIONS,
			ConsoleCommand.SEE_PROFESSIONAL_PAYMENT_NOTICE,
			ConsoleCommand.UPDATE_PROFESSIONAL_INFO,
			ConsoleCommand.DELETE_PROFESSIONAL,
			ConsoleCommand.QUIT
	};

	private final ConsoleCommand[] CANCELLED_SESSIONS_ACTIONS = new ConsoleCommand[] {
			ConsoleCommand.CANCEL_SESSION,
			ConsoleCommand.REMOVE_CANCELLED_SESSION,
			ConsoleCommand.RETURN
	};

	/**
	 * Entry point
	 * @param args Program arguments (unused)
	 */
	public static void main(String[] args) {
		Gym app = new Gym();
		app.run();
	}

	/**
	 * Executes the program. Contains the main loop.
	 */
	private void run() {
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

		// Ces données initiales sont présentes pour faciliter les tests.

		// Ajout de 2 professionnels
		userManager.createProfessional("Jean Tremblay", "jean.tremblay@gmail.com", "111 rue abc", "montreal", "QC", "ABCDEF", User.Gender.MALE, "11-11-1111", "12");
		userManager.createProfessional("Pierre Tremblay", "pierre.tremblay@gmail.com", "222 rue def", "quebec", "QC", "CODEPO", User.Gender.MALE, "22-02-2222", "123");
		Professional professional1 = (Professional) userManager.fetchUser("jean.tremblay@gmail.com", false);
		Professional professional2 = (Professional) userManager.fetchUser("pierre.tremblay@gmail.com", false);

		// Ajout de 3 membres
		userManager.createMember("John Doe", "john.doe@gmail.com", "333 rue ghi", "Vancouver", "BC", "POCODE", User.Gender.MALE, "10-01-1010", "1");
		userManager.createMember("James Doe", "james.doe@gmail.com", "444 rue jkl", "Kuujjuaq", "QC", "H0H0H0", User.Gender.MALE, "01-01-0101", "2");
		userManager.createMember("Janie Doe", "janie.doe@gmail.com", "555 rue mno", "Yellowknife", "TN", "GROLAC", User.Gender.FEMALE, "21-12-1212", "3");

		// Création de 3 services par professionnel
		serviceManager.createService(professional1, "Cours de yoga", "01-12-2020", "30-12-2020", "12:30", 15, 30, "des commentaires", new String[]{"MONDAY", "TUESDAY", "WEDNESDAY"});
		serviceManager.createService(professional2, "Cours de yoga", "01-12-2020", "30-12-2020", "14:00", 10, 20, "des commentaires", new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
		serviceManager.createService(professional1, "Crossfit", "01-12-2020", "30-12-2020", "16:00", 10, 20, "des commentaires", new String[]{"WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
		serviceManager.createService(professional2, "Crossfit", "01-12-2020", "30-12-2020", "18:30", 20, 20, "des commentaires", new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});
		serviceManager.createService(professional1, "Aérobie", "01-12-2020", "30-12-2020", "20:00", 10, 20, "des commentaires", new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"});
		serviceManager.createService(professional2, "Aérobie", "01-12-2020", "30-12-2020", "23:00", 15, 20, "des commentaires", new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"});

		try {
			boolean running = true;

			while (running) {
				System.out.println("--- CENTRE DE DONNÉES #GYM ---\n");

				switch (promptActions(consoleIn, MAIN_MENU)) {
					case AGENT:
						showAgentMenu(consoleIn);
						break;
					case PROFESSIONAL:
						showProfessionalMenu(consoleIn);
						break;
					case MEMBER:
						showMemberMenu(consoleIn);
						break;
					case ACCOUNTING_PROCEDURE:
						doAccountingProcedure();
						break;
					default:
						running = false;
				}
				System.out.println();
			}
		} catch (IOException e) {
			System.out.println("Erreur interne : impossible de lire les commandes.");
		}
	}

	/**
	 * Shows the menu for actions to be executed by an agent.
	 * @param consoleIn User input stream
	 * @throws IOException In case of an I/O error
	 */
	private void showAgentMenu(BufferedReader consoleIn)
			throws IOException {
		boolean running = true;
		while (running) {
			System.out.println("- MENU PRINCIPAL -");
			switch (promptActions(consoleIn, AGENT_ACTIONS)) {
				case NEW_CLIENT:
					showCreateUserMenu(consoleIn, false);
					break;
				case NEW_PROFESSIONAL:
					showCreateUserMenu(consoleIn, true);
					break;
				case ADD_SERVICE:
					createService(consoleIn);
					break;
				case MODIFY_SERVICE:
					modifyService(consoleIn);
					break;
				case MANAGE_CANCELLED_SESSIONS:
					manageCancelledSessions(consoleIn);
					break;
				case REMOVE_SERVICE:
					deleteService(consoleIn);
					break;
				case ACCOUNTING_REPORT:
					generateProvisionalAccountingReport();
				case QUIT:
					running = false;
			}
		}
	}

	/**
	 * Shows the interface for the creation of a new user account.
	 * @param consoleIn User input stream
	 * @param isProfessional True if the user to be created is a professional, otherwise false.
	 * @throws IOException In case of an I/O error
	 */
	private void showCreateUserMenu(BufferedReader consoleIn, boolean isProfessional)
			throws IOException {
		System.out.println("Veuillez entrer les informations du nouveau "
				+ (isProfessional ? "professionnel" : "membre")
				+ "\ndans le format suivant, ou laisser vide pour annuler :");

		UserProfileInput profile = promptUserProfileData(consoleIn, true);
		boolean valid = false;

		while (profile != null && !valid) {
			if (isProfessional) {
				valid = userManager.createMember(profile.name, profile.email, profile.address, profile.city,
						profile.province, profile.postalCode, profile.gender, profile.birthDate, profile.phoneNumber);
			} else {
				valid = userManager.createProfessional(profile.name, profile.email, profile.address, profile.city,
						profile.province, profile.postalCode, profile.gender, profile.birthDate, profile.phoneNumber);
			}

			if (!valid) {
				System.out.println("Données invalides.");
				profile = promptUserProfileData(consoleIn, false);
			} else {
				System.out.println((isProfessional ? "Professionnel" : "Membre") + " créé avec succès !");
			}
		}

		System.out.println();
	}

	/**
	 * Shows the menu asking for all the details needed to create a new service, and creates
	 * the service accordingly.
	 * @param consoleIn User input stream
	 * @throws IOException In case of an I/O error
	 */
	private void createService(BufferedReader consoleIn)
			throws IOException {
		Professional professional = askForProfessionalID(consoleIn);

		if (professional != null) {
			System.out.println("Veuillez entrer les informations du nouveau service\n"
									   + "dans le format suivant, ou laisser vide pour annuler :"
									   + "\n\t[nom];[date de début (JJ-MM-AAAA)];[date de fin (JJ-MM-AAAA)];[heure (HH:mm)];"
									   + "\n\t[capacité (personnes)];[prix ($)];[récurrence hebdomadaire (MONDAY,...,SUNDAY)];"
									   + "\n\t[commentaires (facultatif)]");

			String input = consoleIn.readLine();

			if (input.length() > 0) {
				boolean valid = false;

				while (!valid) {
					String[] inputParts = input.split(";");

					if (inputParts.length == 8) {
						try {
							String name = inputParts[0];
							String beginningDate = inputParts[1];
							String endDate = inputParts[2];
							String serviceHour = inputParts[3];
							int capacity = Integer.parseInt(inputParts[4]);
							int cost = Integer.parseInt(inputParts[5]);
							String[] recurrence = inputParts[6].split(",");
							String comments = inputParts[7];

							valid = serviceManager.createService(professional, name, beginningDate, endDate,
									serviceHour, capacity, cost, comments, recurrence);
						} catch (NumberFormatException e) {
						}
					}

					if (!valid)
						System.out.println("Entrée mal formatée.");
				}

				System.out.println("Service créé avec succès.");
			}
		}

		System.out.println();
	}

	/**
	 * Shows the menu allowing to change a service's details.
	 * @param consoleIn User input stream
	 * @throws IOException In case of an I/O error
	 */
	private void modifyService(BufferedReader consoleIn)
			throws IOException {
		Professional professional = askForProfessionalID(consoleIn);

		if (professional != null) {
			List<Service> services = serviceManager.getProfessionalServices(professional);

			printServicesByProfessional(services);

			if (services.size() > 0) {
				Service service = promptChoice(consoleIn, services);

				if (service != null) {
					String input;

					System.out.println("Pour chaque attribut, laisser vide pour conserver le même,\n"
											   + "ou entrer une nouvelle valeur pour le modifier.");

					System.out.println("Nom: " + service.getName());
					System.out.print("Nouveau nom: ");
					if ((input = consoleIn.readLine()).length() > 0)
						service.setName(input);

					System.out.println("Date de début: " + DateOperations.dayFormat(service.getBeginningDate()));
					System.out.print("Nouvelle date de début: ");
					while ((input = consoleIn.readLine()).length() > 0
								   && !service.setBeginningDate(input)) {
						System.out.print("Format de date invalide. Réessayer : ");
					}

					System.out.println("Date de fin: " + DateOperations.dayFormat(service.getEndDate()));
					System.out.print("Nouvelle date de fin: ");
					while ((input = consoleIn.readLine()).length() > 0
								   && !service.setEndDate(input)) {
						System.out.print("Format de date invalide. Réessayer : ");
					}

					System.out.println("Heure: " + DateOperations.timeFormat(service.getServiceHour()));
					System.out.print("Nouvelle heure: ");
					while ((input = consoleIn.readLine()).length() > 0
								   && !service.setServiceHour(input)) {
						System.out.print("Heure invalide. Réessayer : ");
					}

					System.out.println("Capacité: " + service.getCapacity());
					System.out.print("Nouvelle capacité: ");
					boolean isValid = false;
					while (!isValid && (input = consoleIn.readLine()).length() > 0) {
						try {
							int capacity = Integer.parseInt(input);
							isValid = service.setCapacity(capacity);
						} catch (NumberFormatException e) {
						}
						if (!isValid)
							System.out.print("Capacité invalide. Réessayer : ");
					}

					System.out.println("Prix: " + service.getCost() + "$");
					System.out.print("Nouveau prix: ");
					isValid = false;
					while (!isValid && (input = consoleIn.readLine()).length() > 0) {
						try {
							int cost = Integer.parseInt(input);
							isValid = service.setCost(cost);
						} catch (NumberFormatException e) {
						}
						if (!isValid)
							System.out.print("Prix invalide. Réessayer : ");
					}

					System.out.println("Récurrence hebdomadaire: "
											   + DateOperations.formatDaysOfWeekRawNames(service.getRecurrence()));
					System.out.print("Nouvelle récurrence hebdomadaire: ");
					while ((input = consoleIn.readLine()).length() > 0
								   && !service.setRecurrence(input.split(","))) {
						System.out.print("Entrée invalide. Réessayer : ");
					}

					System.out.println("Commentaires: " + service.getComments());
					System.out.print("Nouveaux commentaires: ");
					while ((input = consoleIn.readLine()).length() > 0
								   && !service.setComments(input)) {
						System.out.print("Commentaires trop longs (max. 100 carac.). Réessayer : ");
					}

					System.out.println("Service modifié avec succès.");
				}
			}
		}

		System.out.println();
	}

	/**
	 * Shows a menu allowing to cancel a specific service session or to re-offer cancelled sessions.
	 * @param consoleIn User input stream
	 * @throws IOException In case of an I/O error
	 */
	private void manageCancelledSessions(BufferedReader consoleIn)
			throws IOException {
		Professional professional = askForProfessionalID(consoleIn);

		if (professional != null) {
			List<Service> services = serviceManager.getProfessionalServices(professional);

			printServicesByProfessional(services);

			if (services.size() > 0) {
				Service service = promptChoice(consoleIn, services);

				if (service != null) {
					List<Session> cancelledSessions = service.getCancelledSessionsToCome();

					System.out.println("Séances annulées à venir:");
					printSessions(cancelledSessions, false);

					if (cancelledSessions.size() == 0) {
						showCancelSessionMenu(consoleIn, service);
					} else {
						System.out.println("Actions:");
						switch (promptActions(consoleIn, CANCELLED_SESSIONS_ACTIONS)) {
							case CANCEL_SESSION:
								showCancelSessionMenu(consoleIn, service);
								break;
							case REMOVE_CANCELLED_SESSION:
								System.out.println("Entrer le numéro de la séance (dans la liste ci-haut) à offrir.");
								Session choice = promptChoice(consoleIn, cancelledSessions);
								if (choice != null) {
									choice.setIsCancelled(false);
									System.out.println("La séance est maintenant offerte.");
								}
								break;
						}
					}
				}
			}
		}

		System.out.println();
	}

	/**
	 * Shows a menu allowing to cancel a specific service session.
	 * @param consoleIn User input stream
	 * @param service Service for which a session is to be cancelled.
	 * @throws IOException In case of an I/O error
	 */
	private void showCancelSessionMenu(BufferedReader consoleIn, Service service)
			throws IOException {
		String input;

		System.out.print("Entrer la date de la séance à annuler dans le format" +
								 "\nJJ-MM-AAAA (laisser vide pour annuler) : ");

		while ((input = consoleIn.readLine()).length() > 0
					   && !service.cancelSession(input.trim())) {
			System.out.print("Format de date invalide. Réessayer : ");
		}

		if (input.length() > 0)
			System.out.println("Séance annulée.\n");
	}

	/**
	 * Shows a menu allowing to completely delete a service.
	 * @param consoleIn User input stream
	 * @throws IOException In case of an I/O error
	 */
	private void deleteService(BufferedReader consoleIn)
			throws IOException {
		Professional professional = askForProfessionalID(consoleIn);

		if (professional != null) {
			List<Service> services = serviceManager.getProfessionalServices(professional);

			printServicesByProfessional(services);

			if (services.size() > 0) {
				Service service = promptChoice(consoleIn, services);

				if (service != null) {
					System.out.println("Êtes-vous certain(e) de vouloir supprimer ce service ? (O/n)");

					if (askConfirmation(consoleIn)) {
						serviceManager.deleteService(service);
						System.out.println("Service supprimé avec succès.");
					}
				}
			}
		}

		System.out.println();
	}

	/**
	 * Shows a menu asking for a professional user ID, and returns that professional.
	 * @param consoleIn User input stream
	 * @return The corresponding Professional object, or null if the operation was cancelled.
	 */
	private Professional askForProfessionalID(BufferedReader consoleIn)
			throws IOException {
		Professional professional = null;

		while (professional == null) {
			System.out.print("Identifiant numérique du professionnel (laisser vide pour quitter) : ");
			String input = consoleIn.readLine();

			try {
				if (input.length() > 0) {
					int id = Integer.parseInt(input);
					professional = (Professional) userManager.fetchUser(id, false);

					if (professional == null)
						System.out.println("Professionnel non trouvé.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrée invalide");
			}
		}

		return professional;
	}

	/**
	 * Prints the accounting report for all operations since last friday.
	 * Can be executed at any time during the week.
	 */
	private void generateProvisionalAccountingReport() {
		Map<Professional, List<Service>> servicesPerProfessional = serviceManager.getServicesPerProfessional();
		LocalDate today = LocalDate.now();
		LocalDate lastFriday = DateOperations.getLastFridayDate();

		accountingProcedure.computePayments(lastFriday, today, servicesPerProfessional);

		System.out.println("Rapport provisoire de la semaine:");
		System.out.println(accountingProcedure.generateReport());
	}

	/**
	 * Returns a User object corresponding to the email address typed in through consoleIn.
	 * @param consoleIn User input stream
	 * @param isProfessional True if the user to be logged in is a professional.
	 * @return A User object if the user typed a valid email, or null if the operation was cancelled.
	 * @throws IOException In case of an I/O error
	 */
	private User logInUser(BufferedReader consoleIn, boolean isProfessional)
			throws IOException {
		User user = null;

		while (user == null) {
			System.out.print("Adresse courriel (laisser vide pour quitter) : ");
			String input = consoleIn.readLine();

			if (input.length() > 0) {
				user = userManager.fetchUser(input, !isProfessional);

				if (user == null)
					System.out.println((isProfessional ? "Professionnel" : "Membre") + " non trouvé.");
				else
					System.out.println("Validé");
			} else {
				break;
			}
		}

		System.out.println();

		return user;
	}

	/**
	 * Shows the menu for actions to be executed by a professional.
	 * @param consoleIn User input stream User input stream.
	 * @throws IOException In case of an I/O error
	 */
	private void showProfessionalMenu(BufferedReader consoleIn)
			throws IOException {
		Professional professional = (Professional) logInUser(consoleIn, true);

		if (professional != null) {
			boolean running = true;

			while (running) {
				System.out.println("- MENU PRINCIPAL -");
				System.out.println(professional.getName()
						+ " (" + professional.getUserID() + ")");

				switch (promptActions(consoleIn, PROFESSIONAL_ACTIONS)) {
					case CONFIRM_MEMBER_SUBSCRIPTION:
						confirmMemberSubscription(consoleIn, professional);
						break;
					case SEE_SUBSCRIPTIONS:
						seeSubscriptions(consoleIn, professional);
						break;
					case SEE_PROFESSIONAL_PAYMENT_NOTICE:
						seeProfessionalPaymentNotice(professional);
						break;
					case UPDATE_PROFESSIONAL_INFO:
						updateUserInfo(consoleIn, professional, true);
						break;
					case DELETE_PROFESSIONAL:
						running = !deleteUser(consoleIn, professional, true);
						break;
					case QUIT:
						running = false;
				}
			}
		}
	}

	/**
	 * Shows a menu allowing to confirm a member's subscription to a session.
	 * @param consoleIn User input stream
	 * @param professional
	 * @throws IOException In case of an I/O error
	 */
	private void confirmMemberSubscription(BufferedReader consoleIn, Professional professional)
			throws IOException {
		List<Session> sessions = serviceManager.getSessionsForToday(professional);

		System.out.println("Séances que vous offrez aujourd'hui :");
		printSessions(sessions, true);

		if (sessions.size() > 0) {
			Session chosenSession = promptChoice(consoleIn, sessions);

			if (chosenSession != null) {
				System.out.print("Numéro du membre inscrit (laisser vide pour annuler) : ");
				String input;
				Member member = null;
				while (member == null && (input = consoleIn.readLine()).length() > 0) {
					try {
						int memberID = Integer.parseInt(input);
						member = (Member) userManager.fetchUser(memberID, true);
					} catch (NumberFormatException e) {}

					if (member == null) {
						System.out.print("Membre inexistant. Réessayer : ");
					} else {
						System.out.print("Commentaires (facultatif) : ");
						String comments = consoleIn.readLine();

						if (chosenSession.getService().makeConfirmation(member, comments))
							System.out.println("Inscription de " + member.getName() + " confirmée avec succès.");
						else
							System.out.println("Erreur : l'inscription n'a pas pu être confirmée.");
					}
				}
			}
		}

		System.out.println();
	}

	/**
	 * Shows a menu allowing to print a list of all members subscribed to a specific session today.
	 * @param consoleIn User input stream
	 * @param professional Service owner
	 * @throws IOException In case of an I/O error
	 */
	private void seeSubscriptions(BufferedReader consoleIn, Professional professional)
			throws IOException {
		List<Session> sessions = serviceManager.getSessionsForToday(professional);

		System.out.println("Séances que vous offrez aujourd'hui :");
		printSessions(sessions, true);

		if (sessions.size() > 0) {
			Session chosenSession = promptChoice(consoleIn, sessions);

			if (chosenSession != null) {
				for (Registration registration : chosenSession.getRegistrations()) {
					Member member = registration.getMember();
					System.out.println("- " + member.getName() + " (" + member.getUserID() + ")");
				}

				System.out.println();
			}
		}
	}

	/**
	 * Prints a professional's latest payment notice generated by the accounting procedure, if any.
	 * @param professional Professional whose payment notice will be printed
	 */
	private void seeProfessionalPaymentNotice(Professional professional) {
		String paymentNotice = professional.getLatestAccountingReport();

		System.out.println(paymentNotice != null
								   ? paymentNotice
								   : "Aucun avis de paiement n'a été généré.\n");
	}

	/**
	 * Prints a list of services, if any.
	 * @param services Services to print
	 */
	private void printServicesByProfessional(List<Service> services) {
		if (services.size() == 0) {
			System.out.println("Ce professionnel n'a aucun service à son nom.");
		} else {
			System.out.println("Services appartenant à ce professionnel :");
			int i = 1;
			for (Service service : services) {
				System.out.println(i + " : " + service.getName() + ", à " + service.getServiceHour()
						+ ", les " + DateOperations.formatDaysOfWeek(service.getRecurrence()));
				i++;
			}
		}
	}

	/**
	 * Prints a list of sessions, if any.
	 * @param sessions Sessions to print
	 * @param showServiceInfo If true, will show additional details about the corresponding service.
	 */
	private void printSessions(List<Session> sessions, boolean showServiceInfo) {
		if (sessions.size() == 0) {
			System.out.println("Aucune.");
		} else {
			int i = 1;
			for (Session session : sessions) {
				if (showServiceInfo) {
					System.out.println(i + " : " + session.getService().getName()
							+ ", le " + DateOperations.dayFormat(session.getSessionDate())
							+ ", à " + session.getService().getServiceHour());
				} else {
					System.out.println(i + " : " + DateOperations.dayFormat(session.getSessionDate()));
				}
				i++;
			}
		}
	}

	/**
	 * Shows the menu for actions to be executed by a member.
	 * @param consoleIn User input stream
	 * @throws IOException In case of an I/O error
	 */
	private void showMemberMenu(BufferedReader consoleIn)
			throws IOException {
		Member member = (Member) logInUser(consoleIn, false);

		if (member != null) {
			boolean isValid = member.getValidity();
			if (isValid) {
				boolean running = true;
				while (running) {
					System.out.println("- MENU PRINCIPAL -");
					System.out.println(member.getName()
							+ " (" + member.getUserID() + ")");
					switch (promptActions(consoleIn, MEMBER_ACTIONS)) {
						case SUBSCRIBE_TO_SERVICE:
							subscribeMemberToService(consoleIn, member);
							break;
						case CANCEL_SUBSCRIBTION_TO_SERVICE:
							cancelSubscriptionToService(consoleIn, member);
							break;
						case SEE_MEMBER_INVOICE:
							seeMemberInvoice(member);
							break;
						case UPDATE_MEMBER_INFO:
							updateUserInfo(consoleIn, member, false);
							break;
						case DELETE_MEMBER:
							running = !deleteUser(consoleIn, member, false);
							break;
						case QUIT:
							running = false;
					}
				}
			} else {
				System.out.println("MEMBRE SUSPENDU: " + member.getName()
						+ " (" + member.getUserID() + ")");
			}
		}
	}

	/**
	 * Shows a menu allowing to subscribe a member to a specific service offered today.
	 * @param consoleIn User input stream
	 * @param member Member to be subscribed.
	 * @throws IOException In case of an I/O error
	 */
	private void subscribeMemberToService(BufferedReader consoleIn, Member member)
			throws IOException {
		List<Session> availableSessions = serviceManager.getSessionsForToday();

		if (availableSessions.size() == 0) {
			System.out.println("Aucun service disponible pour aujourd'hui.");
		} else {
			printSessionsForToday(availableSessions);

			Session selectedSession = promptChoice(consoleIn, availableSessions);

			if (selectedSession != null) {
				System.out.println("Commentaires (facultatif):");
				String comments = consoleIn.readLine();

				if (selectedSession.getService().makeRegistrationForToday(member, comments))
					System.out.println("Inscription enregistrée.");
				else
					System.out.println("Erreur : l'inscription n'a pas pu être enregistrée.");
			}
		}

		System.out.println();
	}

	/**
	 * Shows a menu allowing to cancel a member's subscription to a service.
	 * @param consoleIn User input stream
	 * @param member Member whose subscription is to be cancelled
	 * @throws IOException In case of an I/O error
	 */
	private void cancelSubscriptionToService(BufferedReader consoleIn, Member member)
			throws IOException {
		List<Session> subscriptions = serviceManager.getMemberSubscriptionsForToday(member);

		if (subscriptions.size() == 0) {
			System.out.println("Vous n'êtes inscrit(e) à aucun service.");
		} else {
			System.out.println("Inscriptions aujourd'hui:");
			printSessionsForToday(subscriptions);
			System.out.print("Choix (laisser vide pour annuler): ");

			Session toCancel = promptChoice(consoleIn, subscriptions);

			if (toCancel != null) {
				toCancel.unsubscribeMember(member);
				System.out.println("Inscription annulée avec succès.");
			}
		}

		System.out.println();
	}

	/**
	 * Prints a member's latest invoice generated by the accounting procedure, if any.
	 * @param member Member whose invoice is to be printed
	 */
	private void seeMemberInvoice(Member member) {
		String invoice = member.getLatestAccountingReport();

		System.out.println(invoice != null
								   ? invoice
								   : "Aucune facture n'a été générée.\n");
	}

	/**
	 * Prints a list of sessions.
	 * @param sessions Sessions to be printed
	 */
	private void printSessionsForToday(List<Session> sessions) {
		int i = 1;
		for (Session session : sessions) {
			System.out.println(i + ": " + session.getService().getName()
					+ ", par " + session.getService().getOfferedBy().getName()
					+ ", à " + DateOperations.timeFormat(session.getService().getServiceHour()));
			i++;
		}

	}

	/**
	 * Deletes a user's or a professional's account, asking for a confirmation before proceeding.
	 * @param consoleIn User input stream
	 * @param user User whose account is to be deleted
	 * @param isProfessional True if the user to delete is a professional.
	 * @return True if the account has been deleted, false if the operation has been cancelled.
	 * @throws IOException In case of an I/O error
	 */
	private boolean deleteUser(BufferedReader consoleIn, User user, boolean isProfessional)
			throws IOException {
		System.out.print("Voulez-vous vraiment effacer votre compte et résilier votre inscription ?"
				+"\nCette action est irréversible. (O/n) ");

		if (askConfirmation(consoleIn)) {
			if (isProfessional) {
				serviceManager.deleteAllServicesFromProfessional((Professional) user);
				userManager.deleteProfessional(user.getUserID());
			} else {
				serviceManager.deleteAllSubscriptionsForMember((Member) user);
				userManager.deleteMember(user.getUserID());
			}

			System.out.println("Compte effacé avec succès.\n");
			return true;
		} else {
			System.out.println();
			return false;
		}
	}

	/**
	 * Asks the user to type in a number from 1 to n, where n is the number of elements in the provided list,
	 * and returns the n-th element in that list, unless the operation was cancelled.
	 * @param consoleIn User input stream
	 * @param list List from which to choose an item
	 * @param <T> Type of the elements in the list
	 * @return The chosen element, or null if the user typed a blank line
	 * @throws IOException In case of an I/O error
	 */
	private <T> T promptChoice(BufferedReader consoleIn, List<T> list)
			throws IOException {
		boolean validChoice = false;
		int choice = -1;

		while (!validChoice) {
			System.out.print("Choix (laisser vide pour annuler): ");
			String input = consoleIn.readLine();

			if (input.length() == 0) {
				validChoice = true;
			} else {
				try {
					choice = Integer.parseInt(input);

					if (choice >= 1 && choice <= list.size())
						validChoice = true;
				} catch (NumberFormatException e) {}
			}

			if (!validChoice)
				System.out.println("Entrée invalide. Valeurs acceptées : 1 à " + list.size() + ".");
		}

		if (choice > 0)
			return list.get(choice - 1);
		else
			return null;
	}

	/**
	 * Asks the user for a confirmation by typing either "O" (acceptation) or "n" (refusal).
	 * @param consoleIn User input stream
	 * @return True if the user accepted, false otherwise.
	 * @throws IOException In case of an I/O error
	 */
	private boolean askConfirmation(BufferedReader consoleIn)
			throws IOException {
		while (true) {
			switch (consoleIn.readLine().trim()) {
				case "O": return true;
				case "n": return false;
				default: System.out.print("Entrée invalide. Choix : «O» ou «n». Réessayer : ");
			}
		}
	}

	/**
	 * Shows a menu allowing to update a user's profile.
	 * @param consoleIn User input stream
	 * @param user User whose profile is to be updated.
	 * @param isProfessional True if the profile to modify is that of a professional.
	 * @throws IOException In case of an I/O error
	 */
	private void updateUserInfo(BufferedReader consoleIn, User user, boolean isProfessional)
			throws IOException {
		System.out.println(user.getName()
				+ ", " + user.getGender()
				+ ", " + DateOperations.dayFormat(user.getBirthDate())
				+ ", " + user.getPhoneNumber());

		System.out.println("Veuillez entrer les nouvelles informations du "
				+ (isProfessional ? "professionnel" : "membre")
				+ " dans le format suivant,\n"
				+ "en laissant vides les champs des données inchangées :");

		UserProfileInput profile = promptUserProfileData(consoleIn, true);

		boolean nameValid = false;
		boolean emailValid = false;
		boolean addressValid = false;
		boolean cityValid = false;
		boolean provinceValid = false;
		boolean postalCodeValid = false;
		boolean birthDateValid = false;

		while (profile != null && !(nameValid && emailValid && addressValid && cityValid
				&& provinceValid && postalCodeValid && birthDateValid)) {
			nameValid = (profile.name.length() == 0 || user.setName(profile.name));
			emailValid = (profile.name.length() == 0 || user.setEmail(profile.email));
			addressValid = (profile.name.length() == 0 || user.setAddress(profile.address));
			cityValid = (profile.name.length() == 0 || user.setCity(profile.city));
			provinceValid = (profile.name.length() == 0 || user.setProvince(profile.province));
			postalCodeValid = (profile.name.length() == 0 || user.setPostalCode(profile.postalCode));
			birthDateValid = (profile.birthDate.length() == 0 || user.setBirthDate(profile.birthDate));
			if (profile.gender != null)
				user.setGender(profile.gender);

			if (nameValid && emailValid && addressValid && cityValid
					&& provinceValid && postalCodeValid && birthDateValid) {
				System.out.println("Données modifiées avec succès.\n");
			} else {
				System.out.println("Les données suivantes sont invalides :"
						+ (nameValid ? "" : "\n- Nom")
						+ (emailValid ? "" : "\n- Courriel")
						+ (addressValid ? "" : "\n- Adresse")
						+ (cityValid ? "" : "\n- Ville")
						+ (provinceValid ? "" : "\n- Province")
						+ (postalCodeValid ? "" : "\n- Code postal")
						+ (birthDateValid ? "" : "\n- Date de naissance"));
				System.out.println("Les autres données ont été enregistrées.\n"
						+ "Vous pouvez recommencer en spécifiant correctement les données invalides,\n "
						+ "ou laisser vides les champs correspondants, ou encore ne rien écrire pour\n "
						+ "annuler l'opération.\n"
						+ "Format attendu :");
				profile = promptUserProfileData(consoleIn, true);
			}
		}

		System.out.println();

	}

	/**
	 * Asks the user to type in a set of attributes for a user profile, and returns them as a UserProfileInput object.
	 * @param consoleIn User input stream
	 * @param showFormat If true, a short explanatory text will be printed to show the expected syntax.
	 * @return A UserProfileInput object
	 * @throws IOException In case of an I/O error
	 */
	private UserProfileInput promptUserProfileData(BufferedReader consoleIn, boolean showFormat)
			throws IOException {
		String input;
		UserProfileInput profile = null;

		if (showFormat)
			System.out.println("\t[nom];[courriel];[adresse];[ville];[province];[code postal];"
					+ "\n\t[genre (M, F ou X)];[date de naissance JJ-MM-AAAA];[téléphone]");

		input = consoleIn.readLine();

		while (input.length() > 0 && profile == null) {
			String[] inputParts = input.split(";", -1);

			if (inputParts.length == 9) {
				User.Gender gender = User.Gender.findGender(inputParts[6].trim().toUpperCase());

				if (gender != null || inputParts[6].trim().length() == 0) {
					profile = new UserProfileInput();
					profile.name = inputParts[0].trim();
					profile.email = inputParts[1].trim();
					profile.address = inputParts[2].trim();
					profile.city = inputParts[3].trim();
					profile.province = inputParts[4].trim();
					profile.postalCode = inputParts[5].replaceAll("\\s", "");
					profile.gender = gender;
					profile.birthDate = inputParts[7].trim();
					profile.phoneNumber = inputParts[8].trim();
				} else {
					System.out.println("Entrée mal formatée. Champ «genre» invalide. Réessayer :");
					input = consoleIn.readLine();
				}
			} else {
				System.out.println("Entrée mal formatée. Réessayer :");
				input = consoleIn.readLine();
			}
		}

		return profile;
	}

	/**
	 * Performs the accounting procedure, that is :
	 * - shows the EFT data;
	 * - shows the accounting report;
	 * - generates invoices and payment notices, and sends them to the corresponding users.
	 */
	private void doAccountingProcedure() {
		Map<Professional, List<Service>> servicesPerProfessional = serviceManager.getServicesPerProfessional();
		LocalDate today = LocalDate.now();
		LocalDate lastFriday = DateOperations.getLastFridayDate();

		accountingProcedure.computePayments(lastFriday, today, servicesPerProfessional);

		System.out.println("Données TEF par professionnel :");
		System.out.println(accountingProcedure.generateTefData());
		System.out.println("Rapport:");
		System.out.println(accountingProcedure.generateReport());

		// Générer la facture de chaque membre
		for (Member member : userManager.getMembers()) {
			member.setLatestAccountingReport(accountingProcedure.generateMemberReport(member));
		}

		// Générer l'avis de paiement de chaque professionnel
		for (Professional professional : userManager.getProfessionals()) {
			professional.setLatestAccountingReport(accountingProcedure.generateProfessionalReport(professional));
		}

		System.out.println("Les factures des membres et les avis de paiement des professionnels ont été générés.");
		System.out.println("Les usagers peuvent les consulter dans leur compte.");
	}

	/**
	 * Asks the user to choose among a set of actions passed in an array.
	 * @param consoleIn User input stream
	 * @param actions Actions from which to choose.
	 * @return The chosen action.
	 * @throws IOException In case of an I/O error
	 */
	private ConsoleCommand promptActions(BufferedReader consoleIn, ConsoleCommand[] actions)
			throws IOException {
		for(ConsoleCommand action : actions)
			System.out.println(action.command + ": " + action.label);

		ConsoleCommand returnedCommand = null;

		while (returnedCommand == null) {
			String command = consoleIn.readLine().toLowerCase();

			for (ConsoleCommand action : actions) {
				if (action.command.equals(command))
					returnedCommand = action;
			}

			if (returnedCommand == null)
				System.out.println("Commande invalide.");
		}

		System.out.println();

		return returnedCommand;
	}

}
