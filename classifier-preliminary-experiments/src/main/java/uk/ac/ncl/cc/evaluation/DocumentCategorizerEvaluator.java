package uk.ac.ncl.cc.evaluation;

import opennlp.tools.doccat.DocumentCategorizer;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.util.eval.Evaluator;
import opennlp.tools.util.eval.Mean;

/**
 * The {@link DocumentCategorizerEvaluator} measures the performance of
 * the given {@link DocumentCategorizer} with the provided reference
 * {@link DocumentSample}s.
 *
 * @see DocumentCategorizer
 * @see DocumentSample
 */
public class DocumentCategorizerEvaluator extends Evaluator<DocumentSample> {

    private DocumentCategorizer categorizer;

    private Mean accuracy = new Mean();

    /**
     * Initializes the current instance.
     *
     * @param categorizer
     */
    public DocumentCategorizerEvaluator(DocumentCategorizer categorizer,
                                        DoccatEvaluationMonitor ... listeners) {
        super(listeners);
        this.categorizer = categorizer;
    }

    /**
     * Evaluates the given reference {@link DocumentSample} object.
     *
     * This is done by categorizing the document from the provided
     * {@link DocumentSample}. The detected category is then used
     * to calculate and update the score.
     *
     * @param sample the reference {@link TokenSample}.
     */
    public DocumentSample processSample(DocumentSample sample) {

        String document[] = sample.getText();

        double probs[] = categorizer.categorize(document);

        String cat = categorizer.getBestCategory(probs);

        if (sample.getCategory().equals(cat)) {
            accuracy.add(1);
        }
        else {
            accuracy.add(0);
        }

        return new DocumentSample(cat, sample.getText());
    }

    /**
     * Retrieves the accuracy of provided {@link DocumentCategorizer}.
     *
     * accuracy = correctly categorized documents / total documents
     *
     * @return the accuracy
     */
    public double getAccuracy() {
        return accuracy.mean();
    }

    public long getDocumentCount() {
        return accuracy.count();
    }

    /**
     * Represents this objects as human readable {@link String}.
     */
    @Override
    public String toString() {
        return "Accuracy: " + accuracy.mean() + "\n" +
                "Number of documents: " + accuracy.count();
    }
}