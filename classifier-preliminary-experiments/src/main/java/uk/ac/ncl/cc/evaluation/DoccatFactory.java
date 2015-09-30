package uk.ac.ncl.cc.evaluation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.BaseToolFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ext.ExtensionLoader;

/**
 * The factory that provides Doccat default implementations and resources
 */
public class DoccatFactory extends BaseToolFactory {

    private static final String FEATURE_GENERATORS = "doccat.featureGenerators";
    private static final String TOKENIZER_NAME = "doccat.tokenizer";

    private FeatureGenerator[] featureGenerators;
    private Tokenizer tokenizer;

    /**
     * Creates a {@link DoccatFactory} that provides the default implementation of
     * the resources.
     */
    public DoccatFactory() {
    }

    /**
     * Creates a {@link DoccatFactory}. Use this constructor to programmatically
     * create a factory.
     *
     * @param tokenizer
     * @param featureGenerators
     */
    public DoccatFactory(Tokenizer tokenizer, FeatureGenerator[] featureGenerators) {
        this.init(tokenizer, featureGenerators);
    }

    protected void init(Tokenizer tokenizer, FeatureGenerator[] featureGenerators) {

        this.featureGenerators = featureGenerators;
        this.tokenizer = tokenizer;
    }

    @Override
    public Map<String, String> createManifestEntries() {
        Map<String, String> manifestEntries = super.createManifestEntries();

        if (getTokenizer() != null) {
            manifestEntries.put(TOKENIZER_NAME, getTokenizer().getClass()
                    .getCanonicalName());
        }

        if (getFeatureGenerators() != null) {
            manifestEntries.put(FEATURE_GENERATORS, featureGeneratorsAsString());
        }

        return manifestEntries;
    }

    private String featureGeneratorsAsString() {
        List<FeatureGenerator> fgs = Arrays.asList(getFeatureGenerators());
        Iterator<FeatureGenerator> iter = fgs.iterator();
        StringBuilder sb = new StringBuilder();
        if (iter.hasNext()) {
            sb.append(iter.next().getClass().getCanonicalName());
            while (iter.hasNext()) {
                sb.append(',').append(iter.next().getClass().getCanonicalName());
            }
        }
        return sb.toString();
    }

    @Override
    public void validateArtifactMap() throws InvalidFormatException {
        // nothing to validate
    }

    public static DoccatFactory create(String subclassName, Tokenizer tokenizer,
                                       FeatureGenerator[] featureGenerators) throws InvalidFormatException {
        if (subclassName == null) {
            // will create the default factory
            return new DoccatFactory(tokenizer, featureGenerators);
        }
        try {
            DoccatFactory theFactory = ExtensionLoader.instantiateExtension(
                    DoccatFactory.class, subclassName);
            theFactory.init(tokenizer, featureGenerators);
            return theFactory;
        } catch (Exception e) {
            String msg = "Could not instantiate the " + subclassName
                    + ". The initialization throw an exception.";
            System.err.println(msg);
            e.printStackTrace();
            throw new InvalidFormatException(msg, e);
        }

    }

    private FeatureGenerator[] loadFeatureGenerators(String classNames) {
        String[] classes = classNames.split(",");
        FeatureGenerator[] fgs = new FeatureGenerator[classes.length];

        for (int i = 0; i < classes.length; i++) {
            fgs[i] = ExtensionLoader.instantiateExtension(FeatureGenerator.class,
                    classes[i]);
        }
        return fgs;
    }

    public FeatureGenerator[] getFeatureGenerators() {
        if (featureGenerators == null) {
            if (artifactProvider != null) {
                String classNames = artifactProvider
                        .getManifestProperty(FEATURE_GENERATORS);
                if (classNames != null) {
                    this.featureGenerators = loadFeatureGenerators(classNames);
                }
            }
            if (featureGenerators == null) { // could not load using artifact provider
                // load bag of words as default
                FeatureGenerator[] bow = { new BagOfWordsFeatureGenerator() };
                this.featureGenerators = bow;
            }
        }
        return featureGenerators;
    }

    public void setFeatureGenerators(FeatureGenerator[] featureGenerators) {
        this.featureGenerators = featureGenerators;
    }

    public Tokenizer getTokenizer() {
        if (this.tokenizer == null) {
            if (artifactProvider != null) {
                String className = artifactProvider.getManifestProperty(TOKENIZER_NAME);
                if (className != null) {
                    this.tokenizer = ExtensionLoader.instantiateExtension(
                            Tokenizer.class, className);
                }
            }
            if (this.tokenizer == null) { // could not load using artifact provider
                this.tokenizer = WhitespaceTokenizer.INSTANCE;
            }
        }
        return tokenizer;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

}
