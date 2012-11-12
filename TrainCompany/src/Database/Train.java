package Database;

public class Train {

	public Integer Train_id;
	public Integer maximumCapacity;
	public Float velocity;
	
	
	Train(Integer Train_id, Integer maximumCapacity, Float velocity){
		this.Train_id = Train_id;
		this.maximumCapacity = maximumCapacity;
		this.velocity = velocity;
	}
	
	Train(){}
	
}
