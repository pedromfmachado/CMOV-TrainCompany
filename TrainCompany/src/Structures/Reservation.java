package Structures;

public class Reservation{

	public Integer id;
	public String uuid;
	public Integer user_id;
	public String canceled;
	public String date;
	public Integer departureStation_id;
	public Integer arrivalStation_id;
	public String departureStation_name;
	public String arrivalStation_name;
	

	public Reservation(Integer id, String uuid, Integer User_id, String canceled, String date, Integer departureStation_id, Integer arrivalStation_id){
		
		this.id = id;
		this.uuid = uuid;
		this.user_id = User_id;
		this.canceled = canceled;
		this.date = date;
		this.departureStation_id = departureStation_id;
		this.arrivalStation_id = arrivalStation_id;
		
	}

	public Reservation() {
		super();
	}

}
