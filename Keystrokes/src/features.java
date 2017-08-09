
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User_1
 */
public class features {
    
    /**
     *
     */
     
    public static String getFeatures(){
      ConnectDatabase con = new ConnectDatabase(); 
        try {
             con.connectToDataBase();
             
             String query = "select * from keystrokes.features";
             con.statement = con.connect.createStatement();
           con.resultSet = con.statement.executeQuery(query);
           
           StringBuilder string = new StringBuilder();
      
           while(con.resultSet.next())
           {
               string.append(con.resultSet.getString("inputkeys"));
               string.append(",");
               string.append(con.resultSet.getLong("dwell_time"));
               string.append(",");
               string.append(con.resultSet.getLong("down_flight_time"));
               string.append(",");
               string.append(con.resultSet.getLong("up_flight_time"));
               string.append(",");
               string.append(con.resultSet.getLong("dwell_time_mean"));
               string.append(",");
               string.append(con.resultSet.getLong("down_flight_time_mean"));
               string.append(",");
               string.append(con.resultSet.getLong("up_flight_time_mean"));
               string.append(",");
               string.append(con.resultSet.getLong("dwell_time_stdeviation"));
               string.append(",");
               string.append(con.resultSet.getLong("down_flight_time_stdeviation"));
               string.append(",");
               string.append(con.resultSet.getLong("up_flight_time_stdeviation"));
               string.append(",");
               string.append(con.resultSet.getInt("number_of_backspace"));
               string.append("\n");
               
           }
           return string.toString();
             
         } catch (Exception ex) {
             Logger.getLogger(features.class.getName()).log(Level.SEVERE, null, ex);
         }
   return "";
            }
    
    public static void main(String[] args) throws Exception  {
		
           FileWriter fw = new FileWriter("features.csv",true);
        PrintWriter writer = new PrintWriter(fw);
        writer.print(features.getFeatures());
        writer.close();
	}
    
}
