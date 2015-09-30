package uk.ac.ncl.cc.weka;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ncl.cc.normalization.*;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestClassifyInstance {

    public static void main(String[] args) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("bayes.model"));
        Object tmp = in.readObject();
        FilteredClassifier classifier = (FilteredClassifier) tmp;
        in.close();

        String[] categories = new String[]{"Info", "Joke", "Mosq", "Sick"};

        LemmaNormalizer lemmaNormalizer = new LemmaNormalizer("pt-pos-maxent.bin");
        lemmaNormalizer.ignoreToken("dengue");

        FileOutputStream fos = new FileOutputStream(new File("results.txt"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));

        FileInputStream fis = new FileInputStream(new File("test.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {

            String input = line;

            Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
            TokenProcessor tokenProcessor = TokenProcessor
                    .getInstance()
                    .addNormalizer(new LinkToWebpageNormalizer())
                    .addNormalizer(new LinkToImageNormalizer())
                    .addNormalizer(new SpecialCharacterNormalizer())
                    .addNormalizer(new PictographEmojiNormalizer())
                    .addNormalizer(new AbbreviationsNormalizer())
                    .addNormalizer(new RepeatedLetterNormalizer())
                    .addNormalizer(new NumericDataNormalizer())
                    .addNormalizer(new FunnyWordsNormalizer());

            tokenProcessor.addNormalizer(lemmaNormalizer);

            String[] tokens = tokenizer.tokenize(input);
            List<String> featureSet = new ArrayList<String>();

            String[] removals = new String[] {
                    "@",
                    "rn",
                    "via",
                    "tbr",
            };

            for (String token : tokens) {
                if (StringUtils.startsWithAny(token.toLowerCase(), removals)) {
                    continue;
                }
                String tok = tokenProcessor.process(token);
                if (tok.trim().length() == 0) {
                    continue;
                }
                featureSet.add(tok);
            }

            Instance instance = createTestInstance(StringUtils.join(featureSet, " ")).instance(0);

            double prediction = classifier.classifyInstance(instance);
            double[] probs = classifier.distributionForInstance(instance);

            List<String> probas = new ArrayList<String>();

            for (double prob : probs) {
                probas.add(String.format("%.12f", prob));
            }

            System.out.println(categories[(int) prediction] + " " + probas + " " + line);
            //bw.write(categories[(int) prediction] + " " + line);
            //bw.newLine();
        }
        //bw.flush();
        //bw.close();
    }

    private static Instances createTestInstance(String tweet) {
        FastVector categories = new FastVector(4);
        categories.addElement("informative");
        categories.addElement("joke");
        categories.addElement("mosquito_focus");
        categories.addElement("sickness");
        Attribute tweetAttr = new Attribute("tweet", (FastVector) null);
        FastVector attributes = new FastVector(2);
        attributes.addElement(new Attribute("class", categories));
        attributes.addElement(tweetAttr);
        Instances instances = new Instances("Tweets Relation", attributes, 1);
        instances.setClassIndex(0);
        Instance instance = new Instance(2);
        instance.setValue(tweetAttr, tweet);
        instances.add(instance);
        //System.out.println(instances);
        return instances;
    }
}
