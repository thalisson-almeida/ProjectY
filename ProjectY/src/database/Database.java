package database;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.SQLite.TableDataSave;
import model.Request;
import model.Response;

public class Database {
	public final static String ERROR_EMPTY_CNPJ = "CNPJ is empty";
	public final static String ERROR_EMPTY_EMAIL = "E-mail is empty or invalid";
	public final static String ERROR_EMPTY_COMMENT = "Comment is empty";
	public final static String ERROR_EMPTY_NAME = "Name is empty";
	public final static String ERROR_INVALID_ID = "ID is invalid";
	public final static String ERROR_INSERT_FAIL = "Could not insert data in database";
	public final static String ERROR_UPDATE_FAIL = "Could not update data in database";
	public final static String ERROR_QUERY_FAIL = "Could not query the data";
	public final static String ERROR_QUERY_IDNOTFOUND = "Could not found the data with id ";
	public final static String ERROR_DELETE_FAIL = "Could not delete the data";

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	
	public static Response insert(Request request) {
		Response response = validateInsertRequest(request);
		if(!response.hasError) {
			boolean insertOK = TableDataSave.insert(request);
			if(insertOK) {
				response.setSuccess(request);
			}else {
				response.setError(ERROR_INSERT_FAIL);
			}
		}
		return response;
	}
	
	
	public static Response update(Request request) {
		Response response = validateUpdateRequest(request);
		if(!response.hasError) {
			boolean updateOK = TableDataSave.update(request);
			if(updateOK) {
				response.setSuccess(request);
			}else {
				response.setError(ERROR_UPDATE_FAIL);
			}
		}
		return response;
	}
	
	public static Response getAll() {
		Response response = new Response();
		
		try {
			ArrayList<Request> list = TableDataSave.getAll();
			response.setSuccess(list);
			
		}catch(Exception e) {
			response.setError(ERROR_QUERY_FAIL+" "+e.getMessage());
		
		}
		return response;
	}
	
	public static Response getByID(int id) {
		Response response = new Response();
		
		try {
			ArrayList<Request> list = TableDataSave.getByID(id);
			if(list.isEmpty() || list.size()>1) {
				response.setError(ERROR_QUERY_IDNOTFOUND+id);
			}else {
				response.setSuccess(list);
			}
		}catch(Exception e) {
			response.setError(ERROR_QUERY_FAIL);
		
		}
		return response;
	}
	
	public static Response delete(int id) {
		Response response = new Response();
		try {
			if(TableDataSave.delete(id)) {
				response.setSuccess();
			}else {
				response.setError(ERROR_DELETE_FAIL);
			}
		}catch(Exception e) {
			response.setError(ERROR_DELETE_FAIL+" "+e.getMessage());
		}
		return response;
	}
	
	private static Response validateUpdateRequest(Request request) {
		Response response;
		if(request.id > 0) {
			response = validateInsertRequest(request);			
		}else {
			response = new Response();
			response.setError(ERROR_INVALID_ID);
		}
		return response;
		
	}
	
	
	private static Response validateInsertRequest(Request request) {
		Response response = new Response();
		
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(request.email);
        
		if(request.cnpj.isEmpty()) {
			response.setError(ERROR_EMPTY_CNPJ);
			
		}else if(!matcher.find()) {
			response.setError(ERROR_EMPTY_EMAIL);
			
		}else if(request.comment.isEmpty()) {
			response.setError(ERROR_EMPTY_COMMENT);
			
		}else if(request.name.isEmpty()) {
			response.setError(ERROR_EMPTY_NAME);
			
		}
		
		return response;
	}
	
	
	
}
