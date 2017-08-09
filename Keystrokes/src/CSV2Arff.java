/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User_1
 */
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;
 
public class CSV2Arff {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("\nUsage:");
      //System.exit(1);
    }
 
    // load CSV
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("log_file.csv"));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File("output.arff"));
    //saver.setDestination(new File("output.arff"));
    saver.writeBatch();
  }
}