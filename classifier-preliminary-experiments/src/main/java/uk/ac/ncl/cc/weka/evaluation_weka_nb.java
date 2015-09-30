package uk.ac.ncl.cc.weka;
/**
 * Created by b4060825.
 */
import weka.*;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Random;

import opennlp.tools.util.eval.Mean;
import uk.ac.ncl.cc.TextIO;
public class evaluation_weka_nb {
	private static Mean acccuracy = new Mean();
	private static Mean Incorrect = new Mean();
	private static Mean AUC = new Mean();
	private static Mean kappa = new Mean();
	private static Mean Precision = new Mean();
	private static Mean Error_Rate = new Mean();

	public static void main(String[] args) throws Exception {

		// load dataset
		DataSource source = new DataSource("tweets-processed.arff");
		Instances dataset = source.getDataSet();
		// set class index to the first attribute
		dataset.setClassIndex(0);

		// the base classifier
		NaiveBayesMultinomial nb = new NaiveBayesMultinomial();
		// the filter
		StringToWordVector filter = new StringToWordVector(100000);
        filter.setTokenizer(new NGramTokenizer());
        filter.setOutputWordCounts(true);
		
        // Create the FilteredClassifier object
		FilteredClassifier fc = new FilteredClassifier();
		// specify filter
		fc.setFilter(filter);
		// specify base classifier
		fc.setClassifier(nb);

		int seed = 1;
		int folds = 10;
		// randomize data
		Random rand = new Random(seed);
		// create random dataset
		Instances randData = new Instances(dataset);
		randData.randomize(rand);
		// stratify
		if (randData.classAttribute().isNominal())
			randData.stratify(folds);

		TextIO.writeFile("Evaluation results for Naive Bayes.txt");
		// perform cross-validation
		for (int n = 0; n < folds; n++) {
			Evaluation eval = new Evaluation(randData);
			// get the folds
			Instances train = randData.trainCV(folds, n);
			Instances test = randData.testCV(folds, n);
			// build and evaluate classifier
			fc.buildClassifier(train);
			eval.evaluateModel(fc, test);

			// output evaluation
			System.out.println();
			System.out.println(eval
					.toMatrixString("=== Confusion matrix for fold " + (n + 1)
							+ "/" + folds + " ===\n"));
			TextIO.putln(eval
					.toMatrixString("=== Confusion matrix for fold " + (n + 1)
							+ "/" + folds + " ===\n"));
			;

			System.out.println("Correct % = " + eval.pctCorrect());
		TextIO.putln("Correct % = " + eval.pctCorrect());
			acccuracy.add(eval.pctCorrect());

			TextIO.putln("Incorrect % = " + eval.pctIncorrect());
			System.out.println("Incorrect % = " + eval.pctIncorrect());
			Incorrect.add(eval.pctIncorrect());

			System.out.println("AUC = " + eval.areaUnderROC(1));
			TextIO.putln("AUC = " + eval.areaUnderROC(1));
			AUC.add(eval.areaUnderROC(1));

			System.out.println("kappa = " + eval.kappa());
			TextIO.putln("kappa = " + eval.kappa());
			kappa.add(eval.kappa());

			System.out.println("MAE = " + eval.meanAbsoluteError());
			TextIO.putln("MAE = " + eval.meanAbsoluteError());

			System.out.println("RMSE = " + eval.rootMeanSquaredError());
			TextIO.putln("RMSE = " + eval.rootMeanSquaredError());

			System.out.println("RAE = " + eval.relativeAbsoluteError());
			TextIO.putln("RAE = " + eval.relativeAbsoluteError());

			System.out.println("RRSE = " + eval.rootRelativeSquaredError());
			TextIO.putln("RRSE = "
					+ eval.rootRelativeSquaredError());

			System.out.println("Precision = " + eval.precision(1));
			TextIO.putln("Precision = " + eval.precision(1));
			Precision.add(eval.precision(1));

			System.out.println("Recall = " + eval.recall(1));
			TextIO.putln("Recall = " + eval.recall(1));

			System.out.println("fMeasure = " + eval.fMeasure(1));
			TextIO.putln("fMeasure = " + eval.fMeasure(1));

			System.out.println("Error Rate = " + eval.errorRate());
			TextIO.putln("Error Rate = " + eval.errorRate());
			Error_Rate.add(eval.errorRate());

			
		}

		System.out.println("Mean Accuracy is:" + acccuracy.mean());
		System.out.println("Mean Incorrect % is:" + Incorrect.mean());
		System.out.println("Mean AUC is:" + AUC.mean());
		System.out.println("Mean kappa is:" + kappa.mean());
		System.out.println("Mean Precision is:" + Precision.mean());
		System.out.println("Mean Error_Rate is:" + Error_Rate.mean());

		TextIO.putln("Mean Accuracy is:" + acccuracy.mean());
		TextIO.putln("Mean Incorrect %is:" + Incorrect.mean());
		TextIO.putln("Mean AUC is:" + AUC.mean());
		TextIO.putln("Mean kappa is:" + kappa.mean());
		TextIO.putln("Mean Precision is:" + Precision.mean());
		TextIO.putln("Mean Error_Rate is:" + Error_Rate.mean());
	}
}
