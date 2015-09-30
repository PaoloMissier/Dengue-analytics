package uk.ac.ncl.cc.normalization;

/**
 * Created by B4046044 on 27/05/2015.
 */
public class PictographEmojiNormalizer implements Normalizer {

    public PictographEmojiNormalizer() {

    }

    /**
     *  Removes all miscellaneous symbols and pictographs including
     *  emoji characters within the token.
     *
     * @param token
     * @return
     */
    @Override
    public String normalize(String token) {
        return token.replaceAll("\\p{So}+", "");
        
    }
}
