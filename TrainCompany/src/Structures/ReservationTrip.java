package Structures;

public class ReservationTrip {

	public String time;
	public int departure_id, arrival_id, reservation_id, trip_id;
	
	public ReservationTrip(int departure, int arrival, String time, int r_id, int trip_id){
		
		this.departure_id = departure;
		this.arrival_id = arrival;
		this.time = time;
		this.reservation_id = r_id;
		this.trip_id = trip_id;
	}
	
	public ReservationTrip() {
		super();
	}
}
