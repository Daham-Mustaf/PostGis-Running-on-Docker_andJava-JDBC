
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.locationtech.jts.geom.LineString;

public class LineStrings {
	private int srid;
	private String name;
	private LineString geometry;

	public LineStrings() {

	}

	public LineStrings(int srid, String name, LineString geometry) {

		this.srid = srid;
		this.name = name;
		this.geometry = geometry;
	}

	public int getSrid() {
		return srid;
	}

	public void setSrid(int srid) {
		this.srid = srid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LineString getGeometry() {
		return geometry;
	}

	public void setGeometry(LineString geometry) {
		this.geometry = geometry;
	}

	@Override
	public String toString() {
		return "LineStrings [srid=" + srid + ", name=" + name + ", geometry=" + geometry + "]";
	}

	public void creatLineStringTable() {
		Connection c = null;
		Statement stmt = null;
		String CreateSql = null;

		try {

			Class.forName("org.postgresql.Driver");

			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postGis", "postgres", "postgres");
			System.out.println("Database Connected ..");

			stmt = c.createStatement();
			CreateSql = "CREATE TABLE linestrings ( id serial PRIMARY KEY, name varchar(20), linestrings geometry(LINESTRING));";
			stmt.executeUpdate(CreateSql);
			stmt.close();
			c.close();
		}

		catch (Exception e)

		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);

		}

		System.out.println("Table Created successfully");

	}

	public void creatPostgresQuery(String qury) {
		Connection c = null;
		Statement stmt = null;
		String CreateSql = null;

		try {

			Class.forName("org.postgresql.Driver");

			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postGis", "postgres", "postgres");
			System.out.println("Database Connected ..");

			stmt = c.createStatement();
			CreateSql = qury;
			stmt.executeUpdate(CreateSql);
			stmt.close();
			c.close();
		}

		catch (Exception e)

		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);

		}

		System.out.println("Query excuted successfully");

	}

	public static void main(String[] args) {

		LineStrings lineStrings = new LineStrings();
		//lineStrings.creatLineStringTable();
		String query= "INSERT INTO linestrings (name, linestrings) VALUES ('Open', ST_GeomFromText('LINESTRING(0 0, 1 1, 1 -1)')),('Closed', ST_GeomFromText('LINESTRING(0 0, 1 1, 1 -1, 0 0)'));";
		lineStrings.creatPostgresQuery(query);

	}

}
