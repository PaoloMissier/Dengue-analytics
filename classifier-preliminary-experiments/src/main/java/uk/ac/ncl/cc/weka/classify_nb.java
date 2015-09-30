package uk.ac.ncl.cc.weka;
/**
 * Created by b4060825.
 */
import uk.ac.ncl.cc.TextIO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class classify_nb {
	public static void main(String args[]) throws Exception {
		       
		      //deserialize model created by CreateSave_nb_multinomialModel.java
				Classifier classifier = (Classifier) weka.core.SerializationHelper.read("nb_multinomial.model");
				
				// load new dataset
				DataSource source1 = new DataSource("unclassified_tweets_afterChanges.arff");
				Instances testDataset = source1.getDataSet();
				// set class index to the first attribute
				testDataset.setClassIndex(0);
				//To print output to a text file
				//TextIO.writeFile("Results.txt");
				
				// loop through the new dataset and make predictions
				System.out.println("===================");
				System.out.println(" Nb_Multinomial Predicted");
				for (int i = 0; i < testDataset.numInstances(); i++) {
					// get class double value for current instance
					double actualClass = testDataset.instance(i).classValue();
					//get class string value using the class index using the class's int value
					String actual = testDataset.classAttribute().value((int) actualClass);
					//get Instance object of current instance
					Instance newInst = testDataset.instance(i);
					// call classifyInstance, which returns a double value for the class
					double pred = classifier.classifyInstance(newInst);
					// use this value to get string value of the predicted class
					String predString = testDataset.classAttribute().value((int) pred);
					System.out.print(predString+ "\t\t\t");
					//TextIO.put( predString);
					 double[] prediction=classifier.distributionForInstance(newInst);

				        //output predictions
				        for(int j=0; j<prediction.length; j=j+1)
				        {
				        	if(prediction[j]<0.00 )
				        		 System.out.print("0 \t\t\t")	;
				        			
				            System.out.print(Double.toString(prediction[j] )+ "\t\t\t");
				            //System.out.print("\t\t\t");
				           // TextIO.put(Double.toString(prediction[j] )+ "\t\t\t");
				}
				        System.out.println("");
				        //TextIO.putln("");
			}}
		}


