package Database;

public class Reservation{

	public Integer uuid;
	public Integer User_id;
	public String canceled;
	public String date;
	public Integer departureStation_id;
	public Integer arrivalStation_id;
	public String departureStation_name;
	public String arrivalStation_name;

	
	public Reservation(Integer uuid, Integer User_id, String canceled, String date, Integer departureStation_id, Integer arrivalStation_id){
		
		this.uuid = uuid;
		this.User_id = User_id;
		this.canceled = canceled;
		this.date = date;
		this.departureStation_id = departureStation_id;
		this.arrivalStation_id = arrivalStation_id;
		
	}


	public Reservation() {
	}

}
