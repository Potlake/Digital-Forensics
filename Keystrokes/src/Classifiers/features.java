package Classifiers;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Statement;
import java.util.ArrayList;
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

    private long presstime;
    private long releasetime;
    private long dwelltime;
    private long upflighttime;
    private ArrayList<Long> upflighttimeArray = new ArrayList<Long>();
    private long downflighttime;
    private long dwelltimemean;
    private long upflighttimemean;
    private long downflighttimemean;
    private long dwelltimestdDev;
    private long upflighttimestdDev;
    private long downflighttimestdDev;
    private int  numberOfBackspaces;
    private ArrayList<Long> downflighttimeArray = new ArrayList<Long>();
    
    
    public features()
    {
        
    }
    
    public features(long dwelltime,long upflighttime,long downflighttime,long dwelltimemean,long upflighttimemean,long downflighttimemean ,long dwelltimestdDev,long upflighttimestdDev, long downflighttimestdDev,int numberOfBackspaces)
    {
        this.dwelltime = dwelltime;
        this.upflighttime = upflighttime;
        this.downflighttime = downflighttime;
        this.downflighttimemean = downflighttimemean;
        this.downflighttimestdDev = downflighttimestdDev;
        this.upflighttimemean = upflighttimemean;
        this.upflighttimestdDev = upflighttimestdDev;
        this.dwelltimemean = dwelltimemean;
        this.dwelltimestdDev = dwelltimestdDev;
        this.numberOfBackspaces = numberOfBackspaces; 
        
    }
  
    //Getters
    public long getDwelltime()
    {
        return dwelltime;
    }
    
    public long getUpflighttime()
    {
        return upflighttime;
    }
    public long getDownflighttime()
    {
        return downflighttime;
    }
    public long getDownflighttimemean()
    {
        return downflighttimemean;
    }
    public long getDownflighttimestdDev()
    {
        return downflighttimestdDev;
    }
    public long getUpflighttimemean()
    {
        return upflighttimemean;
    }
    public long getUpflighttimestdDev()
    {
        return upflighttimestdDev;
    }
    public long getDwelltimemean()
    {
        return dwelltimemean;
    }
    public long getDwelltimestdDev()
    {
        return dwelltimestdDev;
    }
    public long getNumberOfBackspaces()
    {
        return numberOfBackspaces;
    }
    
    
    //Setters
    public void setDwelltime(long dt)
    {
        dwelltime = dt;
    }
    
    public void setUpflighttime(long upft)
    {
        upflighttime = upft;
    }
     
    public void setDownflighttime(long dft)
    {
        downflighttime = dft;
    }
      
    public void setDownflighttimemean(long dftm)
    {
        downflighttimemean = dftm;
    }
     public void setDownflighttimestdDev(long dftd)
    {
        downflighttimemean = dftd;
    }
     
     public void setUpflighttimemean(long upftm)
    {
        upflighttimemean = upftm;
    }
     
      public void setUpflighttimestdDev(long upftd)
    {
        upflighttimestdDev = upftd;
    }
      
     public void setDwelltimemean(long dtm)
    {
        dwelltimemean = dtm;
    }
       
    public void setDwelltimestdDev(long dtd)
    {
        dwelltimestdDev = dtd;
    }
    public void setNumberOfBackspaces(int nob)
    {
        numberOfBackspaces = nob;
    }
    
    //Functions
    public String print()
    {
        return dwelltime + " " + dwelltimemean + " " + dwelltimestdDev + " " + upflighttime + " " + upflighttimemean + " " + upflighttimestdDev + " " + downflighttime + " " + downflighttimemean + " " + downflighttimemean + " " + numberOfBackspaces;
    }
    
    public long calculateDwelltime(long presstime,long releasetime)
    {
        dwelltime = releasetime - presstime;
        return dwelltime;
    }
    
    public long calculateDwelltimeMean(ArrayList<Long> dwelltimelist)
    {
        dwelltimelist.add(dwelltime);
        long total = 0;
        for(Long d : dwelltimelist)
        {
            total += d;
        }
        
        dwelltimemean = total/dwelltimelist.size();
        return dwelltimemean;
    }
    
    public long calculateDwelltimeStdDeviation(ArrayList<Long> dwelltimelist)
    {
        dwelltimelist.add(dwelltime);
        
        long totalDwelltime = 0;
        long mean = 0;
        for (Long d : dwelltimelist) {
            totalDwelltime += Math.pow(d - calculateDwelltimeMean(dwelltimelist), 2);
        }
        mean = totalDwelltime / dwelltimelist.size();
        dwelltimestdDev = (long) Math.sqrt(mean);
        
        return dwelltimestdDev;
    }
    
    public ArrayList<Long> calculateUpflightTime(ArrayList<Long> releasetimelist, ArrayList<String> keystrokes)
    {
       
        releasetimelist.add(releasetime);
        keystrokes.add(null);
        
        for(int i = 0; i < keystrokes.size(); i++)
        {
            if(i + 1 < releasetimelist.size())
            {
                upflighttime = releasetimelist.get(i + 1) - releasetimelist.get(i);
                upflighttimeArray.add(upflighttime);
            }
        }
        return upflighttimeArray;
    }
    
    public long calculateUpflightTimeMean(ArrayList<Long> upflightlist)
    {
        upflightlist.add(upflighttime);
        long total = 0;
        for(Long d : upflightlist) {
            total += d;
        }
        upflighttimemean = total / upflightlist.size();

        return upflighttimemean;
    }
    
    public long calculateUpflightTimeStdDeviation(ArrayList<Long> upflightlist)
    {
        upflightlist.add(upflighttime);
        long totalDwelltime = 0;
        long mean = 0;
        for (Long d : upflightlist) {
            totalDwelltime += Math.pow(d - calculateUpflightTimeMean(upflightlist), 2);
        }
        mean = totalDwelltime / upflightlist.size();
        upflighttimestdDev = (long) Math.sqrt(mean);

        return upflighttimestdDev;
    }
    
    public ArrayList<Long> calculateDownflightTime(ArrayList<Long> presstimelist, ArrayList<String> keystrokes)
    {
        presstimelist.add(presstime);
        keystrokes.add(null);
        
        for(int i = 0; i < keystrokes.size(); i++)
        {
            if(i + 1 < presstimelist.size())
            {
                downflighttime = presstimelist.get(i + 1) - presstimelist.get(i);
                downflighttimeArray.add(downflighttime);
            }
        }
        return downflighttimeArray;
    }
    public long calculateDownflightTimeMean(ArrayList<Long> downflightlist)
    {
        downflightlist.add(downflighttime);
        long total = 0;
        for(Long d : downflightlist) {
            total += d;
        }
        downflighttimemean = total / downflightlist.size();

        return downflighttimemean;
    }
    
    public long calculateDownflightTimeStdDeviation(ArrayList<Long> downflightlist)
    {
        downflightlist.add(upflighttime);
        long totalDwelltime = 0;
        long mean = 0;
        for (Long d : downflightlist) {
            totalDwelltime += Math.pow(d - calculateDownflightTimeMean(downflightlist), 2);
        }
        mean = totalDwelltime / downflightlist.size();
        downflighttimestdDev = (long) Math.sqrt(mean);

        return downflighttimestdDev;
    }
}
