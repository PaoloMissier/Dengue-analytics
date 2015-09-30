package uk.ac.ncl.cc.stanford;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.classify.GeneralDataset;
import edu.stanford.nlp.util.Pair;

import java.util.List;

public class LogRegressionTest {

    public static void main(String[] args) throws Exception {
        ColumnDataClassifier cdc = new ColumnDataClassifier("stanford/log/tweets.prop");
        Pair<GeneralDataset<String, String>, List<String[]>> dataset = cdc.readAndReturnTrainingExamples("stanford/log/tweets.train");
        cdc.crossValidate(dataset.first(), dataset.second());
    }
}
