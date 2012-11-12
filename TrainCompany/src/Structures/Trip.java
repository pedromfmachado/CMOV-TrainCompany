package Structures;

public class Trip {

	public Integer Trip_id;
	public String beginTime;
	public Integer Train_id;
	public Integer departureStation_id;
	public Integer arrivalStation_id;
	public Integer Line_id;
	public Integer TripType_id;
	
	public Trip(Integer Trip_id, String beginTime, Integer Train_id, Integer departureStation_id, Integer arrivalStation_id, Integer Line_id, Integer TripType_id){
		
		this.Trip_id = Trip_id;
		this.beginTime = beginTime;
		this.Train_id = Train_id;
		this.departureStation_id = departureStation_id;
		this.arrivalStation_id = arrivalStation_id;
		this.Line_id = Line_id;
		this.TripType_id = TripType_id;
		
	}
	
	Trip(){}
	
}
