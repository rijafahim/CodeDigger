package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBConn {

	public ArrayList<String> ConnectDB_auto(String query) {
		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://localhost:1433;"
				+ "databaseName=StackOverflow;integratedSecurity=true;";
		ArrayList<String> qRes = new ArrayList<String>();

		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);

			query = "'" + query + "'";

			String SQL = "SELECT Title, Body FROM Posts where Id=";
			SQL += query;
			System.out.println(SQL);
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			while (rs.next()) {
				qRes.add(rs.getString(1));
				qRes.add(rs.getString(2));
				System.out.println(rs.getString(1));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		return qRes;
	}

	public ArrayList<String> ConnectDB(String query) {
		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://localhost:1433;"
				+ "databaseName=StackOverflow;integratedSecurity=true;";
		ArrayList<String> qRes = new ArrayList<String>();

		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);

			query = "'%" + query + "%'";

			String SQL = "SELECT TOP 10 Title, AcceptedAnswers FROM SortResult where Title like  ";
			SQL += query;

			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			while (rs.next()) {
				qRes.add(rs.getString(1));
				qRes.add(rs.getString(2));
				System.out.println(rs.getString(1));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		return qRes;
	}
	
	public ArrayList<String> ConnectDB_Title(String query) {
		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://localhost:1433;"
				+ "databaseName=StackOverflow;integratedSecurity=true;";
		ArrayList<String> qRes = new ArrayList<String>();

		// Declare the JDBC objects.
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);

			query = "'" + query + "'";

			String SQL = "SELECT Title FROM Posts where Id=";
			SQL += query;

			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			while (rs.next()) {
				qRes.add(rs.getString(1));
				//qRes.add(rs.getString(2));
				System.out.println(rs.getString(1));
				this.write_file(rs.getString(1));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
		return qRes;
	}
	
	public void write_file(String str)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "C:\\Users\\daany\\eclipse-workspace\\TestAnotherone\\ids_title.txt";

		try {

			String data = str;

			File file = new File(FILENAME);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(data);

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
}
