package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(
		ignoreUnknown = true
)
public class User
{
	private String name;
	private String password;
	private String hashedPassword;
	private String userId;
	private List<Ticket> ticketsBooked;

	public User(String name, String password, String hashedPassword, List<Ticket> ticketsBooked, String userId) {
		this.name = name;
		this.password = password;
		this.hashedPassword = hashedPassword;
		this.ticketsBooked = ticketsBooked;
		this.userId = userId;
	}

	public User() {
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

	public String getHashedPassword() {
		return this.hashedPassword;
	}

	public List<Ticket> getTicketsBooked() {
		return this.ticketsBooked;
	}

	public String getUserId() {
		return this.userId;
	}

//	public List<Ticket> getTickets() {
//		return this.ticketsBooked;
//	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setTicketsBooked(List<Ticket> ticketsBooked) {
		this.ticketsBooked = ticketsBooked;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}