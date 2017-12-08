package Classifiers;

import DatabaseConnection.ConnectDatabase;
import Classifiers.features;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.j48.ClassifierTree;
import weka.classifiers.trees.j48.ModelSelection;
import weka.core.FastVector;
import weka.core.Instances;
 
public class Weka {
    public static AssociationRules ruleobj;
    public static ClassifierTree tree;
    private static PreparedStatement preparedStatement = null;
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static Evaluation classify(Classifier model,
		Instances trainingSet, Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);
 
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
 
		return evaluation;
	}
 
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
 
		return 100 * correct / predictions.size();
	}
 
	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
 
		return split;
	}
 
	public static String extractpattern(String filename) throws Exception {
		BufferedReader datafile = readDataFile(filename);
 
		Instances data = new Instances(datafile);
		data.setClassIndex(data.numAttributes() - 1);
 
		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(data, 4);
 
		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
 
		// Use a set of classifiers
		Classifier[] models = { 
                                 // a decision tree
				new J48(), 
                                new RandomForest(),
                                
				
		};
 
                  ModelSelection m = null;
                  
		// Run for each model
		for (int j = 0; j < models.length; j++) {
 
			// Collect every group of predictions for current model in a FastVector
			FastVector predictions = new FastVector();
 
			// For each training-testing split pair, train and test the classifier
			for (int i = 0; i < trainingSplits.length; i++) {
				Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);
 
				predictions.appendElements(validation.predictions());
 
				// Uncomment to see the summary for each training-testing pair.
                                 //split(models[j].toString());
				System.out.println(models[j].toString());
                               
                                
                                
			}
                      
			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);
                        compareRule(filename);
                       
                    //Print current classifier's name and accuracy in a complicated,
                    // but nice-looking way.
                    /*System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
                    + String.format("%.2f%%", accuracy)
                    + "\n---------------------------------");*/
                    
		}
                return "";
 
	}
       public static ArrayList<String> splitForTestingFile(String tree) throws IOException, Exception {
         FileWriter fw = new FileWriter("rulesCompareTest.csv", true);
         PrintWriter writer = new PrintWriter(fw);
        String[] lines = tree.split("\n");
        
        List<List<String>> lists = new ArrayList<List<String>>();
        for (String line : lines) {
            List<String> temp = new ArrayList<String>();
            while (line.indexOf("|") != -1) {
                temp.add("|");
                line = line.replaceFirst("\\|", "");
            }
            temp.add(line.trim());
            lists.add(temp);
        }

        for (int i = 0; i < 3; i++) {
         lists.remove(0);
        }
        for (int i = 0; i < 4; i++) {
            lists.remove(lists.size() - 1);
        }
        List<String> substitutes = new ArrayList<String>();

        for (List<String> list : lists) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).contains(":") && !list.get(i).equals("|") && !substitutes.contains(list.get(i))) {
                    substitutes.add(list.get(i));
                }
            }
            //System.out.println("list: " + list.toString());
        }
         //System.out.println("substitutes: "+ substitutes.toString());
        for (List<String> list : lists) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals("|")) {
                    list.set(i, substitutes.get(i));
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        ArrayList<String> tempRule = new ArrayList<String>();
        for (List<String> list : lists) {
            String line = "";
            String rule = "";
            String condition = "";
            String accuracyWeight = "";
            String bcon = "";
            String bWeight = "";
            String numerator = "";
            String denominator = "";
            String finaldenominator = "";
            String [] tokenSplit;
            String [] token2Split;
             double num = 0.0;
            double den = 0.0;
            double computedWeight = 0.00;
             double extract = 0.0;
            for (String s : list) {
                line = line + " " + s;
                for(int i = 0; i < line.indexOf(":");i++)
                {
                    rule += line.charAt(i);
                }
                
                //int j = line.indexOf(":");
              
                condition = line.substring(line.indexOf(":") + 1,line.indexOf("(") + 1);
                bcon = condition.replaceAll("\\(", "");
                accuracyWeight = line.substring(line.indexOf("(") + 1,line.length());
                bWeight = accuracyWeight.replace("\\)", "");
                
                //denominator =  stripNonDigitsV2(bWeight);
                 
                int i = bWeight.indexOf("/") + 1;
                denominator = bWeight.substring(0, i);
                numerator = bWeight.substring(i,bWeight.length());
                i++;
                
                
                
                
                 Matcher m = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(denominator);
                 Matcher m2 = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(numerator);

                while (m.find() && m2.find()) {
                    double value = Double.parseDouble(m.group());
                    double value2 = Double.parseDouble(m2.group());
                  
                if(value2 == 0.0)
                {
                    computedWeight = 0.0;
                }
                else
                {
                    computedWeight = value2/value;
                }
                //System.out.println("ComputedWeight=" + computedWeight);
        }
    }
 
            
          
            
            if (line.endsWith(")")) {
                 
                sb.append(line + "\n");
              
                 //writer.append(rule + "\n");
                 //System.out.println(rule + "\n");
                 tempRule.add(rule);
            }
        }
        
        writer.close();
        return tempRule;
       }

    
     public static void split(String tree) throws IOException, Exception {
       //Writer writer = new PrintWriter(fw);
        String[] lines = tree.split("\n");
        
        List<List<String>> lists = new ArrayList<List<String>>();
        for (String line : lines) {
            List<String> temp = new ArrayList<String>();
            while (line.indexOf("|") != -1) {
                temp.add("|");
                line = line.replaceFirst("\\|", "");
            }
            temp.add(line.trim());
            lists.add(temp);
        }

        for (int i = 0; i < 3; i++) {
         lists.remove(0);
        }
        for (int i = 0; i < 4; i++) {
            lists.remove(lists.size() - 1);
        }
        List<String> substitutes = new ArrayList<String>();

        for (List<String> list : lists) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).contains(":") && !list.get(i).equals("|") && !substitutes.contains(list.get(i))) {
                    substitutes.add(list.get(i));
                }
            }
            //System.out.println("list: " + list.toString());
        }
         //System.out.println("substitutes: "+ substitutes.toString());
        for (List<String> list : lists) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals("|")) {
                    list.set(i, substitutes.get(i));
                }
            }
        }
        
        
        StringBuilder sb = new StringBuilder();
        for (List<String> list : lists) {
            String line = "";
            String rule = "";
            String condition = "";
            String accuracyWeight = "";
            String bcon = "";
            String bWeight = "";
            String numerator = "";
            String denominator = "";
            String finaldenominator = "";
            String [] tokenSplit;
            String [] token2Split;
             double num = 0.0;
            double den = 0.0;
            double computedWeight = 0.00;
             double extract = 0.0;
            for (String s : list) {
                line = line + " " + s;
                for(int i = 0; i < line.indexOf(":");i++)
                {
                    rule += line.charAt(i);
                }
                
                //int j = line.indexOf(":");
              
                condition = line.substring(line.indexOf(":") + 1,line.indexOf("(") + 1);
                bcon = condition.replaceAll("\\(", "");
                accuracyWeight = line.substring(line.indexOf("(") + 1,line.length());
                bWeight = accuracyWeight.replace("\\)", "");
                
                //denominator =  stripNonDigitsV2(bWeight);
                 
                int i = bWeight.indexOf("/") + 1;
                denominator = bWeight.substring(0, i);
                numerator = bWeight.substring(i,bWeight.length());
                i++;
                
                
                
                
                 Matcher m = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(denominator);
                 Matcher m2 = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(numerator);

                while (m.find() && m2.find()) {
                    double value = Double.parseDouble(m.group());
                    double value2 = Double.parseDouble(m2.group());
                  
                if(value2 == 0.0)
                {
                    computedWeight = 0.0;
                }
                else
                {
                    computedWeight = value2/value;
                }
                //System.out.println("ComputedWeight=" + computedWeight);
        }
    }
 
            
          
            
            if (line.endsWith(")")) {
                 
                sb.append(line + "\n");
              
                System.out.println(rule + "," + bcon + ","  + computedWeight +"\n");
                
                //Connect to database and insert into allrules table
                 ConnectDatabase con = new ConnectDatabase();
                 
                 try
                 {
                     con.connectToDataBase();
                     
                     String query = " insert into keystrokes.allrules(rule, class, weight)"
                       + " values (?, ?, ?)";
                     
                     preparedStatement = con.connect.prepareStatement(query);
                     preparedStatement.setString(1, rule);
                     preparedStatement.setString(2, bcon);
                     preparedStatement.setDouble(3, computedWeight);
                     preparedStatement.executeUpdate(); 
                    
                 }
                 catch(Exception ex)
                 {
                     Logger.getLogger(features.class.getName()).log(Level.SEVERE, null, ex);
                 } finally {
                     con.close();
                 }

             }
         }
        
        
    }
     
     
 

 
    public static void compareRule(String filename) throws SQLException, IOException, Exception {
        
        ConnectDatabase con = new ConnectDatabase(); 
        con.connectToDataBase();
             Statement stmt = null;
            
             String query1 = "select * from keystrokes.allrules order by class";
            
             stmt = con.connect.createStatement();
             ResultSet rs1 = con.connect.createStatement().executeQuery(query1);
            
        
        ArrayList<String>tempRule = new ArrayList<String>();
        String newRuleClass = null;
        String line = "";
       
        //FileWriter fw = new FileWriter("patternmatched.csv", true);
        PrintWriter writer1 = new PrintWriter("patternmatched.csv");
      
       BufferedReader datafile = readDataFile(filename);
         
        
        Instances data = new Instances(datafile);
	data.setClassIndex(data.numAttributes() - 1);
 
	// Do 10-split cross validation
	Instances[][] split = crossValidationSplit(data, 4);
 
	// Separate split into training and testing arrays
	Instances[] trainingSplits = split[0];
	Instances[] testingSplits = split[1];
 
	// Use a set of classifiers
	Classifier[] models = { 
                                 // a decision tree
				new J48(), 
                                new RandomForest(),
                                
				
	};
                
        ModelSelection m = null;
        
                  
		// Run for each model
		for (int j = 0; j < models.length; j++) {
                
			// Collect every group of predictions for current model in a FastVector
			FastVector predictions = new FastVector();
 
			// For each training-testing split pair, train and test the classifier
			for (int i = 0; i < trainingSplits.length; i++) {
                          
				Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);
 
				predictions.appendElements(validation.predictions());
 
				// Uncomment to see the summary for each training-testing pair.
                                tempRule =splitForTestingFile(models[j].toString());
                                
				System.out.println(tempRule.toString());
                                       
                                
			}
                        
                        
             
     
            while (rs1.next()) {
                //Retrieve by column name
              
                String rules = rs1.getString("rule");

                String ruleClass = rs1.getString("class");
                double weight = rs1.getDouble("weight");
                for (int i = 0; i < tempRule.size(); i++) {

                    if (tempRule.get(i).equals(rules)) {
                        if(weight <= 0.1)
                        {  
                            newRuleClass = ruleClass;
                            writer1.append(tempRule.get(i) + "," + newRuleClass + "," + weight + "\n");
                            System.out.println(tempRule.get(i) + "," + newRuleClass + "," + weight + "\n");
                        }
                        else if(weight >= 0.11 && weight <= 0.2)
                        {
                            newRuleClass = ruleClass;
                            writer1.append(tempRule.get(i) + "," + newRuleClass + "," + weight + "\n");
                            System.out.println(tempRule.get(i) + "," + newRuleClass + "," + weight + "\n");
                        }
                        
                        else if(weight >= 0.21 && weight <= 0.3)
                        {
                            newRuleClass = ruleClass;
                            writer1.append(tempRule.get(i) + "," + newRuleClass + "," + weight + "\n");
                            System.out.println(tempRule.get(i) + "," + newRuleClass + "," + weight + "\n");
                        }
                    }

                }

            }

        }
                //System.out.println("inside the loop");
        //System.out.println("New Array: " + tempRule.toString());

        writer1.close();
        con.close();

    }
    
    
     public static boolean checkAccuracyWeights(ArrayList<Double> weight,ArrayList<String> rulesFromCSV, ArrayList<String> ruleClassFromCSV)
     {
        String rule = null;
         String ruleClass = null;
         double originalWeight = 0.0;
         
        ConnectDatabase con = new ConnectDatabase(); 
        
        try {
             con.connectToDataBase();
             String query = " insert into keystrokes.condition1Rules(rules, class, weight)"
                       + " values (?, ?, ?)";
           
            preparedStatement = con.connect.prepareStatement(query);
         for(int i = 0; i < rulesFromCSV.size() && i < weight.size() && i < ruleClassFromCSV.size(); i++)
         {               
             if(weight.get(i) <= 0.1) {
                 rule = rulesFromCSV.get(i);
                 ruleClass = ruleClassFromCSV.get(i);
                 originalWeight = weight.get(i);
                 preparedStatement.setString(1,rule);
                 preparedStatement.setString(2,ruleClass);
                 preparedStatement.setDouble(3,originalWeight);
                preparedStatement.executeUpdate();
                 
                 
             }
                
         }
         
         String query2 = " insert into keystrokes.condition2Rules(rules, class, weight)"
                       + " values (?, ?, ?)";
          preparedStatement = con.connect.prepareStatement(query2);
        for(int i = 0; i < rulesFromCSV.size() && i < weight.size() && i < ruleClassFromCSV.size(); i++)
         {               
             if(weight.get(i) >= 0.11 && weight.get(i) <= 0.2 ) {
                 rule = rulesFromCSV.get(i);
                 ruleClass = ruleClassFromCSV.get(i);
                 originalWeight = weight.get(i);
                 preparedStatement.setString(1,rule.toString());
                 preparedStatement.setString(2,ruleClass);
                 preparedStatement.setDouble(3,originalWeight);
                 preparedStatement.executeUpdate();
             }
         }
        String query3 = " insert into keystrokes.condition3Rules(rules, class, weight)"
                       + " values (?, ?, ?)";
         preparedStatement = con.connect.prepareStatement(query3);
          for(int i = 0; i < rulesFromCSV.size() && i < weight.size() && i < ruleClassFromCSV.size(); i++)
         {               
             if(weight.get(i) >= 0.21 && weight.get(i) <= 0.3) {
                 rule = rulesFromCSV.get(i);
                 ruleClass = ruleClassFromCSV.get(i);
                 originalWeight = weight.get(i);
                 preparedStatement.setString(1,rule.toString());
                 preparedStatement.setString(2,ruleClass);
                 preparedStatement.setDouble(3,originalWeight);
                 preparedStatement.executeUpdate();
             }
         }
    
             
        }catch(Exception ex)
        {
             Logger.getLogger(features.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            con.close();
        }
         return true;
     }

}