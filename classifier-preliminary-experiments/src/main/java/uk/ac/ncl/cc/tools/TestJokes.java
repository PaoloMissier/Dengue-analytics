package uk.ac.ncl.cc.tools;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruno on 17/07/15.
 */
public class TestJokes {

    public static void main(String[] args) throws IOException {

        Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;

        FileInputStream fis = new FileInputStream(new File("tweetsNew1.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

        String line = null;
        while ((line = br.readLine()) != null) {

            if (tokenizer.tokenize(line)[0].equals("joke")) {
                System.out.println(line);
            }
        }
    }
}
