package uk.ac.ncl.cc;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mdanilakis on 26/05/15.
 */
public class Categorizer {

    private DocumentCategorizerME categorizer;

    public Categorizer(String modelFileName) {
        InputStream modelIn = null;
        DoccatModel model = null;
        try {
            modelIn = new FileInputStream(modelFileName);
            model = new DoccatModel(modelIn);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelIn != null) try {
                modelIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        categorizer = new DocumentCategorizerME(model);
    }

    public String categorize(String tweet) {
        return categorizer.getBestCategory(categorizer.categorize(tweet));
    }

    public String categorize(String[] tweet) {
        return categorizer.getBestCategory(categorizer.categorize(tweet));
    }
}
