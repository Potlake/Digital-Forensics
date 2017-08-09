/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.ArrayList;
/**
 *
 * @author Potlake
 */
public class GlobalKeyListenerExample implements NativeKeyListener {
    
    private static PrintWriter writer;
    private static API api;
    private static ArrayList <String> keystrokes = new ArrayList<String>();
    private static ArrayList <Long> keyreleased = new ArrayList<Long>();
    private static ArrayList <Long> keypressed = new ArrayList<Long>();
    private static ArrayList <Long> downflighttime = new ArrayList<Long>();
    private static ArrayList <Long> upflighttime = new ArrayList<Long>();
    private static ArrayList <Long> dwelltime = new ArrayList<Long>();
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
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
  
    
    
public void nativeKeyPressed(NativeKeyEvent e) {
        try { 
            FileWriter fw = new FileWriter("log_file.csv",true);
            writer = new PrintWriter(fw);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
        }
		writer.print("KeyPressed ,"  + NativeKeyEvent.getKeyText(e.getKeyCode())+  ","+ System.currentTimeMillis());
                //System.out.println("Key Pressed\t" + System.currentTimeMillis() + "\t" + NativeKeyEvent.getKeyText(e.getKeyCode()));
                writer.close();
                pressedtime = System.currentTimeMillis();
                keypressed.add(System.currentTimeMillis());
                keyp = NativeKeyEvent.getKeyText(e.getKeyCode());

                //System.out.println(keypressed);
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    System.out.println("Stopped");
                    try {
                        File file = new File("log_file.csv");
               
               if(file != null && file.exists())
               {
                   try {
                       FileInputStream fis = new FileInputStream(file);
                     
                       api = new API();
                        api.saveHashvalue(fis, file);
                   } catch (FileNotFoundException ex) {
                       Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (IOException ex) {
                       Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                   }        catch (SQLException ex) {
                                Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                            }
               
               }
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ex) {
                        Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
           try
           {
               NativeKeyEvent f;
               try
               {
                   FileWriter fw = new FileWriter("log_file.csv",true);
                   writer = new PrintWriter(fw);
                   
            
               }catch(FileNotFoundException ex)
               {
                   Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
               } catch (IOException ex) {
                   Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
               }
               writer.println("Key Released , "  + NativeKeyEvent.getKeyText(e.getKeyCode())+ ","+ System.currentTimeMillis());
               System.out.println("KeyReleased " + System.currentTimeMillis() + ","+ NativeKeyEvent.getKeyText(e.getKeyCode()));
               
               releasedtime = System.currentTimeMillis();
               keystrokes.add(NativeKeyEvent.getKeyText(e.getKeyCode()));
               NumOfKeys++;
                System.out.println("Released time: " + releasedtime);
                System.out.println("Pressed time: " + pressedtime);
                System.out.println("Difference: " + (releasedtime - pressedtime));
               keyreleased.add(System.currentTimeMillis()) ;
               keyr = NativeKeyEvent.getKeyText(e.getKeyCode());
            
               System.out.println("keystrokes " + keystrokes);
               //System.out.println("Released time " + keyreleased);
             
               dwelltime.add(releasedtime - pressedtime);
               
               System.out.println("Dwell time = " + dwelltime.toString());
               
               long keyTime = 0;
               long keyTime2 = 0;
               for (int i = 0; i < keystrokes.size()-1; i++) {
                   if(keystrokes.size() <= 1)
                   {
                       
                   }
                   else{
                       keyTime  = keyreleased.get(i + 1) - keyreleased.get(i);
                       keyTime2  = keypressed.get(i + 1) - keypressed.get(i);
                   }
               }
               downflighttime.add(keyTime);
               upflighttime.add(keyTime2);
               System.out.println("Number of keys = " + NumOfKeys);
               System.out.println("Down flight time = " + downflighttime.toString());
               
               System.out.println("Up flight time = " + upflighttime.toString());
               
               writer.close();
           
               calculateDwellTimeMean();
               calculateDownFlightTimeMean();
               calculateUpFlightTimeMean();
               calculateStandardDeviation();
               calculateStandardDeviationDownFlight();
               calculateStandardDeviationUpFlight();
               if (e.getKeyCode() == NativeKeyEvent.VC_BACKSPACE) {
                   count++;
               }
               System.out.println("Number of backspace = " + count);
               
               
               Class.forName("com.mysql.jdbc.Driver");
               // Setup the connection with the DB
               connect = DriverManager
                       .getConnection("jdbc:mysql://localhost/keystrokes?"
                               + "user=root&password=");
               
               String query = " insert into keystrokes.features (inputkeys, dwell_time, down_flight_time,up_flight_time,dwell_time_mean,down_flight_time_mean,up_flight_time_mean,dwell_time_stdeviation,down_flight_time_stdeviation,up_flight_time_stdeviation,number_of_backspace,userid)"
                       + " values (?, ?, ?,?,?,?,?,?,?,?,?,?)";
                
               preparedStatement = connect
                       .prepareStatement(query);
               preparedStatement.setString(1,keyr);
               for(Long d : dwelltime)
               {
                   
                    preparedStatement.setLong(2, d);
               }
               for(Long d: downflighttime)
               {
                    preparedStatement.setLong(3,d);
               }
               
               for(Long d: upflighttime)
               {
                    preparedStatement.setLong(4,d);
               }
               
               for(Long d: upflighttime)
               {
                    preparedStatement.setLong(4,d);
               }
               preparedStatement.setLong(5,calculateDwellTimeMean());
               preparedStatement.setLong(6,calculateDownFlightTimeMean());
               preparedStatement.setLong(7,calculateUpFlightTimeMean());
               preparedStatement.setLong(8,calculateStandardDeviation());
               preparedStatement.setLong(9,calculateStandardDeviationDownFlight());
               preparedStatement.setLong(10,calculateStandardDeviationUpFlight());
               preparedStatement.setInt(11,count);
               preparedStatement.setInt(12,2);
               preparedStatement.executeUpdate();
               
           }catch(ClassNotFoundException ex)
           {
                Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex) {
            Logger.getLogger(GlobalKeyListenerExample.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
		//writer.println(", " + System.currentTimeMillis() + ","+ NativeKeyEvent.getKeyText(e.getKeyCode()));
                //System.out.println("KeyReleased " + System.currentTimeMillis() + ","+ NativeKeyEvent.getKeyText(e.getKeyCode()));
                 
	}
        

	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + "\t"+ NativeKeyEvent.getKeyText(e.getKeyCode()));
	}
        
        public static long calculateDwellTimeMean()
        {
            long total = 0; 
            for (Long d: dwelltime) {
                total += d; 
            }
            dwellmean = total/dwelltime.size();
            
            
            
                 /*System.out.println("Number of keys = " + count);
                 System.out.println("Mean = " + mean);*/
                //writer.println(",Down flight time = ," + downflighttime); 
          return dwellmean;
        }
        
        public static long calculateDownFlightTimeMean()
        {
            long total = 0; 
            for (Long d: downflighttime) {
                total += d; 
            }
            downflightmean = total/downflighttime.size();
            return downflightmean;
        }
        
        public static long calculateUpFlightTimeMean()
        {
            long total = 0; 
            for (Long d: upflighttime) {
                total += d; 
            }
            upflightmean = total/upflighttime.size();
            //writer.print("," + upflightmean);
            //System.out.println("Up flight time mean" + upflightmean);
            
            return upflightmean;
                    
        }
        
        public static long calculateStandardDeviation()
        {
            long totalDwelltime = 0;
            long mean2 = 0;
            for (Long d : dwelltime) {
                totalDwelltime += Math.pow(d - dwellmean, 2);
            }
            mean2 = totalDwelltime/dwelltime.size();
            standardDeviation = (long) Math.sqrt(mean2);
            return standardDeviation;
        }
        
        public static long calculateStandardDeviationDownFlight()
        {
            long totalDwelltime = 0;
            long mean2 = 0;
            for (Long d : downflighttime) {
                totalDwelltime += Math.pow(d - downflightmean, 2);
            }
            mean2 = totalDwelltime/downflighttime.size();
            downflightstandardDeviation = (long) Math.sqrt(mean2);
            return downflightstandardDeviation;
        }
        public static long calculateStandardDeviationUpFlight()
        {
            long totalDwelltime = 0;
            long mean2 = 0;
            for (Long d : upflighttime) {
                totalDwelltime += Math.pow(d - upflightmean, 2);
            }
            mean2 = totalDwelltime/upflighttime.size();
            upflightstandardDeviation = (long) Math.sqrt(mean2);
            
            return upflightstandardDeviation;
        }
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
            try {
                    GlobalScreen.registerNativeHook();
                    // Clear previous logging configurations.
                    LogManager.getLogManager().reset();

                    // Get the logger for "org.jnativehook" and set the level to off.
                    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
                    logger.setLevel(Level.OFF);
                    
                          
                    
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExample());
                
                
                
                
	}
}
