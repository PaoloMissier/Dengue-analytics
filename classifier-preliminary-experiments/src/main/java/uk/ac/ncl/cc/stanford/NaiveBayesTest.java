package uk.ac.ncl.cc.stanford;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.classify.GeneralDataset;
import edu.stanford.nlp.util.Pair;

import java.util.List;

public class NaiveBayesTest {

    public static void main(String[] args) throws Exception {
        ColumnDataClassifier cdc = new ColumnDataClassifier("stanford/nb/tweets.prop");
        Pair<GeneralDataset<String, String>, List<String[]>> dataset = cdc.readAndReturnTrainingExamples("stanford/nb/tweets.train");
        cdc.crossValidate(dataset.first(), dataset.second());
    }
}
