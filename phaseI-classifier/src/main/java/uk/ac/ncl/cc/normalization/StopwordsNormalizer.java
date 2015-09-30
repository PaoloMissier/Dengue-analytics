package uk.ac.ncl.cc.normalization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StopwordsNormalizer implements Normalizer {

    private List<String> stopwords = new ArrayList<String>();

    private String fromFile;

    public StopwordsNormalizer(String fromFile) {
        this.fromFile = fromFile;
        try {
            FileInputStream fis = new FileInputStream(new File(this.fromFile));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                stopwords.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fromFile;
    }

    public void setFileName(String fromFile) {
        this.fromFile = fromFile;
    }

    @Override
    public String normalize(String token) {
        if (stopwords.contains(token)) {
            return "";
        }
        return token;
    }
}
