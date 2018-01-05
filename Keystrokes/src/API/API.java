package API;


import Login.LoginDialog;
import KeystrokesInterface.KeystrokesForm;
import DatabaseConnection.ConnectDatabase;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

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

    private String filePath;
    private String hashValue;
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private static ArrayList<String> keystrokes = new ArrayList<String>();
    private static ArrayList<Long> keyreleased = new ArrayList<Long>();
    private static ArrayList<Long> keypressed = new ArrayList<Long>();
    private static ArrayList<Long> downflighttime = new ArrayList<Long>();
    private static ArrayList<Long> upflighttime = new ArrayList<Long>();
    private static ArrayList<Long> dwelltime = new ArrayList<Long>();
    private static long releasedtime;
    private static long pressedtime;
    private static int count = 0;
    private static int NumOfKeys = 0;
    private static long dwellmean;
    private static long downflightmean;
    private static long upflightmean;
    private static long standardDeviation;
    private static String keyp;
    private static String keyr;
    private static long downflightstandardDeviation;
    private static long upflightstandardDeviation;

    ArrayList<String> keypressedholder = new ArrayList<String>();
    ArrayList<String> keyreleasedholder = new ArrayList<String>();
    ArrayList<Long> keypressedtimestamp = new ArrayList<Long>();
    ArrayList<Long> keyreleasedtimestamp = new ArrayList<Long>();
    public StringBuffer sb = new StringBuffer();
    
   

    public int getUser() {
        return 0;
    }

    public void createUser() {

    }

    public String getFile() {
        ConnectDatabase con = new ConnectDatabase();
        ArrayList<String>filename = new ArrayList<String>();
        String file = " ";
        try {
            con.connectToDataBase();

            String query = "select filepath,filename from duplicatefiles";

            con.statement = con.connect.createStatement();
            con.resultSet = con.statement.executeQuery(query);
            int count = 1;
            StringBuffer sb = new StringBuffer();

            while (con.resultSet.next()) {
                FileOutputStream output = new FileOutputStream(new File("file" + count + ".csv"));
                Blob fileBlob = con.resultSet.getBlob("filePath");
                filename.add(con.resultSet.getString("filename"));
                InputStream input = con.resultSet.getBinaryStream("filename");
                //System.out.println("File name:" + filename);
                byte[] buffer = new byte[1024];
                /*while (input.read(buffer) > 0) {
                    output.write(buffer);
                    System.out.println(new String(buffer));
                }*/

                count++;
                output.close();

            }
          
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        for(int i = 0; i < filename.size(); i++)
        {
          file = filename.get(i) + "\n";
        }
        return file;
    }
    public boolean saveFeatures(ArrayList<String> keyaction, ArrayList<String> keycode, ArrayList<Long> timestamp, String sessionid) throws ClassNotFoundException, SQLException, IOException {
        for (int k = 0; k < timestamp.size(); k++) {
            if (k + 1 < timestamp.size()) {
                dwelltime.add(timestamp.get(k + 1) - timestamp.get(k));
            }
        }
        System.out.println("Dwelltime:" + dwelltime);

        //add the key pressed timestamps into array
        for (int i = 0; i < timestamp.size() - 1; i++) {
            if (i % 2 == 0) {
                keypressed.add(timestamp.get(i));
            }
            //}
            //}
        }

        //add the key released timestamps into array
        for (int i = 1; i < timestamp.size() + 1; i++) {
            if (i % 2 == 1) {
                keyreleased.add(timestamp.get(i));
            }

        }
        System.out.println(keypressed);
        //System.out.println(keyreleased);

        //calculate dwelltime
        //add the keystrokes into array
        for (int i = 0; i < keycode.size(); i++) {
            keystrokes.add(keycode.get(i));
        }
        System.out.println(keystrokes);
        long keyTime = 0;
        long keyTime2 = 0;
        for (int i = 0; i < keyreleased.size(); i++) {
            if (i + 1 < keypressed.size()) {
                System.out.println("Size: " + keyreleased.size());
                keyTime = keyreleased.get(i + 1) - keyreleased.get(i);
                upflighttime.add(keyTime);
            }

        }
        for (int i = 0; i < keypressed.size(); i++) {
            if (i + 1 < keypressed.size()) {
                System.out.println(keypressed);
                keyTime2 = keypressed.get(i + 1) - keypressed.get(i);
                downflighttime.add(keyTime2);
            }

        }
        ArrayList<Long> flightTime = new ArrayList<Long>();
       

         //calculateDwellTimeMean();
        //calculateDownFlightTimeMean();
        //calculateUpFlightTimeMean();
        //calculateStandardDeviation();
        //calculateStandardDeviationDownFlight();
        //calculateStandardDeviationUpFlight();
        /*Class.forName("com.mysql.jdbc.Driver");
         // Setup the connection with the DB
         connect = DriverManager
         .getConnection("jdbc:mysql://localhost/keystrokes?"
         + "user=root&password=root");
               
         String query = " insert into keystrokes.features (inputkeys, dwell_time, down_flight_time,up_flight_time,dwell_time_mean,down_flight_time_mean,up_flight_time_mean,dwell_time_stdeviation,down_flight_time_stdeviation,up_flight_time_stdeviation,number_of_backspace,classifier)"
         + " values (?, ?, ?,?,?,?,?,?,?,?,?,?)";
           
         preparedStatement = connect.prepareStatement(query);*/
        for (int i = 0; i < upflighttime.size(); i++) {
            flightTime.add(downflighttime.get(i));
            flightTime.add(upflighttime.get(i));

        }
        for (int i = 0; i < keystrokes.size(); i++) {
            //writer1.append(keystrokes.get(i) + "," + dwelltime.get(0));
                   //preparedStatement.setString(1, keystrokes.get(i));
            //preparedStatement.setLong(2, dwelltime.get(0));
            for (int j = 0; j < downflighttime.size(); j++) {

                //writer1.append(downflighttime.get(j) + "," + upflighttime.get(j) + "," + calculateDwellTimeMean(dwelltime) + "," + calculateDownFlightTimeMean(downflighttime) + "," + calculateUpFlightTimeMean());
                        //preparedStatement.setLong(3, downflighttime.get(j));
                //preparedStatement.setLong(4, upflighttime.get(j));
                //preparedStatement.setLong(5, calculateDwellTimeMean(dwelltime));
                //System.out.println(calculateDwellTimeMean(dwelltime));
                //preparedStatement.setLong(6, calculateDownFlightTimeMean(downflighttime));
                //preparedStatement.setLong(7, calculateUpFlightTimeMean());
                //preparedStatement.setLong(8, calculateStandardDeviation());
                //preparedStatement.setLong(9, calculateStandardDeviationDownFlight());
                //preparedStatement.setLong(10, calculateStandardDeviationUpFlight());
            }

                   //preparedStatement.setInt(11, count);
            //preparedStatement.setString(12,sessionid.get(i));
            //preparedStatement.executeUpdate();  
        }

        return true;
    }

    public static void Convert(String sourcepath, String destpath) throws Exception {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(sourcepath));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(destpath));
        //saver.setDestination(new File(destpath));
        saver.writeBatch();
    }
    public boolean getFeatures() throws FileNotFoundException, IOException, Exception {
        //FileWriter fw = new FileWriter("Accumulated.csv", true);
        PrintWriter writer1 = new PrintWriter("Accumulated.csv");
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader("log_file.csv"));

        String line = "";

        String[] columns = null;
        while ((line = reader.readLine()) != null) {
            columns = line.split(",");
            keypressedholder.add(columns[0]);
            keypressedtimestamp.add(Long.parseLong(columns[1]));
            keyreleasedholder.add(columns[2]);
            keyreleasedtimestamp.add(Long.parseLong(columns[3]));
        }
        System.out.println(keypressedholder.toString());
        System.out.println(keypressedtimestamp.toString());
        System.out.println(keyreleasedholder.toString());
        System.out.println(keyreleasedtimestamp.toString());
        
         for (int k = 0; k < keypressedtimestamp.size(); k++) {
           
                dwelltime.add(keyreleasedtimestamp.get(k) - keypressedtimestamp.get(k));
            
        }
        System.out.println("Dwelltime:" + dwelltime);
        
        for(int i = 0; i < keypressedtimestamp.size();i++)
        {
            if(i + 1 < keypressedtimestamp.size())
            {
                downflighttime.add(keypressedtimestamp.get(i + 1) - keypressedtimestamp.get(i));
            }
        }

        System.out.println("down flight time: " + downflighttime);
        
        for(int i = 0; i < keyreleasedtimestamp.size();i++)
        {
            if(i + 1 < keyreleasedtimestamp.size())
            {
                upflighttime.add(keyreleasedtimestamp.get(i + 1) - keyreleasedtimestamp.get(i));
            }
        }
      
        System.out.println("up flight time: " + upflighttime);
        
        System.out.println("Dwell time mean" + calculateDwellTimeMean(dwelltime));
        System.out.println("Down time mean" + calculateDownFlightTimeMean(downflighttime));
        System.out.println("Up time mean" + calculateUpFlightTimeMean(upflighttime));
        System.out.println("Standard deviation dwell" + calculateStandardDeviationDwell(dwelltime));
        System.out.println("Standard deviation down" + calculateStandardDeviationDownFlight(downflighttime));
        System.out.println("Standard deviation up" + calculateStandardDeviationUpFlight(upflighttime));
        
      
        writer1.println("Key , Dwelltime, Dwelltimemean, Dwelltimestandarddeviation, Upflighttime, Upflighttimemean , Upflighttimestandarddeviaton,Downflighttime, Downflihttimemean, Downflighttimestandarddeviation, user");
        for (int i = 0; i < keypressedholder.size() - 1; i++) {

            writer1.println(keypressedholder.get(i) + "," + dwelltime.get(i) + "," + calculateDwellTimeMean(dwelltime) + "," + calculateStandardDeviationDwell(dwelltime) + "," + upflighttime.get(i) + "," + calculateUpFlightTimeMean(upflighttime) + "," + calculateStandardDeviationUpFlight(upflighttime) + "," + downflighttime.get(i)+ "," + calculateDownFlightTimeMean(downflighttime) + "," + calculateStandardDeviationDownFlight(downflighttime));
        }
        
        writer1.close();
        
        Convert("accumulated.csv", "accumulated.arff");
        return true;
    }

    public static long calculateDwellTimeMean(ArrayList<Long> dwell) {
        long total = 0;
        for (Long d : dwell) {
            total += d;
        }
        dwellmean = total / dwell.size();

        /*System.out.println("Number of keys = " + count);
         System.out.println("Mean = " + mean);*/
        //writer.println(",Down flight time = ," + downflighttime); 
        return dwellmean;
    }

    public static long calculateDownFlightTimeMean(ArrayList<Long> flight) {
        long total = 0;
        for (Long d : flight) {
            total += d;
        }
        downflightmean = total / flight.size();
        return downflightmean;
    }

    public static long calculateUpFlightTimeMean(ArrayList<Long> flight) {
        long total = 0;
        for (Long d : flight) {
            total += d;
        }
        upflightmean = total / flight.size();
            //writer.print("," + upflightmean);
        //System.out.println("Up flight time mean" + upflightmean);

        return upflightmean;

    }

    public static long calculateStandardDeviationDwell(ArrayList<Long> dwelltime) {
        long totalDwelltime = 0;
        long mean2 = 0;
        for (Long d : dwelltime) {
            totalDwelltime += Math.pow(d - calculateDwellTimeMean(dwelltime), 2);
        }
        mean2 = totalDwelltime / dwelltime.size();
        standardDeviation = (long) Math.sqrt(mean2);
        return standardDeviation;
    }

    public static long calculateStandardDeviationDownFlight(ArrayList<Long> downflighttime) {
        long totalDwelltime = 0;
        long mean2 = 0;
        for (Long d : downflighttime) {
            totalDwelltime += Math.pow(d - calculateDownFlightTimeMean(downflighttime), 2);
        }
        mean2 = totalDwelltime / downflighttime.size();
        downflightstandardDeviation = (long) Math.sqrt(mean2);
        return downflightstandardDeviation;
    }

    public static long calculateStandardDeviationUpFlight(ArrayList<Long> upflighttime) {
        long totalDwelltime = 0;
        long mean2 = 0;
        for (Long d : upflighttime) {
            totalDwelltime += Math.pow(d - calculateUpFlightTimeMean(upflighttime), 2);
        }
        mean2 = totalDwelltime / upflighttime.size();
        upflightstandardDeviation = (long) Math.sqrt(mean2);

        return upflightstandardDeviation;
    }

    public void saveHashvalue(FileInputStream fis, File file) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
        try {
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
           
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println("File name : " + file.toString());
            System.out.println("Hex format : " + sb.toString());
            FileInputStream input = new FileInputStream(file);
            String filename = file.getName();
            
            System.out.println("File name2:" + filename);
            fis.close();
            reader.close();
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/keystrokes?"
                            + "user=root&password=root");

            String query = " insert into keystrokes.originalfiles (filePath, hashvalue, userid,filename)"
                    + " values (?, ?, ?,?)";
            preparedStatement = connect
                    .prepareStatement(query);
            preparedStatement.setBlob(1, input);
            preparedStatement.setString(2, sb.toString());
            preparedStatement.setInt(3, 2);
            preparedStatement.setString(4,filename);
            preparedStatement.executeUpdate();
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as fileid from keystrokes.originalfiles");

            if (resultSet.next()) {
                query = " insert into keystrokes.duplicatefiles (filePath, hashvalue, fileid,filename)"
                        + " values (?, ?, ?,?)";
                preparedStatement = connect
                        .prepareStatement(query);
                preparedStatement.setBlob(1, new FileInputStream(file));
                preparedStatement.setString(2, sb.toString());
                preparedStatement.setInt(3, resultSet.getInt("fileid"));
                 preparedStatement.setString(4, filename);
                preparedStatement.executeUpdate();
            }
            input.close();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeystrokesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verifyChecksum(String filename) throws NoSuchAlgorithmException, IOException, Exception {
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
            String status = "";
            System.out.println("Checksum Successful! ");
            String filenameQuery = "select * from duplicatefiles";
            //String query = "select * from duplicatefiles d left join originalfiles o on d.fileid = o.fileid where o.hashvalue = d.hashvalue and o.fileid = ?";
            
            con.preparedStatement = con.connect.prepareStatement(filenameQuery);
            
            con.resultSet = con.preparedStatement.executeQuery();
            if (con.resultSet.next()) {
                String filenameResults = con.resultSet.getString("filename");
                System.out.println("Im here");
                System.out.println("Checksum Successful! " + filenameResults);
                
                
                if(filenameResults.equals(filename))
                {
                    //String query = "select * from duplicatefiles d left join originalfiles o on d.fileid = o.fileid where o.hashvalue = d.hashvalue and o.fileid = ?";
                    //System.out.println("NAME: " + filename);
                    System.out.println("Im here inside");
                    String filenameQuery2 = "select * from originalfiles";
                    con.preparedStatement = con.connect.prepareStatement(filenameQuery2);
                    con.resultSet = con.preparedStatement.executeQuery();
                    
                    if (con.resultSet.next()) {
                        String filenameResults2 = con.resultSet.getString("filename");
                         
                        String query = "select * from duplicatefiles d left join originalfiles o on d.fileid = o.fileid where o.hashvalue = d.hashvalue and o.filename= filenameResults2";
                        
                        con.preparedStatement = con.connect.prepareStatement(query);
                        
                        //con.resultSet = con.preparedStatement.executeQuery();
                        
                       
                        
                             String hashvalue = con.resultSet.getString("hashvalue");
                             System.out.println("HASH VALUE: " + hashvalue);
                               
                        
                        System.out.println("Checksum Successful! " + filenameResults2);
                        
                        
                    }
                }
                return true;
            }
        } catch (Exception ex) {

        } finally {
            con.close();
        }
        return false;

    }

    public String getFilePath() {
        return "";
    }

    public static void main(String[] args) throws Exception {

        //API.verifyChecksum(1);
        //API.getFile();
    }

}
