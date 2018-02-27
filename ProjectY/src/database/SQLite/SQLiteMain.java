package database.SQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.SQLite.Contract.DataSave;
import model.Request;

public class SQLiteMain {
	private static boolean initialized = false;
	
	private static void init() throws ClassNotFoundException, SQLException {
		if(!initialized) {
			Class.forName("org.sqlite.JDBC");
			createDatabase();
			initialized = true;
		}
	}
	
	private static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		return connection;
	}
	
	private static void createDatabase() throws SQLException {
		Connection connection = getConnection();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
		
			String sql = "CREATE TABLE IF NOT EXISTS '"+DataSave.TABLE_NAME+"' ("
				+ DataSave.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DataSave.COL_NAME + " TEXT NOT NULL,"
				+ DataSave.COL_EMAIL +" TEXT NOT NULL,"
				+ DataSave.COL_COMMENT +" TEXT NOT NULL,"
				+ DataSave.COL_CNPJ+" TEXT NOT NULL) ";
			statement.executeUpdate(sql);
		}finally {
			connection.close();
		}
	}
	
	public static int insert(String name, String email, String comment, String cnpj) {
		 int result = -1;
		try {
			init();
			Connection connection = getConnection();
			String sql = "INSERT INTO "+DataSave.TABLE_NAME+" ("
					+ DataSave.COL_NAME +", "+DataSave.COL_EMAIL+", "+DataSave.COL_COMMENT+", "
					+ DataSave.COL_CNPJ+")"
					+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, name);
	        statement.setString(2, email);
	        statement.setString(3, comment);
	        statement.setString(4, cnpj);
			statement.setQueryTimeout(30);
			statement.executeUpdate();
			
			ResultSet generatedKeys = statement.getGeneratedKeys();
			 if (generatedKeys.next()) {
				 result = generatedKeys.getInt(1);
	         }
			
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			return -1;
		}
		return result;
		
	}

	public static boolean update(int id, String name, String email, String comment, String cnpj) {
		try {
			init();
			Connection connection = getConnection();
			
		
			String sql = "UPDATE "+DataSave.TABLE_NAME+" SET "
					+ DataSave.COL_NAME +" = ?, "
					+ DataSave.COL_EMAIL + " = ?, "
					+ DataSave.COL_COMMENT + " = ?, "
					+ DataSave.COL_CNPJ + " = ? WHERE "
					+ DataSave.COL_ID + " = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
	        statement.setString(2, email);
	        statement.setString(3, comment);
	        statement.setString(4, cnpj);
	        statement.setInt(5, id);
			statement.setQueryTimeout(30);		
			
			statement.executeUpdate();
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			return false;
		}
		return true;
	}

	public static boolean delete(int id) {
		try {
			init();
			Connection connection = getConnection();
			
		
			String sql = "DELETE FROM "+DataSave.TABLE_NAME+" WHERE "
					+ DataSave.COL_ID + " = ?";
		
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.setQueryTimeout(30);
			statement.executeUpdate();
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			return false;
		}
		return true;
	}
	
	public static ArrayList<Request> getByID(int id) {
		ArrayList<Request> result = null;
		try {
			init();
			Connection connection = getConnection();
			
		
			String sql = "SELECT "
					+ DataSave.COL_ID+", "
					+ DataSave.COL_NAME+", "
					+ DataSave.COL_EMAIL+", "
					+ DataSave.COL_COMMENT+", "
					+ DataSave.COL_CNPJ
					+ " FROM "+DataSave.TABLE_NAME
					+ " WHERE "+DataSave.COL_ID+" = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			result = processResultSet(resultSet);
			
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
		return result;
	}
	
	public static ArrayList<Request> getAll() {
		ArrayList<Request> result = new ArrayList<Request>();
		try {
			init();
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
		
			String sql = "SELECT "
					+ DataSave.COL_ID+", "
					+ DataSave.COL_NAME+", "
					+ DataSave.COL_EMAIL+", "
					+ DataSave.COL_COMMENT+", "
					+ DataSave.COL_CNPJ
					+ " FROM "+DataSave.TABLE_NAME;
			ResultSet resultSet = statement.executeQuery(sql);
			result = processResultSet(resultSet);
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
		return result;
	}
	
	public static int Count() {
		int retorno = 0;
		try {
			init();
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
		
			String sql = "SELECT count(*)"
					+ " FROM "+DataSave.TABLE_NAME;
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				retorno = resultSet.getInt(1);
		      
		    }
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			return -1;
		}
		return retorno;
	}
	
	private static ArrayList<Request> processResultSet(ResultSet resultSet) throws SQLException{
		ArrayList<Request> result = new ArrayList<Request>();
		while(resultSet.next()){
	        Request newRequest = new Request();
			newRequest.id = resultSet.getInt(DataSave.COL_ID);
	        newRequest.name = resultSet.getString(DataSave.COL_NAME);
	        newRequest.email = resultSet.getString(DataSave.COL_EMAIL);
	        newRequest.comment = resultSet.getString(DataSave.COL_COMMENT);
	        newRequest.cnpj = resultSet.getString(DataSave.COL_CNPJ);
	        
	        result.add(newRequest);
	    }
		return result;
	
	}
}
