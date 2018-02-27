package database.SQLite;

import java.util.ArrayList;

import model.Request;

public class TableDataSave {
	public static boolean insert(Request request) {
		int result = SQLiteMain.insert(request.name, request.email, request.comment, request.cnpj); 
		request.id = result;
		
		return (result!=-1);
		
	}
	
	public static boolean update(Request request) {
		return SQLiteMain.update(request.id, request.name, request.email, request.comment, request.cnpj);
	}
	
	public static ArrayList<Request> getAll() {
		return SQLiteMain.getAll();
	}
	
	public static ArrayList<Request> getByID(int id) {
		return SQLiteMain.getByID(id);
				
	}
	
	public static boolean delete(int id) {
		return SQLiteMain.delete(id);
	}
	
	public static int count() {
		return SQLiteMain.Count();
		
	}
}
