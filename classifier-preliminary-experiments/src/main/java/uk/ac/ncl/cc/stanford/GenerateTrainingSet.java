package uk.ac.ncl.cc.stanford;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ncl.cc.normalization.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateTrainingSet {

    public static final String RAW_FILE = "tweetsNew1.txt";
    public static final String DST_FILE = "tweets.train";

    private static String[] ofThese = new String[] {
            "@", "http", "rt", "rn", "via", "tbr"
    };

    public static void main(String[] args) {

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
        tokenProcessor.addNormalizer(new AccentsNormalizer());

        try {

            FileInputStream fis = new FileInputStream(new File(RAW_FILE));
            FileOutputStream fos = new FileOutputStream(new File(DST_FILE));

            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {

                String[] tokens = tokenizer.tokenize(line);
                List<String> tokensList = Arrays.asList(tokens);
                List<String> featureSet = new ArrayList<String>();

                for (String token : tokensList.subList(1, tokensList.size())) {

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
                builder.append(tokens[0]).append("\t");
                for (String token1 : featureSet) {
                    builder.append(token1).append(" ");
                }
                bw.write(builder.toString().trim());
                bw.newLine();
            }
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
