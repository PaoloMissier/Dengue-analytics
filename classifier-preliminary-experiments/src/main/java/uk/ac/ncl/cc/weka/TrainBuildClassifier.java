package uk.ac.ncl.cc.weka;


import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TrainBuildClassifier {

    public static void main(String[] args) throws Exception {

        DataSource dataSource = new DataSource("tweets.arff");
        Instances instances = dataSource.getDataSet();
        instances.setClassIndex(0);

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

        // Train and build the classifier
        fc.buildClassifier(instances);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("bayes.model"));
        out.writeObject(fc);
        out.flush();
        out.close();
    }
}
