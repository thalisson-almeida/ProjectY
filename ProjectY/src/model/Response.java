package model;

import java.util.ArrayList;

public class Response {
	public final static String SUCCESS = "SUCCESS";
	
	public String result;
	public boolean hasError;
	public ArrayList<Request> resultRequest;
	
	public Response() {
		resultRequest = new ArrayList<Request>();
	}
	
	public void setError(String message) {
		result = message;
		hasError = true;
	}
	
	public void setSuccess() {
		result = Response.SUCCESS; 
		hasError = false;
	}
	
	public void setSuccess(Request request) {
		setSuccess();
		resultRequest.add(request);
	}
	
	public void setSuccess(ArrayList<Request> listRequest) {
		setSuccess();
		resultRequest = listRequest;
	}
}
