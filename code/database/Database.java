package database;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
	
private final Logger log = Logger.getLogger("Database");
	
	private Connection con;
    
    public Database(String host, int port, String database, String user, String password) {
            
            try {
                    
                    Class.forName("com.mysql.jdbc.Driver");
                    
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?" + "user=" + user + "&" + "password=" + password + "&useUnicode=yes&characterEncoding=UTF8");
                    
            } catch (ClassNotFoundException e) {
            	log.log(Level.SEVERE, "Database Library missing", e);
            } catch (SQLException e) {
            	log.log(Level.SEVERE, "Connection to mysql server not possible", e);
            }
    }
    
    public int insertWebsiteMetadaten(String url, String date, String filepath, String titel) {
    	
    	String sql = "INSERT INTO WebsiteMetadaten(Url, CrawlerDate, Filepath, Titel) " 
    			   + "VALUES(?, NOW(), ?, ?)";

		try {
			
			String key[] = {"Id"};
			
			PreparedStatement ps = con.prepareStatement(sql, key);
			ps.setString(1, url);
			ps.setString(2, filepath);
			ps.setString(3, titel);
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if(rs != null) {
			
				rs.next();
				
				return rs.getInt(1);
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Error insert WebsiteMetadaten", e);
		}
    	
    	return -1;
    }
	
	public void insertHeadMetadaten(int id, HashMap<String, String> headmetadaten) {
		
	}
	
	public void insertInhaltsMetadaten(int id, List<String> keywords) {
		
	}
	
	public void getMetaTags() {
		
		
	}

	public long getLastVisit(URL url){
		// TODO
		Date fakeDate = new Date();
		return fakeDate.getTime();
	}

}
