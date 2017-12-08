package Login;


import DatabaseConnection.ConnectDatabase;
import java.sql.PreparedStatement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User_1
 */
public class Login {
    
    
 
    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        ConnectDatabase con = new ConnectDatabase();
        String query = "select * from keystrokes.users where classifier=? and password=?";
        String insert= "insert into keystrokes.users(classifier,password)"
                       + " values (?, ?)";
        try {
            con.connectToDataBase();           
            con.preparedStatement = con.connect.prepareStatement(query);
            con.preparedStatement.setString(1, username);
            con.preparedStatement.setString(2, password);
            con.resultSet = con.preparedStatement.executeQuery();
            if(con.resultSet.next())
            {
                //TRUE iff the query founds any corresponding data
                return true;
            }
            else
            {
               
                return false;
            }
        } catch (Exception ex) {
            
            return false;

        }
        
        
        
    }
    
    
}
