package uk.ac.ncl.cc.weka;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import uk.ac.ncl.cc.training.Train;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by B4046044 on 20/06/2015.
 */
public class ArffConverter {

    private static final Logger log = Logger.getLogger(ArffConverter.class.getName());

    public static void main(String[] args) {
        String tweetsFilePath = "tweets.train";
        String arffFilePath = "tweets.arff";
        try {
            FileOutputStream fos = new FileOutputStream(new File(arffFilePath));
            FileInputStream fis = new FileInputStream(new File(tweetsFilePath));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;

            bw.write("@RELATION tweets");
            bw.newLine();
            bw.newLine();
            bw.write("@ATTRIBUTE class {informative,joke,mosquito_focus,sickness}");
            bw.newLine();
            bw.write("@ATTRIBUTE tweet string");
            bw.newLine();
            bw.newLine();
            bw.write("@DATA");
            bw.newLine();

            String line = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = tokenizer.tokenize(line);
                List<String> tokensList = Arrays.asList(tokens);

                StringBuilder builder = new StringBuilder();
                for (String token : tokensList.subList(1, tokensList.size())) {
                    builder.append(token).append(" ");
                }

                bw.write(tokens[0] + ", " + "'" + builder.toString().trim() + "'");
                bw.newLine();
            }
            bw.flush();
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
