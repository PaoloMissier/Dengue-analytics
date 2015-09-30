package uk.ac.ncl.cc.training;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ncl.cc.normalization.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preprocess {
    private static final Logger log = Logger.getLogger(Train.class.getName());

    private static String[] ofThese = new String[]{
            "@", "rn", "via", "tbr"
    };

    public static void main(String[] args) {
        String rawDataFilaPath = "tweetsForTraining.txt";
        String outputFilePath = "tweets.train";
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

        LemmaNormalizer lemmaNormalizer = new LemmaNormalizer("pt-pos-maxent.bin");
        lemmaNormalizer.ignoreToken("dengue");
        tokenProcessor.addNormalizer(lemmaNormalizer);

        try {
            FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
            FileInputStream fis = new FileInputStream(new File(rawDataFilaPath));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {

                String[] tokens = tokenizer.tokenize(line);
                List<String> rawTokens = Arrays.asList(tokens);
                List<String> featureSet = new ArrayList<String>();

                for (String token : rawTokens.subList(1, rawTokens.size())) {

                    if (StringUtils.startsWithAny(token.toLowerCase(), ofThese)) {
                        continue;
                    }

                    String tok = tokenProcessor.process(token);
                    if (tok.trim().length() == 0) {
                        continue;
                    }

                    featureSet.add(tok);
                }

                StringBuilder builder = new StringBuilder();
                builder.append(tokens[0]).append(" ");

                for (String token1 : featureSet) {
                    builder.append(token1).append(" ");
                }
                bw.write(builder.toString().trim());
                bw.newLine();
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            log.log(Level.INFO, e.getMessage());
        }
    }
}
