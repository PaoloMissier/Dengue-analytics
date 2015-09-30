package uk.ac.ncl.cc.tools;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.*;
import java.util.*;

public class FindFrequentWords {



    public static void main(String[] args) throws IOException {

        Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        Map<String, Integer> freq = new HashMap<String, Integer>();

        FileInputStream fis = new FileInputStream(new File("tweetsNew1.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

        String line = null;
        while ((line = br.readLine()) != null) {

            List<String> tokensList = Arrays.asList(tokenizer.tokenize(line));
            for (String token : tokensList.subList(1, tokensList.size())) {

                String key = token.trim();

                Integer intCount = freq.get(key);

                int count = intCount == null ? 0 : intCount;

                freq.put(key, count + 1);
            }
        }
        br.close();

        // Get entries and sort them.
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(freq.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        // Put entries back in an ordered map.
        Map<String, Integer> orderedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : entries) {
            orderedMap.put(entry.getKey(), entry.getValue());
        }

        System.out.println(orderedMap);

    }
}
