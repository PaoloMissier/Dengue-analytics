package uk.ac.ncl.cc.normalization;

/**
 * Created by B4046044 on 29/06/2015.
 */
public class AccentsNormalizer implements Normalizer {

    public AccentsNormalizer() {

    }

    @Override
    public String normalize(String token) {
        StringBuilder sb = new StringBuilder(token.length());
        token = java.text.Normalizer.normalize(token, java.text.Normalizer.Form.NFD);
        for (char c : token.toCharArray()) {
            if (c <= '\u007F') sb.append(c);
        }
        return sb.toString();
    }
}
