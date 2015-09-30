package uk.ac.ncl.cc.weka;
/**
 * Created by b4060825.
 */
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.tokenizers.NGramTokenizer;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Arrays;
import java.util.Random;

import opennlp.tools.util.eval.Mean;
import uk.ac.ncl.cc.TextIO;

public class evaluation_nb_multinomial {

	private static Mean acccuracy = new Mean();
	private static Mean Incorrect = new Mean();
	private static Mean AUC = new Mean();
	private static Mean kappa = new Mean();
	private static Mean Precision = new Mean();
	private static Mean Error_Rate = new Mean();

	public static void main(String[] args) throws Exception {

		// load dataset
		DataSource source = new DataSource("tweets.arff");
		Instances dataset = source.getDataSet();
		// set class index to the first attribute
		dataset.setClassIndex(0);

		StringToWordVector filter = new StringToWordVector();
		filter.setWordsToKeep(100000000);
		filter.setOutputWordCounts(true);
		filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));

		NGramTokenizer tokenizer = new NGramTokenizer();
		tokenizer.setNGramMinSize(1);
		tokenizer.setNGramMaxSize(4);
		filter.setTokenizer(tokenizer);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(new NaiveBayesMultinomial());
		fc.setFilter(filter);
		
		//=================Evaluation by checking results of each fold===============================================
		/*int seed = 1;
		int folds = 10;
		// randomize data
		Random rand = new Random(seed);
		// create random dataset
		Instances randData = new Instances(dataset);
		randData.randomize(rand);
		// stratify
		if (randData.classAttribute().isNominal())
			randData.stratify(folds);

		TextIO
				.writeFile("Evaluation results for Naive Bayes multinomial.txt");
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
							+ "/" + folds + " ===\n\n"));
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

			// the confusion matrix
			 //System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
		}
		System.out.println("\n\nTokenizer used is:" +filter.getTokenizer()) ;
		System.out.println("\n\n WTK is:" + filter.getWordsToKeep());
		System.out.println("\n\n Normalization  is:" + filter.getNormalizeDocLength());
		System.out.println("\n\nMean Accuracy is:" + acccuracy.mean());
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
		========================================================================================================
	*/
	
	
        //================= Evaluation by checking the final result instead of result of each fold==========================
	    Evaluation eval = new Evaluation(dataset);
		Random rand = new Random(1);
		int folds = 10;
		
		//Notice we build the classifier with the training dataset
        //now evaluate model
		
		eval.crossValidateModel(fc, dataset, folds, rand);
		System.out.println(eval.toSummaryString("Evaluation results:\n", false));
		
		System.out.println("Correct % = "+eval.pctCorrect());
		System.out.println("Incorrect % = "+eval.pctIncorrect());
		System.out.println("AUC = "+eval.areaUnderROC(1));
		System.out.println("kappa = "+eval.kappa());
		System.out.println("MAE = "+eval.meanAbsoluteError());
		System.out.println("RMSE = "+eval.rootMeanSquaredError());
		System.out.println("RAE = "+eval.relativeAbsoluteError());
		System.out.println("RRSE = "+eval.rootRelativeSquaredError());
		System.out.println("Precision = "+eval.precision(1));
		System.out.println("Recall = "+eval.recall(1));
		System.out.println("fMeasure = "+eval.fMeasure(1));
		System.out.println("Error Rate = "+eval.errorRate());
	    //the confusion matrix
		System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));

		System.out.println(Arrays.toString(filter.getOptions()));
	      
	
	}
	
	
}
