import be.abeel.util.Pair;
import java.io.File;
import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.classifiers.trees.*;
import weka.filters.*;

import java.util.*;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.sampling.Sampling;
import net.sf.javaml.tools.data.FileHandler;
import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;

/**
 * performs attribute selection using CfsSubsetEval and GreedyStepwise
 * (backwards) and trains J48 with that. Needs 3.5.5 or higher to compile.
 *
 * 
 */
public class AttributeSelectionTest {

  /**
   * uses the meta-classifier
   */
  protected static void useClassifier(Instances data) throws Exception {
    System.out.println("\n1. Meta-classfier");
    AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
    CfsSubsetEval eval = new CfsSubsetEval();
    GreedyStepwise search = new GreedyStepwise();
    search.setSearchBackwards(true);
    J48 base = new J48();
    classifier.setClassifier(base);
    classifier.setEvaluator(eval);
    classifier.setSearch(search);
    Evaluation evaluation = new Evaluation(data);
    evaluation.crossValidateModel(classifier, data, 10, new Random(1));
    System.out.println(evaluation.toSummaryString());
  }

  /**
   * uses the filter
   */
  protected static void useFilter(Instances data) throws Exception {
    System.out.println("\n2. Filter");
    weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
    CfsSubsetEval eval = new CfsSubsetEval();
    GreedyStepwise search = new GreedyStepwise();
    search.setSearchBackwards(true);
    filter.setEvaluator(eval);
    filter.setSearch(search);
    filter.setInputFormat(data);
    Instances newData = Filter.useFilter(data, filter);
    System.out.println(newData);
  }

  /**
   * uses the low level approach
   */
  protected static void useLowLevel(Instances data) throws Exception {
    System.out.println("\n3. Low-level");
    AttributeSelection attsel = new AttributeSelection();
    CfsSubsetEval eval = new CfsSubsetEval();
    GreedyStepwise search = new GreedyStepwise();
    search.setSearchBackwards(true);
    attsel.setEvaluator(eval);
    attsel.setSearch(search);
    attsel.SelectAttributes(data);
    int[] indices = attsel.selectedAttributes();
    System.out.println("selected attribute indices (starting with 0):\n" + Utils.arrayToString(indices));
  }

  /**
   * takes a dataset as first argument
   *
   * @param args        the commandline arguments
   * @throws Exception  if something goes wrong
   */
  public static void main(String[] args) throws Exception {
    // load data
    System.out.println("\n0. Loading data");
    DataSource source = new DataSource("reporting.arff");
    Instances data = source.getDataSet();
    if (data.classIndex() == -1)
      data.setClassIndex(data.numAttributes() - 1);

    // 1. meta-classifier
    
    useClassifier(data);

    // 2. filter
    useFilter(data);

    // 3. low-level
    useLowLevel(data);
      
      /*Dataset data = FileHandler.loadDataset(new File("iris.csv"), 4, ",");
      Sampling s = Sampling.SubSampling;
      for (int i = 0; i < 5; i++) {
          Pair<Dataset, Dataset> datas = s.sample(data, (int) (data.size() * 0.5), i);
          Classifier c = (Classifier) new LibSVM();
          c.buildClassifier(datas.x());
          Map pms = EvaluateDataset.testDataset(c, datas.y());
          System.out.println(pms);
}*/
  }
}