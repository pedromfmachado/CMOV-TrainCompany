package Structures;

public class LineStation {

	public Integer Station_id;
	public Integer Line_id;
	public Integer order;
	public Integer distance;
	
	public LineStation(Integer order, Integer distance, Integer Station_id, Integer Line_id){
		this.order = order;
		this.distance = distance;
		this.Station_id = Station_id;
		this.Line_id = Line_id;
	}
	
	LineStation(){}
	
}
