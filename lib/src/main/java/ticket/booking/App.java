package ticket.booking;

import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class App
{
	public static void main(String[] args)
	{
		System.out.println("Running Train Booking System");
		Scanner scanner = new Scanner(System.in);
		Train trainSelectedForBooking = new Train();
		Ticket userTicket = new Ticket();
		User currentUser = null;
		int option = 0;
		UserBookingService userBookingService;
		try {
			userBookingService = new UserBookingService();
		} catch (IOException e) {
			System.out.println("There is something wrong");
			return;
		}
		while(option!=6)
		{
			System.out.println("Choose option");
			System.out.println("1. Sign up");
			System.out.println("2. Login");
			System.out.println("3. Fetch Bookings");
			System.out.println("4. Book a Seat");
			System.out.println("5. Cancel my Booking");
			System.out.println("6. Exit the App");
			System.out.println();
			option = scanner.nextInt();
			switch (option)
			{
				case 1:
					System.out.println("Enter the username to signup");
					scanner.nextLine();
					String nameToSignUp = scanner.nextLine();
					System.out.println("Enter the password to signup");
					String passwordToSignUp = scanner.nextLine();
					currentUser = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
					userBookingService.signup(currentUser);
					break;
				case 2:
					System.out.println("Enter username to login");
					scanner.nextLine();
					String nameToLogin = scanner.nextLine();
					System.out.println("Enter password to login");
					String passwordToLogin = scanner.nextLine();

					User loggedInUser = userBookingService.login(nameToLogin, passwordToLogin);
					if (loggedInUser != null) {
						currentUser = loggedInUser;
                        try {
                            userBookingService = new UserBookingService(loggedInUser);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
					} else {
						System.out.println("Wrong name or password");
					}
					break;
				case 3:
					if(currentUser==null)
					{
						System.out.println("Please login and try again!");
						break;
					}
					System.out.println("Fetching your bookings");
					List<Ticket> tickets = userBookingService.fetchBookings();
					if(!tickets.isEmpty()) {
						int index = 1;
						for (Ticket ticket : tickets) {
							System.out.println(index + ". " + ticket.getTicketInfo());
							index++;
						}
					}
					break;
				case 4:
					if(currentUser==null)
					{
						System.out.println("Please login and try again!");
						break;
					}
					System.out.println("Type your source station");
					scanner.nextLine();
					String source = scanner.nextLine();
					System.out.println("Type your destination station");
					String destination = scanner.nextLine();
					List<Train> trains = userBookingService.getTrains(source, destination);
					if(trains.isEmpty()){
						System.out.println("No trains available for this route");
						break;
					}
					int index = 1;

					for (Train t : trains)
					{
						System.out.println(index + ". Train ID: " + t.getTrainId());
						for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
							System.out.println("Station: " + entry.getKey() + " Time: " + entry.getValue());
						}
						System.out.println();
						index++;
					}
					System.out.println("Select a train by typing 1,2,3...");
					int choice = scanner.nextInt();
					scanner.nextLine();
					if(choice < 1 || choice > trains.size()){
						System.out.println("Invalid choice");
						break;
					}
					trainSelectedForBooking = trains.get(choice - 1);

					for(List<Integer> row : userBookingService.fetchSeats(trainSelectedForBooking)) {
						for(Integer val : row) {
							System.out.print(val + " ");
						}
						System.out.println();
					}
					System.out.println("Select the seat by typing the row and column");
					System.out.println("Enter the row");
					int row = scanner.nextInt();
					System.out.println("Enter the column");
					int col = scanner.nextInt();
					System.out.println("Booking your seat....");
					List<Integer> seats = List.of(row-1, col-1);
					userTicket = new Ticket(UUID.randomUUID().toString(), currentUser.getUserId(), source, destination, LocalDate.now().toString(), trainSelectedForBooking, seats);

					Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row-1, col-1, userTicket);
					if (booked) {
						System.out.println("Booked! Enjoy your journey");
					} else {
						System.out.println("Can't book this seat");
					}
					break;
				case 5:
					if(currentUser==null)
					{
						System.out.println("Please login and try again!");
						break;
					}
					System.out.println("Your bookings");
					List<Ticket> ticketsToCancel = userBookingService.fetchBookings();
					if(!ticketsToCancel.isEmpty()) {
						int idx = 1;
						for (Ticket ticket : ticketsToCancel) {
							System.out.println(idx + ". " + ticket.getTicketInfo());
							idx++;
						}
						System.out.println("Select a ticket by typing 1,2,3...");
						int ticketChoice = scanner.nextInt();
						scanner.nextLine();
						if(ticketChoice < 1 || ticketChoice > ticketsToCancel.size()){
							System.out.println("Invalid choice");
							break;
						}
						userBookingService.cancelBooking(ticketsToCancel.get(ticketChoice-1).getTicketId());
					}
					break;
			}
		}
	}	
}