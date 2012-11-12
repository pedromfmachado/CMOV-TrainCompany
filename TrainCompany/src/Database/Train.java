package Database;

public class Train {

	public Integer Train_id;
	public Integer maximum_capacity;
	public Float velocity;
	
	
	Train(Integer Train_id, Integer maximum_capacity, Float velocity){
		this.Train_id = Train_id;
		this.maximum_capacity = maximum_capacity;
		this.velocity = velocity;
	}
	
	Train(){}
	
}
