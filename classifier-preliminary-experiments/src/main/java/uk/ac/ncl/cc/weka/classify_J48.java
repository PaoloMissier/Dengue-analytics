package uk.ac.ncl.cc.weka;
/**
 * Created by b4060825.
 */
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class classify_J48 {

	public static void main(String args[]) throws Exception {
		// load training dataset
		DataSource source = new DataSource("weka.arff");
		Instances trainDataset = source.getDataSet();
		// set class index to the first attribute
		trainDataset.setClassIndex(0);

		// the base classifier
		J48 tree = new J48();
		// the filter
		StringToWordVector filter = new StringToWordVector(100000);
		// Create the FilteredClassifier object
		FilteredClassifier fc = new FilteredClassifier();
		// specify filter
		fc.setFilter(filter);
		// specify base classifier
		fc.setClassifier(tree);
		fc.buildClassifier(trainDataset);
		// output model
		System.out.println(fc);

		// load new dataset
		DataSource source1 = new DataSource("test_weka.arff");
		Instances testDataset = source1.getDataSet();
		// set class index to the last attribute
		testDataset.setClassIndex(0);

		// loop through the new dataset and make predictions
		System.out.println("===================");
		System.out.println("Actual Class, J48 Predicted");
		for (int i = 0; i < testDataset.numInstances(); i++) {
			// get class double value for current instance
			double actualValue = testDataset.instance(i).classValue();
			// get class string value using the class index using the class's
			// int value
			String actual = testDataset.classAttribute().value(
					(int) actualValue);

			// get Instance object of current instance
			Instance newInst = testDataset.instance(i);
			// call classifyInstance, which returns a double value for the class
			double pred = fc.classifyInstance(newInst);
			// use this value to get string value of the predicted class
			String predString = testDataset.classAttribute().value((int) pred);
			System.out.println("Predicted class: " + predString);
		}

	}
}
