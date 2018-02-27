package model;

public class Request {
	public int id; 
	public String name;
	public String email;
	public String comment;
	public String cnpj; 
	
	public Request() {
		
	}
	
	public Request(int id, String name, String email, String comment, String cnpj) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.comment = comment;
		this.cnpj = cnpj;
	}
	
}
