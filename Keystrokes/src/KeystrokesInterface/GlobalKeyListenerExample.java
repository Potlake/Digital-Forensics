package KeystrokesInterface;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import API.API;
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
    private static String keyp;
    private static String keyr;
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
               
		writer.print(NativeKeyEvent.getKeyText(e.getKeyCode())+  ","+ System.currentTimeMillis() + ",");
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
        //writer.println(", " + System.currentTimeMillis() + ","+ NativeKeyEvent.getKeyText(e.getKeyCode()));
        //System.out.println("KeyReleased " + System.currentTimeMillis() + ","+ NativeKeyEvent.getKeyText(e.getKeyCode()));
        
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
            writer.println(NativeKeyEvent.getKeyText(e.getKeyCode())+ ","+ System.currentTimeMillis());
            System.out.println("KeyReleased " + System.currentTimeMillis() + ","+ NativeKeyEvent.getKeyText(e.getKeyCode()));
            releasedtime = System.currentTimeMillis();
            keystrokes.add(NativeKeyEvent.getKeyText(e.getKeyCode()));
            NumOfKeys++;
           
            if (e.getKeyCode() == NativeKeyEvent.VC_BACKSPACE) {
                count++;
            }
            System.out.println("Number of backspace = " + count);
            writer.close();
                 
	}
        

	public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + "\t"+ NativeKeyEvent.getKeyText(e.getKeyCode()));
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
