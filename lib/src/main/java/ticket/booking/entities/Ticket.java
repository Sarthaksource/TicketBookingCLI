package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(
		ignoreUnknown = true
)
public class Ticket
{
	private String ticketId;
	private String userId;
	private String source;
	private String destination;
	private String dateOfTravel;
	private Train train;
	private List<Integer> seatLocation;

	public Ticket() {
	}

	public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel, Train train, List<Integer> seatLocation) {
		this.ticketId = ticketId;
		this.userId = userId;
		this.source = source;
		this.destination = destination;
		this.dateOfTravel = dateOfTravel;
		this.train = train;
		this.seatLocation = seatLocation;
	}

	public String getTicketInfo() {
		return String.format("Ticket ID: %s belongs to User %s from %s to %s on %s in row %d and seat %d", this.ticketId, this.userId, this.source, this.destination, this.dateOfTravel, this.seatLocation.get(0)+1, this.seatLocation.get(1)+1);
	}

	public String getTicketId() {
		return this.ticketId;
	}

	public String getSource() {
		return this.source;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getDestination() {
		return this.destination;
	}

	public String getDateOfTravel() {
		return this.dateOfTravel;
	}

	public Train getTrain() {
		return this.train;
	}

	public List<Integer> getSeatLocation() { return this.seatLocation; }

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setDateOfTravel(String dateOfTravel) {
		this.dateOfTravel = dateOfTravel;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public void setSeatLocation(List<Integer> seatLocation) { this.seatLocation = seatLocation; }
}