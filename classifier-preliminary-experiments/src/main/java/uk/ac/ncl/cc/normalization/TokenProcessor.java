package uk.ac.ncl.cc.normalization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by B4046044 on 27/05/2015.
 */
public class TokenProcessor {

    private List<Normalizer> normalizers = new ArrayList<Normalizer>();

    private static TokenProcessor instance = null;

    protected TokenProcessor() {

    }

    public static TokenProcessor getInstance() {
        if (instance == null) {
            instance = new TokenProcessor();
        }
        return instance;
    }

    public TokenProcessor addNormalizer(Normalizer normalizer) {
        normalizers.add(normalizer);
        return this;
    }

    public String process(String token) {
        for (Normalizer normalizer : normalizers) {
            token = normalizer.normalize(token);
        }
        return token;
    }
}
