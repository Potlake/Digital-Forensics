package FileManipulation;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User_1
 */
public class ReadRawData {
    private static ArrayList<Long> dwelltime;
    private static long dwellmean;
    private static ArrayList<Long> downflighttime;
    private static long downflightmean;
    private static ArrayList<Long> upflighttime;
    private static long upflightmean;
    private static long standardDeviation;
    private static long downflightstandardDeviation;
    private static long upflightstandardDeviation;
    
    public static void readRawData() throws IOException
    {
         String csvFile = "log_file.csv";
        String[][] arrayStrings=new String[4][6];
         
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
          
 
        try {
 
            
 
            br = new BufferedReader(new FileReader(csvFile));
          
           
            boolean firstTime=false;
            int i=0;
            while ((line = br.readLine()) != null) {
                 firstTime=false;
                  
                arrayStrings[i] = line.split(cvsSplitBy);
                 
                i++;
                if(i==4)
                {
                    i=0;
                    System.out.println("CSV contents:" + arrayStrings[i].toString());
                    
                }
            
            }
 
 
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        //reader.close();
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
           
                
                readRawData();
                
                
	}
}
