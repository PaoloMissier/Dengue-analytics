package uk.ac.ncl.cc.evaluation;

import java.io.IOException;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.CrossValidationPartitioner;
import opennlp.tools.util.eval.Mean;

public class DoccatCrossValidator {

    private final String languageCode;

    private final TrainingParameters params;

    private Mean documentAccuracy = new Mean();

    private DoccatEvaluationMonitor[] listeners;


    /**
     * Creates a {@link DoccatCrossValidator} with the given
     */
    public DoccatCrossValidator(String languageCode, TrainingParameters mlParams, DoccatEvaluationMonitor ... listeners) {
        this.languageCode = languageCode;
        this.params = mlParams;
        this.listeners = listeners;
    }

    /**
     * Starts the evaluation.
     *
     * @param samples
     *          the data to train and test
     * @param nFolds
     *          number of folds
     *
     * @throws IOException
     */
    public void evaluate(ObjectStream<DocumentSample> samples, int nFolds)
            throws IOException {

        CrossValidationPartitioner<DocumentSample> partitioner = new CrossValidationPartitioner<DocumentSample>(
                samples, nFolds);

        while (partitioner.hasNext()) {

            CrossValidationPartitioner.TrainingSampleStream<DocumentSample> trainingSampleStream = partitioner
                    .next();

            DoccatModel model = DocumentCategorizerME.train(languageCode,
                    trainingSampleStream);

            DocumentCategorizerEvaluator evaluator = new DocumentCategorizerEvaluator(
                    new DocumentCategorizerME(model), listeners);

            evaluator.evaluate(trainingSampleStream.getTestSampleStream());


            System.out.println("ACCURACY: " + evaluator.getAccuracy());

            documentAccuracy.add(evaluator.getAccuracy(),
                    evaluator.getDocumentCount());

        }
    }

    /**
     * Retrieves the accuracy for all iterations.
     *
     * @return the word accuracy
     */
    public double getDocumentAccuracy() {
        return documentAccuracy.mean();
    }

    /**
     * Retrieves the number of words which where validated over all iterations.
     * The result is the amount of folds multiplied by the total number of words.
     *
     * @return the word count
     */
    public long getDocumentCount() {
        return documentAccuracy.count();
    }
}