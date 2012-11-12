package Structures;

public class ReservationTrip {

	public String departureName, arrivalName, time;
	public int reservation_id, trip_id;
	
	public ReservationTrip(String departure, String arrival, String time, int r_id, int trip_id){
		
		this.departureName = departure;
		this.arrivalName = arrival;
		this.time = time;
		this.reservation_id = r_id;
		this.trip_id = trip_id;
	}
	
	public ReservationTrip() {}
}
