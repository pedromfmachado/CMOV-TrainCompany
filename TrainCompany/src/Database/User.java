package Database;

public class User {

	public Integer User_id;
	public String name;
	public String token;
	public String email;
	
	
	User(Integer User_id, String name, String token, String email){
		this.User_id = User_id;
		this.name = name;
		this.token = token;
		this.email = email;
	}
	
	User(){}
}
