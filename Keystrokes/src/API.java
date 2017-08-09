
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import static java.sql.JDBCType.BLOB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.BLOB;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Potlake
 */
public class API {
    
    private String filePath ;
    private String hashValue;
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
  
    public void connectToDatabase()
    {
        
    }
            
    public int getUser()
    {
       return 0;
    }
    
    public void createUser()
    {
        
    }
    
    public static String getFile()
    {
       ConnectDatabase con = new ConnectDatabase(); 
        try {
             con.connectToDataBase();
             
             String query = "select * from duplicateFiles";
      
             con.statement = con.connect.createStatement();
           con.resultSet = con.statement.executeQuery(query);
           int count = 1;
           StringBuffer sb = new StringBuffer();
         
           while(con.resultSet.next())
           {
               Blob fileBlob = con.resultSet.getBlob("filePath");
               
                byte[] fileBytes = fileBlob.getBytes(1, (int) fileBlob.length());
                String filename = "logs/file" + count;
                File files = new File(filename);
                /*if(!files.exists())
                {
                    files.createNewFile();
                }*/
               FileOutputStream fos = new FileOutputStream(filename);
               fos.write(fileBytes);
               fos.close();;
               
                
       
                count++;
               
               
           }
           return "logs/";
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
       
        return "";
    }
    public void saveHashvalue(FileInputStream fis,File file) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException
    {
        try{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
         MessageDigest md = MessageDigest.getInstance("SHA-256");
        fis = new FileInputStream(file);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println("File name : " + file.toString());
        System.out.println("Hex format : " + sb.toString());
        
        Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/keystrokes?"
                            + "user=root&password=");

        String query = " insert into keystrokes.originalFiles (filePath, hashvalue, userid)"
        + " values (?, ?, ?)";
        preparedStatement = connect
                    .prepareStatement(query);
        preparedStatement.setString(1, file.toString());
        preparedStatement.setString(2,sb.toString());
        preparedStatement.setInt(3,2);
        preparedStatement.executeUpdate();
       statement = connect.createStatement();
       resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as fileid from keystrokes.originalFiles");
       
       if(resultSet.next())
       {
         query = " insert into keystrokes.duplicateFiles (filePath, hashvalue, fileid)"
        + " values (?, ?, ?)";
        preparedStatement = connect
                    .prepareStatement(query);
        preparedStatement.setString(1, file.toString());
        preparedStatement.setString(2,sb.toString());
        preparedStatement.setInt(3,resultSet.getInt("fileid"));
        preparedStatement.executeUpdate();
       }
        }
        catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(KeystrokesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static boolean verifyChecksum(int file) throws NoSuchAlgorithmException, IOException, Exception
    {
        /*MessageDigest sha256 = MessageDigest.getInstance("SHA256");
        FileInputStream fis = new FileInputStream(file);
  
        byte[] data = new byte[1024];
        int read = 0; 
        while ((read = fis.read(data)) != -1) {
            sha256.update(data, 0, read);
        };
        byte[] hashBytes = sha256.digest();
  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        String fileHash = sb.toString();
         
        return fileHash.equals(testChecksum);*/
       ConnectDatabase con = new ConnectDatabase(); 
        try {
             con.connectToDataBase();
             
             String query = "select * from duplicateFiles d left join originalFiles o on d.fileid = o.fileid where o.hashvalue = d.hashvalue and o.fileid = ?";
      
             con.preparedStatement = con.connect.prepareStatement(query);
             con.preparedStatement.setInt(1, file);
           con.resultSet = con.preparedStatement.executeQuery();
           if(con.resultSet.next())
           {
               System.out.println("hello");
               return true;
           }
        }
        catch(Exception ex)
        {
            
        }
        finally
        {
            con.close();
        }
        return false;
        
    }
     
    public String getFilePath()
    {
        return "";
    }
    public static void main(String[] args) throws Exception  {
		
          API.verifyChecksum(2);
          API.getFile();
	}
    
}
