package uk.ac.ncl.cc;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ncl.cc.normalization.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mdanilakis on 26/05/15.
 */
public class Classification {

    public static void main(String[] args) {
        System.out.println("Testing classification...");
        Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        try {
            FileInputStream fis = new FileInputStream(new File("test.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;

            String[] ofThese = new String[] {
                    "@",
                    "rt",
                    "rn",
                    "via",
                    "tbr",
                    "http"
            };

            TokenProcessor tokenProcessor = TokenProcessor
                    .getInstance()
                    .addNormalizer(new SpecialCharacterNormalizer())
                    .addNormalizer(new PictographEmojiNormalizer())
                    .addNormalizer(new RepeatedLetterNormalizer());

            LemmaNormalizer lemmaNormalizer = new LemmaNormalizer("pt-pos-maxent.bin");
            lemmaNormalizer.ignoreToken("dengue");

            tokenProcessor.addNormalizer(lemmaNormalizer);

            while ((line = br.readLine()) != null) {

                // Tokenize the input string by a space character
                String[] tokens = tokenizer.tokenize(line);

                // The final feature set after applying some de-noising
                List<String> featureSet = new ArrayList<String>();

                for (String token : tokens) {
                    if (StringUtils.startsWithAny(token.toLowerCase(), ofThese)) {
                        continue;
                    }
                    String tok = tokenProcessor.process(token);
                    if (tok.trim().length() == 0) {
                        continue;
                    }
                    featureSet.add(tok);
                }

                tokens = featureSet.toArray(new String[featureSet.size()]);
                Categorizer categorizer = new Categorizer("pt-doccat.bin");
                System.out.println(categorizer.categorize(tokens) + " " + Arrays.toString(tokens));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
