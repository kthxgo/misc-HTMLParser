package database;

import java.util.HashSet;

import common.TimeTaggedURL;

public class DatabasePool {
	
	private Database db;
	
	public DatabasePool() {
		
		db = new Database("78.46.189.61", 3306, "webarchiv", "webarchiv", "t8TPuSZwwDQGtNuG");
	}
	
	public Database getDatabaseConnection() {
		
		return db;
	}

	public HashSet<TimeTaggedURL> getVisitedLinks() {
		// TODO Auto-generated method stub
		return null;
	}

}
