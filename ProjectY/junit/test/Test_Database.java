package test;

import org.junit.Test;

import database.Database;
import database.SQLite.TableDataSave;
import junit.framework.TestCase;
import model.Request;
import model.Response;

public class Test_Database extends TestCase {
	
	@Test
	public void test_insert() {

		Request valid = new Request(0,"X","fulano@acme.com","X","X");
		Request withoutName = new Request(0,"","fulano@acme.com","X","X");
		Request withoutEmail = new Request(0,"X","","X","X");
		Request invalidEmail = new Request(0,"X","xxx","X","X");
		Request withoutComment = new Request(0,"X","fulano@acme.com","","X");
		Request withoutCNPJ = new Request(0,"X","fulano@acme.com","X","");
		
		Response response = Database.insert(valid);
		assertEquals(Response.SUCCESS, response.result);
		assertFalse(response.hasError);
		
		response = Database.insert(withoutName);
		assertEquals(Database.ERROR_EMPTY_NAME, response.result);
		assertTrue(response.hasError);
		
		response = Database.insert(withoutEmail);
		assertEquals(Database.ERROR_EMPTY_EMAIL, response.result);
		assertTrue(response.hasError);
		
		response = Database.insert(invalidEmail);
		assertEquals(Database.ERROR_EMPTY_EMAIL, response.result);
		assertTrue(response.hasError);
		
		response = Database.insert(withoutComment);
		assertEquals(Database.ERROR_EMPTY_COMMENT, response.result);
		assertTrue(response.hasError);
		
		response = Database.insert(withoutCNPJ);
		assertEquals(Database.ERROR_EMPTY_CNPJ, response.result);
		assertTrue(response.hasError);
	}
	
	
	@Test
	public void test_getAll() {
		Response response = Database.getAll();
		int count = TableDataSave.count();
		assertEquals(count, response.resultRequest.size());
	}
	
	@Test
	public void test_getById() {
		Response responseAll = Database.getAll();
		for(Request request : responseAll.resultRequest) {
			int id = request.id;
			Response responseById = Database.getByID(id);
			for(Request requestByID : responseById.resultRequest) {
				assertEquals(id, requestByID.id);
				assertFalse(responseById.hasError);
			}
		}
		
		int id = -1;
		Response responseById = Database.getByID(id);
		assertEquals(Database.ERROR_QUERY_IDNOTFOUND+id, responseById.result);
		assertTrue(responseById.hasError);
	}
	
	@Test
	public void test_update() {
		Request base = new Request(0,"Ale","fulano@acme.com","X","X");
		Response response = Database.insert(base);
		int id = 1;
		for(Request request : response.resultRequest) {
			id = request.id;
			
		}
		
		Request change = new Request(id,"Alex","fulano@acme.com","X","X");
		Database.update(change);
		
		Response responseById = Database.getByID(id);
		for(Request requestByID : responseById.resultRequest) {
			assertEquals(id, requestByID.id);
			assertEquals("Alex",requestByID.name);
		}
		
	}
	
	@Test
	public void test_delete() {
		Request base = new Request(0,"xxsdsd","fulano@acme.com","X","X");
		Response response = Database.insert(base);
		int id = 1;
		for(Request request : response.resultRequest) {
			id = request.id;
			
		}
		
		int count = TableDataSave.count();
		
		Response delete = Database.delete(id);
		assertFalse(delete.hasError);
		
		int newcount = TableDataSave.count();
		assertEquals(count-1,newcount);
		
		
		Response responseById = Database.getByID(id);
		assertEquals(Database.ERROR_QUERY_IDNOTFOUND+id, responseById.result);
		assertTrue(responseById.hasError);
		
	}
}
