package ticket.booking.services;

import java.io.IOException;
import java.util.*;
import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

public class UserBookingService
{
	private User user;
	private List<User> userList;
	private TrainService trainService;
	private ObjectMapper objectMapper = new ObjectMapper();
	private static final String USERS_PATH = "D:\\JavaFullStack\\TicketBooking\\lib\\src\\main\\java\\ticket\\booking\\localDB\\users.json";

	public UserBookingService(User user) throws IOException {
		this.loadUserListFromFile();
		this.trainService = new TrainService();
		this.user = userList.stream()
				.filter(u -> u.getUserId().equals(user.getUserId()))
				.findFirst()
				.orElse(user);
	}

	public UserBookingService() throws IOException {
		this.loadUserListFromFile();
		this.trainService = new TrainService();
	}

	private void loadUserListFromFile() throws IOException {
		File users = new File(USERS_PATH);
		this.userList = this.objectMapper.readValue(users, new TypeReference<List<User>>() {});
	}

	public User login(String username, String password)
	{
		Optional<User> foundUser =
				userList.stream().filter(u -> u.getName().equals(username))
				.filter(u -> UserServiceUtil.checkPassword(password, u.getHashedPassword()))
				.findFirst();

		return foundUser.orElse(null);
	}

	public void signup(User user)
	{
		try{
			userList.add(user);
			saveUserListToFile();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

//	private void saveUserListToFile() throws IOException {
//		File usersFile = new File(USERS_PATH);
//		this.objectMapper.writeValue(usersFile, this.userList);
//	}

	private void saveUserListToFile() throws IOException {
		System.out.println("=== saveUserListToFile called ===");
		Thread.currentThread().getStackTrace();
		for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
			System.out.println(element);
		}
		File usersFile = new File(USERS_PATH);
		this.objectMapper.writeValue(usersFile, this.userList);
	}

	public List<Ticket> fetchBookings()
	{
		Optional<User> userFetched = userList.stream().filter(user1 -> {
			return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
		}).findFirst();

		List<Ticket> userTickets = new ArrayList<>();
		if(userFetched.isPresent()) userTickets = userFetched.get().getTicketsBooked();
        return userTickets;
    }

	public void cancelBooking(String ticketId)
	{
		if(ticketId!=null && !ticketId.isEmpty()) {
			Optional<User> foundUser = userList.stream().filter(u -> u.getUserId().equals(this.user.getUserId())).findFirst();
			if(foundUser.isPresent())
			{
				List<Ticket> ticketsBooked = foundUser.get().getTicketsBooked();
				Ticket ticketToRemove = ticketsBooked.stream().filter(t -> t.getTicketId().equals(ticketId)).findFirst().orElse(null);
                if (ticketToRemove == null) return;

                List<Integer> seatLocation = ticketToRemove.getSeatLocation();
				(ticketToRemove.getTrain().getSeats().get(seatLocation.get(0))).set(seatLocation.get(1), 0);
				trainService.updateTrain(ticketToRemove.getTrain());

				boolean removed = ticketsBooked.remove(ticketToRemove);
				if (removed) {
					System.out.println("Ticket with ID " + ticketId + " has been canceled.");
                    try {
                        saveUserListToFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
					System.out.println("No ticket found with ID " + ticketId);
				}
			}
			else {
				System.out.println("User not found!");
			}
		}
		else
		{
			System.out.println("Ticket ID cannot be null or empty.");
		}
	}

	public List<Train> getTrains(String source, String destination) {
		return trainService.searchTrains(source, destination);
	}

	public List<List<Integer>> fetchSeats(Train train) {
		return train.getSeats();
	}

	public Boolean bookTrainSeat(Train train, int row, int seat, Ticket ticket) {
		try {
			List<List<Integer>> seats = train.getSeats();
			if (row >= 0 && row < seats.size() && seat >= 0 && seat < (seats.get(row)).size()) {
				if ((seats.get(row)).get(seat) == 0) {
					(seats.get(row)).set(seat, 1);
					saveUserTrainDetailToUserList(ticket);
					trainService.addTrain(train);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	private void saveUserTrainDetailToUserList(Ticket ticket) throws IOException
	{
		Optional<User> foundUser = userList.stream().filter(u -> u.getUserId().equals(this.user.getUserId())).findFirst();
		foundUser.ifPresent(u -> u.getTicketsBooked().add(ticket));
		saveUserListToFile();
	}
}